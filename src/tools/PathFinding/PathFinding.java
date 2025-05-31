package tools.PathFinding;

import java.util.ArrayList;
import java.util.HashSet;

import objects.entity.Entity;
import tools.Vector;
import world.World;

public class PathFinding {

    public static void findPath(World world, Entity entity, Vector position, Vector destination) {
        ArrayList<Path> openList = initOpenList(position);
        HashSet<Vector> closeList = new HashSet<>();
        Vector dest = adaptPosition(destination);
        Boolean find = null;
        do {
            find = findPath(world, entity, dest, openList, closeList, 100, new NeighboorsFinderFly());
        }while(find == null);
        if (find == true) {
            ArrayList<Vector> path = getPathFromOpenList(openList);
            for (int i = 0; i < path.size(); i++)
                System.out.println(path.get(i));
        }
        else {
            System.out.println("No path");
        }
    }

    public static Vector adaptPosition(Vector position) {
        return new Vector((int)position.x + 0.5, (int)position.y + 0.5,(int)position.z + 0.5);
    }

    public static ArrayList<Path> initOpenList(Vector position) {
        ArrayList<Path> openList = new ArrayList<>();
        openList.add(new Path(adaptPosition(position), 0, leftEstimation(position, position), null));
        return openList;
    }

    public static ArrayList<Vector> getPathAndLinkToEntity(World world, Entity entity, ArrayList<Path> openList, NeighboorsFinder neighboorsFinder) {
        ArrayList<Vector> path = getPathFromOpenList(openList);
        int nbNodesToLink = Math.min(path.size() / 10, 10);
        if (nbNodesToLink <= 0)
            return path;
        Vector destination = path.get(nbNodesToLink);
        ArrayList<Path> openListLink = initOpenList(entity.getPosition());
        HashSet<Vector> closeList = new HashSet<>();
        Boolean find = null;
        final int step = 100;
        int max = nbNodesToLink * 100;
        do {
            find = findPath(world, entity, destination, openListLink, closeList, step, neighboorsFinder);
            max -= step;
        }while(find == null && 0 < max);
        if (find == false)
            return path;
        ArrayList<Vector> newPath = getPathFromOpenList(openListLink);
        for (int i = nbNodesToLink; i < path.size(); i++)
            newPath.add(path.get(i));
        return newPath;
    }

    public static ArrayList<Vector> getPathFromOpenList(ArrayList<Path> openList) {
        ArrayList<Vector> path = new ArrayList<>();
        if (!openList.isEmpty()) {
            Path node = openList.getFirst();
            while(node.parent != null) {
                path.addFirst(node.position);
                node = node.parent;
            }
        }
        return path;
    }
    
    public static Boolean findPath(World world, Entity entity, Vector destination, ArrayList<Path> openList, HashSet<Vector> closedList, int nbStep, NeighboorsFinder neighboorsFinder) {
        int i = 0;
        while(i < nbStep && !openList.isEmpty()) {
            Path node = openList.getFirst();
            if (node.position.equals(destination))
                return true;
            openList.remove(0);
            closedList.add(node.position);
            ArrayList<Vector> neighboors = neighboorsFinder.getNeighboors(world, entity, node.position, destination);
            for (Vector neighboor : neighboors)
                if (!closedList.contains(neighboor)) {
                    Path nodeNeighboor = new Path(neighboor, node.made + 1, leftEstimation(neighboor, destination), node);
                    boolean insert = true;
                    for (int j = 0; j < openList.size(); j++)
                        if (openList.get(j).position.equals(neighboor)) {
                            if (openList.get(j).made >= nodeNeighboor.made)
                                openList.remove(j);
                            else
                                insert = false;
                            break;
                        }
                    if (insert)
                        insertSorted(openList, nodeNeighboor);
                }
            i++;
        }
        if (openList.isEmpty())
            return false;
        return null;
    }

    private static double leftEstimation(Vector position, Vector destination) {
        double posX = position.x;
        double posY = position.y;
        double posZ = position.z;
        double destX = destination.x;
        double destY = destination.y;
        double destZ = destination.z;
        double difX = destX - posX;
        double difY = destY - posY;
        double difZ = destZ - posZ;
        if (destZ > posZ)
            return Math.abs(difX) + Math.abs(difY) + 2 * Math.abs(difZ);
        return Math.abs(difX) + Math.abs(difY) + Math.abs(difZ);
    }

    private static void insertSorted(ArrayList<Path> openList, Path path) {
        int low = 0;
        int high = openList.size();

        while (low < high) {
            int mid = (low + high) / 2;
            if (path.totalEstimation < openList.get(mid).totalEstimation) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        openList.add(low, path);
    }
}
