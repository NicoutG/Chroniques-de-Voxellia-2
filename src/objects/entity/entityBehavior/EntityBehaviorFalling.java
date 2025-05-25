package objects.entity.entityBehavior;

import objects.entity.Entity;
import world.World;

public class EntityBehaviorFalling extends EntityBehavior {
    
    @Override
    public void onUpdate (World world, Entity entity) {
        entity.addVelocity(0, 0, -0.3);
    }
}
