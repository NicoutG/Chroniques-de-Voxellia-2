package objects.entity.entityBehavior;

import objects.entity.Entity;
import objects.entity.EntityType;
import objects.objectBehavior.ObjectBehaviorActivable;
import world.World;

public class EntityBehaviorActivable extends EntityBehaviorConnected {
    private final ObjectBehaviorActivable<EntityType, Entity, EntityBehavior> commonBehavior = new ObjectBehaviorActivable<>();

    protected void activate(World world, Entity entity, int network) {
    }

    protected void desactivate(World world, Entity entity, int network) {
    }

    @Override
    public void onStart(World world, Entity entity) {
        int network = getNetwork(entity);
        commonBehavior.onStart(world, entity, entity.getPosition(), () -> activate(world, entity, network), () -> desactivate(world, entity, network));
    }

    @Override
    public void onActivated(World world, Entity entity, int network) {
        commonBehavior.onActivated(world, entity, entity.getPosition(), network, () -> activate(world, entity, network));
    }

    @Override
    public void onDesactivated(World world, Entity entity, int network) {
        commonBehavior.onDesactivated(world, entity, entity.getPosition(), network, () -> desactivate(world, entity, network));
    }
}
