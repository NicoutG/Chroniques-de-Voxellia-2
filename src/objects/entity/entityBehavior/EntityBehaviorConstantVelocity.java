package objects.entity.entityBehavior;

import objects.entity.Entity;
import world.World;

public class EntityBehaviorConstantVelocity extends EntityBehavior {
    public static final String VELOCITY = "velocity";

    public void onAttachTo(Entity entity) {
        entity.setState(VELOCITY, new double[] {0, 0, 0});
    }

    @Override
    public void onUpdate (World world, Entity entity) {
        double[] velocity = getVelocity(entity);
        if (velocity != null)
            entity.setVelocity(velocity[0], velocity[1], velocity[2]);
    }

    public double[] getVelocity(Entity entity) {
        Object state = entity.getState(VELOCITY);
        if (state != null && state instanceof double[] && ((double[])state).length == 3)
            return (double[])state;
        return null;
    }
}
