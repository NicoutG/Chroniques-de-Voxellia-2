package objects.block.blockBehavior;

import objects.block.Block;
import tools.*;
import world.World;


public abstract class BlockBehaviorActivable extends BlockBehaviorConnected {

    public abstract void activate(World world, Block block, Vector position, int network);

    public abstract void desactivate(World world, Block block, Vector position, int network);

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        if (network == getNetwork(block)) {
            activate(world, block, position, network);
            block.setStateModification(ACTIVATION_STATE, true);
        }
    }

    @Override
    public void onDesactivated(World world, Block block, Vector position, int network) {
        if (network == getNetwork(block)) {
            desactivate(world, block, position, network);
            block.setStateModification(ACTIVATION_STATE, false);
        }
    }

    
}
