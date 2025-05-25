package objects.entity.entityBehavior;

import objects.entity.Entity;
import tools.Vector;
import world.World;

public class EntityBehaviorActivableMoving extends EntityBehaviorActivable {
    public final static String SPEED = "speed";
    public final static String POSITION1 = "position1";
    public final static String POSITION2 = "position2";

    @Override
    public void onAttachTo(Entity entity) {
        super.onAttachTo(entity);
        entity.setState(SPEED, 1.0);
        entity.setState(POSITION1, new double[]{entity.getX(),entity.getY(),entity.getZ()});
        entity.setState(POSITION2, new double[]{0,0,0});
    }

    public void onUpdate(World world, Entity entity) {
        if (getActivationState(entity))
            move(world, entity, getPosition2(entity));
        else
            move(world, entity, getPosition1(entity));
    }

    private void move(World world, Entity entity, Vector destination) {
        Vector position = entity.getPosition();
        if (!position.equals(destination)) {
            double speed = getSpeed(entity);
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

    public double getSpeed(Entity entity) {
        Object state = entity.getState(SPEED);
        if (state != null && state instanceof Double)
            return (double)state;
        return -1;
    }

    public Vector getPosition1(Entity entity) {
        Object state = entity.getState(POSITION1);
        if (state != null && state instanceof double[]) {
            double[] pos = (double[])state;
            return new Vector(pos[0],pos[1],pos[2]);
        }
        return null;
    }

    public Vector getPosition2(Entity entity) {
        Object state = entity.getState(POSITION2);
        if (state != null && state instanceof double[]) {
            double[] pos = (double[])state;
            return new Vector(pos[0],pos[1],pos[2]);
        }
        return null;
    }
    
}
