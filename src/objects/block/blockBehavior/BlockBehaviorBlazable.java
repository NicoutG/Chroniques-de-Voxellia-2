package objects.block.blockBehavior;

import objects.block.Block;
import objects.block.BlockType;
import objects.objectBehavior.ObjectBehaviorBlazable;
import tools.Vector;
import world.World;

public class BlockBehaviorBlazable extends BlockBehavior {
    private final ObjectBehaviorBlazable<BlockType, Block, BlockBehavior> commonBehavior = new ObjectBehaviorBlazable<>();

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        commonBehavior.onUpdate(world, block, position);
    }
}
