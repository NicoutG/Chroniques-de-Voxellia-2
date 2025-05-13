package block.blockBehavior;

import block.Block;

public abstract class BlockBehaviorConnected extends BlockBehavior {
    protected final static String ACTIVATION_STATE = "activated";
    protected final static String NETWORK = "network";

    @Override
    public void onAttachToBlock(Block block) {
        block.setState(ACTIVATION_STATE, false);
        block.setState(NETWORK, 0);
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
