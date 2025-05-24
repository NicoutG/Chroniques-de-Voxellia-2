package objects.block.blockBehavior;

import world.World;

public class BlockBehaviorNewWorld extends BlockBehaviorChangeWorld {

    @Override
    protected void loadWorld(World world, String newWorld, int spawnPoint) {
        world.executeAfterUpdate(() -> world.loadWorld0(newWorld, spawnPoint));
    }
}
