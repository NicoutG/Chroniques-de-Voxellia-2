package objects.entity.entityBehavior;

import objects.entity.Entity;
import tools.Vector;
import world.World;

public class EntityBehaviorPushable extends EntityBehavior {

    @Override
    public void onPush(World world, Entity entity, Vector move, Entity entityPush) {
        System.out.println(move);
        entity.move(world, move.x, move.y, Math.max(0,move.z));
    }
}
