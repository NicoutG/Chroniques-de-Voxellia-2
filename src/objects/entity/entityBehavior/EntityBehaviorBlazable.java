package objects.entity.entityBehavior;

import objects.entity.EntityType;
import objects.entity.Entity;
import objects.objectBehavior.ObjectBehaviorBlazable;
import world.World;

public class EntityBehaviorBlazable extends EntityBehavior {
    private final ObjectBehaviorBlazable<EntityType, Entity, EntityBehavior> commonBehavior = new ObjectBehaviorBlazable<>();

    @Override
    public void onUpdate(World world, Entity entity) {
        commonBehavior.onUpdate(world, entity, entity.getPosition());
    }
}
