package objects.entity.entityBehavior;

import objects.ObjectInstanceMovable;
import objects.entity.Entity;
import world.World;

public class EntityBehaviorFragile extends EntityBehavior {
    @Override
    public void onBorderCollision(World world, Entity entity) {
        world.removeEntity(entity);
    }

    @Override
    public void onBlockCollision(World world, Entity entity) {
        if (!ObjectInstanceMovable.noCollision(entity) && !ObjectInstanceMovable.noCollisionBlock(entity))
            world.removeEntity(entity);
    }

    @Override
    public void onEntityCollision(World world, Entity entity, Entity entityCollision) {
        if (!ObjectInstanceMovable.noCollision(entity) && !ObjectInstanceMovable.noCollisionEntity(entity) && 
                            !(ObjectInstanceMovable.noCollisionSame(entity) && entity.areSameType(entity)))
            world.removeEntity(entity);
    }
}
