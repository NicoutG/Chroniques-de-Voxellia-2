package objects.block.blockBehavior;

import objects.block.Block;
import objects.block.BlockType;
import objects.objectBehavior.ObjectBehaviorActivableProperty;
import tools.*;
import world.World;


public class BlockBehaviorActivableProperty extends BlockBehavior {
    private final ObjectBehaviorActivableProperty<BlockType, Block, BlockBehavior> commonBehavior;

    public BlockBehaviorActivableProperty(String property) {
        commonBehavior = new ObjectBehaviorActivableProperty<>(property);
    }

    @Override
    public void onAttachTo(Block block) {
        commonBehavior.onAttachTo(block);
    }

    @Override
    public void onStart(World world, Block block, Vector position) {
        commonBehavior.onStart(world, block, position);
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        commonBehavior.onActivated(world, block, position, network);
    }

    @Override
    public void onDesactivated(World world, Block block, Vector position, int network) {
        commonBehavior.onDesactivated(world, block, position, network);
    }

    
}
