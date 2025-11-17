package objects.entity.entityBehavior;

import objects.entity.Entity;
import world.World;

public class EntityBehaviorApplyForce extends EntityBehavior {
    private static final String FORCE = "force";

    public void onAttachTo(Entity entity) {
        entity.setState(FORCE, new double[] {0, 0, -0.24});
    }

    @Override
    public void onUpdate (World world, Entity entity) {
        double[] force = getForce(entity);
        if (force != null)
            entity.addVelocity(force[0], force[1], force[2]);
    }

    public double[] getForce(Entity entity) {
        Object state = entity.getState(FORCE);
        if (state != null && state instanceof double[] && ((double[])state).length == 3)
            return (double[])state;
        return null;
    }
}
