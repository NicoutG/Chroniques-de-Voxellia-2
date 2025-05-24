package objects.entity.entityBehavior;

import objects.entity.Entity;
import world.World;

public class EntityBehaviorKill extends EntityBehavior{
    @Override
    public void onEntityCollision(World world, Entity entity, Entity entityCollision) {
        entityCollision.onDeath(world);
    }
}
