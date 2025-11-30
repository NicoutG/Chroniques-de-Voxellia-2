package objects.block.blockBehavior;

import objects.block.*;
import objects.objectBehavior.ObjectBehaviorVariant;
import tools.Vector;
import world.World;

public class BlockBehaviorVariant extends BlockBehavior {
    private final ObjectBehaviorVariant<BlockType, Block, BlockBehavior> commonBehavior = new ObjectBehaviorVariant<>();

    @Override
    public void onAttachTo(Block block) {
        commonBehavior.onAttachTo(block);
    }

    @Override
    public void onStart(World world, Block block, Vector position) {
        commonBehavior.onStart(block);
    }

    @Override
    public BlockBehaviorVariant clone() {
        return new BlockBehaviorVariant();
    }
}
