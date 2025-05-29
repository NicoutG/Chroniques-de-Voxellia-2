package objects.entity.entityBehavior;

import objects.entity.*;
import objects.objectBehavior.ObjectBehaviorActivableProperty;
import world.World;


public class EntityBehaviorActivableProperty extends EntityBehavior {
    private final ObjectBehaviorActivableProperty<EntityType, Entity, EntityBehavior> commonBehavior;

    public EntityBehaviorActivableProperty(String property) {
        commonBehavior = new ObjectBehaviorActivableProperty<>(property);
    }
    
    @Override
    public void onAttachTo(Entity entity) {
        commonBehavior.onAttachTo(entity);
    }

    @Override
    public void onStart(World world, Entity entity) {
        commonBehavior.onStart(world, entity, entity.getPosition());
    }

    @Override
    public void onActivated(World world, Entity entity, int network) {
        commonBehavior.onActivated(world, entity, entity.getPosition(), network);
    }

    @Override
    public void onDesactivated(World world, Entity entity, int network) {
        commonBehavior.onDesactivated(world, entity, entity.getPosition(), network);
    }

    
}
