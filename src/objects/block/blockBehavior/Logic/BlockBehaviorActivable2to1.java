package objects.block.blockBehavior.logic;

import objects.block.Block;
import tools.Vector;
import world.World;

public abstract class BlockBehaviorActivable2to1 extends BlockBehaviorConnectedExit {
    public final static String ACTIVATION_STATE2 = BlockBehaviorConnectedExit.ACTIVATION_STATE+"2";
    public final static String NETWORK2 = BlockBehaviorConnectedExit.NETWORK+"2";

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(ACTIVATION_STATE2, getActivationState(block));
        block.setState(NETWORK2, getNetwork(block));
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        int network1 = getNetwork(block);
        int network2 = getNetwork2(block);
        if (network == network1 || network == network2) {
            boolean wasActivated = isActivated(block);
            if (network == network1)
                block.setState(ACTIVATION_STATE, true);
            else
                block.setState(ACTIVATION_STATE2, true);
            if (!wasActivated && isActivated(block))
                world.activate(getNetworkExit(block));
        }
    }

    @Override
    public void onDesactivated(World world, Block block, Vector position, int network) {
        int network1 = getNetwork(block);
        int network2 = getNetwork2(block);
        if (network == network1 || network == network2) {
            boolean wasActivated = isActivated(block);
            if (network == network1)
                block.setState(ACTIVATION_STATE, false);
            else
                block.setState(ACTIVATION_STATE2, false);
            if (wasActivated && !isActivated(block))
                world.desactivate(getNetworkExit(block));
        }
    }

    protected abstract boolean isActivated(Block block);

    public boolean getActivationState2(Block block) {
        Object state = block.getState(ACTIVATION_STATE2);
        if (state != null && state instanceof Boolean)
            return (boolean)state;
        return false;
    }

    public int getNetwork2(Block block) {
        Object state = block.getState(NETWORK2);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }

}
