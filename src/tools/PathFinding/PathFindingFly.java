package tools.PathFinding;

import java.util.ArrayList;

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
        double difX = destination.x - entity.getX();
        if (difX <= -epsilon)
            actions.add(EntityAction.LEFT);
        else if (epsilon <= difX)
            actions.add(EntityAction.RIGHT);
        double difY = destination.y - entity.getY();
        if (difY <= -epsilon)
            actions.add(EntityAction.TOP);
        else if (epsilon <= difY)
            actions.add(EntityAction.BOTTOM);
        double difZ = destination.z - entity.getZ();
        if (difZ <= -epsilon)
            actions.add(EntityAction.BELOW);
        else if (epsilon <= difZ)
            actions.add(EntityAction.ABOVE);
        return actions.toArray(new EntityAction[0]);
    }
}
