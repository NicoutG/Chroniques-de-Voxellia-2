package objects.block.blockBehavior;

import objects.block.Block;
import objects.block.BlockType;
import objects.objectBehavior.ObjectBehaviorActivable;
import tools.Vector;
import world.World;

public class BlockBehaviorActivable extends BlockBehaviorConnected {
    private final ObjectBehaviorActivable<BlockType, Block, BlockBehavior> commonBehavior = new ObjectBehaviorActivable<>();

    protected void activate(World world, Block block, Vector position, int network) {
    }

    protected void desactivate(World world, Block block, Vector position, int network) {
    }

    @Override
    public void onStart(World world, Block block, Vector position) {
        int network = getNetwork(block);
        commonBehavior.onStart(world, block, position, () -> activate(world, block, position, network), () -> desactivate(world, block, position, network));
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        commonBehavior.onActivated(world, block, position, network, () -> activate(world, block, position, network));
    }

    @Override
    public void onDesactivated(World world, Block block, Vector position, int network) {
        commonBehavior.onDesactivated(world, block, position, network, () -> desactivate(world, block, position, network));
    }
}
