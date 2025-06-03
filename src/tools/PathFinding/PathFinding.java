package tools.PathFinding;

import java.util.ArrayList;
import java.util.HashSet;

import objects.entity.Entity;
import tools.Vector;
import world.World;

public class PathFinding {

    public static void findPath(World world, Entity entity, Vector position, Vector destination) {
        ArrayList<Node> openList = initOpenList(position);
        HashSet<Vector> closeList = new HashSet<>();
        Vector dest = adaptPosition(destination);
        Boolean find = null;
        int nb = 0;
        do {
            find = findPath(world, entity, dest, openList, closeList, 1, new PathFindingFalling());
            nb++;
        }while(find == null);
        System.out.println(nb);
        if (find == true) {
            ArrayList<Vector> path = getPathAndLinkToEntity(world, entity, openList, new PathFindingFalling());
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

    public static ArrayList<Node> initOpenList(Vector position) {
        ArrayList<Node> openList = new ArrayList<>();
        openList.add(new Node(adaptPosition(position), 0, 0, null));
        return openList;
    }

    public static ArrayList<Vector> getPathAndLinkToEntity(World world, Entity entity, ArrayList<Node> openList, PathFindingType pathFindingType) {
        ArrayList<Vector> path = getPathFromOpenList(openList);
        int nbNodesToLink = Math.min(path.size() / 10, 10);
        if (nbNodesToLink <= 0)
            return pathFindingType.refinePath(world, entity, path);
        Vector destination = path.get(nbNodesToLink);
        ArrayList<Node> openListLink = initOpenList(entity.getPosition());
        HashSet<Vector> closeList = new HashSet<>();
        Boolean find = null;
        final int step = 100;
        int max = nbNodesToLink * 100;
        do {
            find = findPath(world, entity, destination, openListLink, closeList, step, pathFindingType);
            max -= step;
        }while(find == null && 0 < max);
        if (find != null && !find)
            return pathFindingType.refinePath(world, entity, path);
        ArrayList<Vector> newPath = getPathFromOpenList(openListLink);
        for (int i = nbNodesToLink; i < path.size(); i++)
            newPath.add(path.get(i));
        return pathFindingType.refinePath(world, entity, newPath);
    }

    public static ArrayList<Vector> getPathFromOpenList(ArrayList<Node> openList) {
        ArrayList<Vector> path = new ArrayList<>();
        if (!openList.isEmpty()) {
            Node node = openList.getFirst();
            path.addFirst(node.position);
            while(node.parent != null) {
                node = node.parent;
                path.addFirst(node.position);
            }
        }
        return path;
    }
    
    public static Boolean findPath(World world, Entity entity, Vector destination, ArrayList<Node> openList, HashSet<Vector> closedList, int nbStep, PathFindingType pathFindingType) {
        int i = 0;
        while(i < nbStep && !openList.isEmpty()) {
            Node node = openList.getFirst();
            if (node.position.equals(destination))
                return true;
            openList.remove(0);
            closedList.add(node.position);
            ArrayList<Vector> neighboors = pathFindingType.getNeighboors(world, entity, node.position, destination);
            for (Vector neighboor : neighboors)
                if (!closedList.contains(neighboor)) {
                    Node nodeNeighboor = new Node(neighboor, node.made + 1, leftEstimation(neighboor, destination), node);
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
        final double coef = 1.42;
        if (destZ > posZ)
            return coef * Math.sqrt(difX * difX + difY * difY + 2 * difZ * difZ);
        return coef * Math.sqrt(difX * difX + difY * difY + difZ * difZ);
    }

    private static void insertSorted(ArrayList<Node> openList, Node path) {
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
