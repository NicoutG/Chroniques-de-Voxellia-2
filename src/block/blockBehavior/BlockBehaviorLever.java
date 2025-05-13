package block.blockBehavior;

import block.Block;
import entity.Entity;
import game.Game;
import tools.Vector;

public class BlockBehaviorLever extends BlockBehaviorConnected {

    public BlockBehavior clone() {
        return this;
    }

    @Override
    public void onInteraction(Game game, Block block, Vector position, Entity entity) {
        boolean activationState = getActivationState(block);
        block.setStateModification(ACTIVATION_STATE, !activationState);
        int network = getNetwork(block);
        if (activationState)
            game.desactivateBlocks(network);
        else
            game.activateBlocks(network);
    }
}
