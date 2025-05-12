package block.blockBehavior;

import block.Block;
import game.Game;


public abstract class BlockBehaviorActivable extends BlockBehavior {

    public abstract void activate(Game game, Block block, int network);

    public abstract void desactivate(Game game, Block block, int network);

    @Override
    public void onAttachToBlock(Block block) {
        block.setState("activated", false);
        block.setState("network", 0);
    }

    @Override
    public void onActivated(Game game, Block block, int network) {
        if (network == getNetwork(block)) {
            activate(game, block, network);
            block.setStateModification("activated", true);
        }
    }

    @Override
    public void onDesactivated(Game game, Block block, int network) {
        if (network == getNetwork(block)) {
            desactivate(game, block, network);
            block.setStateModification("activated", false);
        }
    }

    public boolean getActivationState(Block block) {
        Object state = block.getState("activated");
        if (state != null && state instanceof Integer)
            return (boolean)state;
        return false;
    }

    public int getNetwork(Block block) {
        Object state = block.getState("network");
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
