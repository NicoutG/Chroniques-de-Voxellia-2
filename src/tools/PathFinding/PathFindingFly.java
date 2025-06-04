package tools.PathFinding;

import java.util.ArrayList;
import java.util.Random;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.Vector;
import world.World;

public class PathFindingFly extends PathFindingType {

    @Override
    public ArrayList<Vector> getNeighboors(World world, Entity entity, Vector position, Vector destination) {
        ArrayList<Vector> neighboors = new ArrayList<>();
        final double x = position.x;
        final double y = position.y;
        final double z = position.z;
        Vector[] positions = new Vector[] {
            new Vector(x,y,z-1),
            new Vector(x,y,z+1),
            new Vector(x,y-1,z),
            new Vector(x,y+1,z),
            new Vector(x-1,y,z),
            new Vector(x+1,y,z),
        };
        for (Vector posNeigh : positions) {
            if (posNeigh.equals(destination) || isValidNeighboor(world, entity, posNeigh))
                neighboors.add(posNeigh);
        }
        return neighboors;
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
            double difZ1 = pos2.z - pos1.z;
            double difZ2 = pos3.z - pos2.z;
            boolean isAngle = false;
            Vector angle = null;
            if (difX1 != difX2 && difY1 != difY2) {
                isAngle = true;
                angle = new Vector(pos1.x + pos3.x - pos2.x, pos1.y + pos3.y - pos2.y, pos1.z);
            }
            else if (difX1 != difX2 && difZ1 != difZ2) {
                isAngle = true;
                angle = new Vector(pos1.x + pos3.x - pos2.x, pos1.y, pos1.z + pos3.z - pos2.z);
            }
            else if (difY1 != difY2 && difZ1 != difZ2) {
                isAngle = true;
                angle = new Vector(pos1.x, pos1.y + pos3.y - pos2.y, pos1.z + pos3.z - pos2.z);
            }
            if (isAngle)
                if (isValidNeighboor(world, entity, angle))
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
        double difZ = destination.z - entity.getZ();
        if (0 < difZ) {
            if (epsilon <= difZ  || (random.nextDouble() < getProbaChange(difZ, epsilon)))
                actions.add(EntityAction.ABOVE);
        }
        else if (difZ < 0) {
            if (difZ <= -epsilon  || (random.nextDouble() < getProbaChange(-difZ, epsilon)))
                actions.add(EntityAction.BELOW);
        }
        return actions.toArray(new EntityAction[0]);
    }
}
