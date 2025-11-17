package objects.entity.entityBehavior;

import objects.entity.Entity;
import objects.entity.EntityType;
import objects.objectBehavior.ObjectBehaviorEffect;
import world.World;

public class EntityBehaviorEffect extends EntityBehavior {
    private final ObjectBehaviorEffect<EntityType, Entity, EntityBehavior> commonBehavior;

    public EntityBehaviorEffect(int effectDuration) {
        commonBehavior = new ObjectBehaviorEffect<>(effectDuration);
    }

    @Override
    public void onAttachTo(Entity entity) {
        commonBehavior.onAttachTo(entity);
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        if (entity.getTickFrame0() + commonBehavior.effectDuration <= World.getTick()) {
            world.executeAfterUpdate(() -> world.getEntities().remove(entity));
        }
    }
}
