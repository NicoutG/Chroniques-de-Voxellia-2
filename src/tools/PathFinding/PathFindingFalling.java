package tools.PathFinding;

import java.util.ArrayList;
import java.util.Random;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.Vector;
import world.World;

public class PathFindingFalling extends PathFindingType {

    @Override
    public ArrayList<Vector> getNeighboors(World world, Entity entity, Vector position, Vector destination) {
        ArrayList<Vector> neighboors = new ArrayList<>();
        final double x = position.x;
        final double y = position.y;
        final double z = position.z;
        Vector posBelow = new Vector(x,y,z-1);
        boolean valid = isValidNeighboor(world, entity, posBelow);
        boolean hasFloor = !valid && (0 < posBelow.z);
        if (posBelow.equals(destination) || valid)
            neighboors.add(posBelow);
        Vector posBelowBelow = new Vector(posBelow.x, posBelow.y, posBelow.z - 1);
        Vector[] positionsSide = new Vector[] {
            new Vector(x,y-1,z),
            new Vector(x,y+1,z),
            new Vector(x-1,y,z),
            new Vector(x+1,y,z),
        };
        for (Vector posSide : positionsSide)
            if ((posSide.equals(destination) || isValidNeighboor(world, entity, posSide)) && 
                    (hasFloor || (!isValidNeighboor(world, entity, new Vector(posSide.x, posSide.y, posSide.z - 1)) && 1 < posSide.z && !isValidNeighboor(world, entity, posBelowBelow))))
                neighboors.add(posSide);
        if (hasFloor) {
            Vector posAbove = new Vector(x,y,z+1);
            if (posAbove.equals(destination) || isValidNeighboor(world, entity, posAbove))
                neighboors.add(posAbove);
        }
        return neighboors;
    }

    @Override
    public Vector getRandomDestination(World world, Entity entity, int distance) {
        Random random = new Random();
        int nbSteps = Math.max(1, distance/2 + random.nextInt(Math.max(1,distance/2)));
        ArrayList<Vector> closedList = new ArrayList<>();
        closedList.add(0,PathFinding.adaptPosition(world, entity.getPosition()));
        for (int i = 0; i < nbSteps; i++) {
            Vector pos = closedList.get(0);
            ArrayList<Vector> neighboors = getNeighboors(world, entity, pos, null);
            Vector next = null;
            boolean found = false;
            while(0 < neighboors.size() && !found) {
                int index = random.nextInt(neighboors.size());
                next = neighboors.get(index);
                if (closedList.contains(next))
                    neighboors.remove(next);
                else
                    found = true;
            }
            if (!found) {
                do {
                    pos = closedList.get(0);
                    if (!hasFloor(world, entity, pos))
                        closedList.remove(0);
                    else
                        return pos;
                }while (0 < closedList.size());
                return pos;
            }
            closedList.add(0,next);
        }
        Vector pos;
        do {
            pos = closedList.get(0);
            if (!hasFloor(world, entity, pos))
                closedList.remove(0);
            else
                return pos;
        }while (0 < closedList.size());
        return pos;
    }

    private boolean hasFloor(World world, Entity entity, Vector position) {
        Vector posBelow = new Vector(position.x,position.y,position.z-1);
        boolean hasFloor = !isValidNeighboor(world, entity, posBelow) && (0 < posBelow.z);
        return hasFloor;
    }

    @Override
    public ArrayList<Vector> refinePath(World world, Entity entity, ArrayList<Vector> path) {
        for (int i = 0; i < path.size() - 2; i++) {
            Vector pos1 = path.get(i);
            Vector pos2 = path.get(i+1);
            Vector pos3 = path.get(i+2);
            double difX1 = pos2.x - pos1.x;
            double difX2 = pos3.x - pos2.x;
            double difY1 = pos2.y - pos1.y;
            double difY2 = pos3.y - pos2.y;
            if (difX1 != difX2 && difY1 != difY2) {
                Vector angle = new Vector(pos1.x + pos3.x - pos2.x, pos1.y + pos3.y - pos2.y, pos1.z);
                if (isValidNeighboor(world, entity, angle))
                    path.remove(i+1);
            }
            double difZ1 = pos2.z - pos1.z;
            double difZ2 = pos3.z - pos2.z;
            if (difZ2 < difZ1)
                path.remove(i+1);
        }
        if (1 < path.size())
            path.remove(0);
        return path;
    }

    @Override
    public EntityAction[] convertToAction(Entity entity, Vector destination, double epsilon) {
        ArrayList<EntityAction> actions = new ArrayList<>();
        Random random = new Random();
        double difX = destination.x - entity.getX();
        if (0 < difX) {
            if (epsilon <= difX || (random.nextDouble() < getProbaChange(difX, epsilon)))
                actions.add(EntityAction.RIGHT);
        }
        else if (difX < 0) {
            if (difX <= -epsilon || (random.nextDouble() < getProbaChange(-difX, epsilon)))
                actions.add(EntityAction.LEFT);
        }
        double difY = destination.y - entity.getY();
        if (0 < difY) {
            if (epsilon <= difY  || (random.nextDouble() < getProbaChange(difY, epsilon)))
                actions.add(EntityAction.BOTTOM);
        }
        else if (difY < 0) {
            if (difY <= -epsilon  || (random.nextDouble() < getProbaChange(-difY, epsilon)))
                actions.add(EntityAction.TOP);
        }
        double zMin = entity.getCollision().getBounds(entity.getPosition())[4];
        int difZ = (int)destination.z - (int)zMin;
        if (0 < difZ)
            actions.add(EntityAction.JUMP);
        return actions.toArray(new EntityAction[0]);
    }
}
