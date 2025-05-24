package objects.block.blockBehavior;

import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorKill extends BlockBehavior {

    @Override
    public void onEntityClose(World world, Block block, Vector position, Entity entity) {
        if (block.getCollision().collision(position, entity.getCollision(), entity.getPosition()))
            entity.onDeath(world);
    }
}
