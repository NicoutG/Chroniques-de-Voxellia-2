package objects.block.blockBehavior;

import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorLever extends BlockBehaviorConnected {

    @Override
    public void onInteraction(World world, Block block, Vector position, Entity entity) {
        boolean activationState = getActivationState(block);
        world.executeAfterUpdate(() -> block.setState(ACTIVATION_STATE, !activationState));
        int network = getNetwork(block);
        if (activationState) {
            world.desactivateBlocks(network);
            block.setIndexTexture(1);
        }
        else {
            world.activateBlocks(network);
            block.setIndexTexture(0);
        }
    }
}
