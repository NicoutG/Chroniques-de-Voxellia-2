package objects.block.blockBehavior;

import objects.block.Block;
import objects.block.BlockType;
import objects.objectBehavior.ObjectBehaviorActivableLight;
import tools.*;
import world.World;


public class BlockBehaviorActivableLight extends BlockBehavior {
    private final ObjectBehaviorActivableLight<BlockType, Block, BlockBehavior> commonBehavior = new ObjectBehaviorActivableLight<>();

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
