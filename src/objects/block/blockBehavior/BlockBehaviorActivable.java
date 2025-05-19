package objects.block.blockBehavior;

import objects.block.Block;
import tools.*;
import world.World;


public abstract class BlockBehaviorActivable extends BlockBehaviorConnected {

    public abstract void activate(World world, Block block, Vector position, int network);

    public abstract void desactivate(World world, Block block, Vector position, int network);

    @Override
    public void onStart(World world, Block block, Vector position) {
        boolean activationState = getActivationState(block);
        int network = getNetwork(block);
        block.setState(ACTIVATION_STATE, !activationState);
        if (activationState)
            activate(world, block, position, network);
        else
            desactivate(world, block, position, network);
        block.setState(ACTIVATION_STATE, activationState);
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        if (network == getNetwork(block)) {
            activate(world, block, position, network);
            world.executeAfterUpdate(() -> block.setState(ACTIVATION_STATE, true));
        }
    }

    @Override
    public void onDesactivated(World world, Block block, Vector position, int network) {
        if (network == getNetwork(block)) {
            desactivate(world, block, position, network);
            world.executeAfterUpdate(() -> block.setState(ACTIVATION_STATE, false));
        }
    }

    
}
