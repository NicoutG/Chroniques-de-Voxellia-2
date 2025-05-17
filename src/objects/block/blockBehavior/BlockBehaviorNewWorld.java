package objects.block.blockBehavior;

import world.World;

public class BlockBehaviorNewWorld extends BlockBehaviorChangeWorld {

    @Override
    protected void loadWorld(World world, String newWorld, int spawnPoint) {
        world.clearWorlds();
        world.executeAfterUpdate(() -> world.loadWorld(newWorld, spawnPoint));
    }
}
