package block.blockBehavior;

import block.Block;
import game.Game;
import tools.*;


public abstract class BlockBehaviorActivable extends BlockBehavior {
    protected final static String ACTIVATION_STATE = "activated";
    protected final static String NETWORK = "network";

    public abstract void activate(Game game, Block block, Vector position, int network);

    public abstract void desactivate(Game game, Block block, Vector position, int network);

    @Override
    public void onAttachToBlock(Block block) {
        block.setState(ACTIVATION_STATE, false);
        block.setState(NETWORK, 0);
    }

    @Override
    public void onActivated(Game game, Block block, Vector position, int network) {
        if (network == getNetwork(block)) {
            activate(game, block, position, network);
            block.setStateModification(ACTIVATION_STATE, true);
        }
    }

    @Override
    public void onDesactivated(Game game, Block block, Vector position, int network) {
        if (network == getNetwork(block)) {
            desactivate(game, block, position, network);
            block.setStateModification(ACTIVATION_STATE, false);
        }
    }

    public boolean getActivationState(Block block) {
        Object state = block.getState(ACTIVATION_STATE);
        if (state != null && state instanceof Boolean)
            return (boolean)state;
        return false;
    }

    public int getNetwork(Block block) {
        Object state = block.getState(NETWORK);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
