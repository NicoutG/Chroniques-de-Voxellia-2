package objects.entity.entityBehavior;

import objects.entity.Entity;
import tools.Vector;
import world.World;

public class EntityBehaviorActivableMoving extends EntityBehaviorActivable {
    public final static String X1 = "x1";
    public final static String Y1 = "y1";
    public final static String Z1 = "z1";
    public final static String X2 = "x2";
    public final static String Y2 = "y2";
    public final static String Z2 = "z2";

    @Override
    public void onAttachTo(Entity entity) {
        super.onAttachTo(entity);
        entity.setState(X1,entity.getX());
        entity.setState(Y1,entity.getY());
        entity.setState(Z1,entity.getZ());
        entity.setState(X2,entity.getX());
        entity.setState(Y2,entity.getY());
        entity.setState(Z2,entity.getZ());
    }

    @Override
    

    public void onUpdate(World world, Entity entity) {
        if (getActivationState(entity)) 
            move(world, entity, getPosition2(entity));
        else
            move(world, entity, getPosition1(entity));
    }

    private void move(World world, Entity entity, Vector destination) {
        Vector position = entity.getPosition();
        if (!position.equals(destination)) {
            double speed = entity.getSpeed();
            double difX = destination.x - position.x;
            double difY = destination.y - position.y;
            double difZ = destination.z - position.z;
            double distance = Math.sqrt(difX*difX + difY*difY + difZ*difZ);
            if (distance < speed)
                entity.move(world, difX, difY, difZ);
            else
                entity.move(world, speed * difX / distance, speed * difY / distance, speed * difZ / distance);
        }
    }

    public Vector getPosition1(Entity entity) {
        Vector position = new Vector();
        Object state = entity.getState(X1);
        if (state != null && state instanceof Double) {
            position.x = (double)state;
            state = entity.getState(Y1);
            if (state != null && state instanceof Double) {
                position.y = (double)state;
                state = entity.getState(Z1);
                if (state != null && state instanceof Double) {
                    position.z = (double)state;
                    return position;
                }
            }
        }
        return null;
    }

    public Vector getPosition2(Entity entity) {
        Vector position = new Vector();
        Object state = entity.getState(X2);
        if (state != null && state instanceof Double) {
            position.x = (double)state;
            state = entity.getState(Y2);
            if (state != null && state instanceof Double) {
                position.y = (double)state;
                state = entity.getState(Z2);
                if (state != null && state instanceof Double) {
                    position.z = (double)state;
                    return position;
                }
            }
        }
        return null;
    }
    
}
