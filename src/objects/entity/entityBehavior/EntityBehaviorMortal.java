package objects.entity.entityBehavior;

import objects.entity.Entity;
import world.World;

public class EntityBehaviorMortal extends EntityBehavior {

    @Override
    public void onDeath(World world, Entity entity) {
        world.getEntities().remove(entity);
    }
    
}
