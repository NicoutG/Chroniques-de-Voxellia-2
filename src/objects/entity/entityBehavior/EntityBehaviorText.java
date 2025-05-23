package objects.entity.entityBehavior;

import objects.entity.*;
import objects.objectBehavior.ObjectBehaviorText;
import world.World;

public class EntityBehaviorText extends EntityBehavior {
    private final ObjectBehaviorText<EntityType, Entity, EntityBehavior> commonBehavior = new ObjectBehaviorText<>();

    @Override
    public void onAttachTo(Entity entity) {
        commonBehavior.onAttachTo(entity);
    }

    @Override
    public void onInteraction(World world, Entity entity, Entity entityInteract) {
        commonBehavior.onInteraction(world, entity);
    }

    @Override
    public EntityBehaviorText clone() {
        return new EntityBehaviorText();
    }
}
