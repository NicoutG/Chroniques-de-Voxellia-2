package objects.block.blockBehavior;

import model.world.World;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;

public class BlockBehaviorLever extends BlockBehaviorConnected {

    @Override
    public void onInteraction(World world, Block block, Vector position, Entity entity) {
        boolean activationState = getActivationState(block);
        block.setStateModification(ACTIVATION_STATE, !activationState);
        int network = getNetwork(block);
        if (activationState)
            world.desactivateBlocks(network);
        else
            world.activateBlocks(network);
    }
}
