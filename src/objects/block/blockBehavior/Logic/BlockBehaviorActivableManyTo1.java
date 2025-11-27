package objects.block.blockBehavior.logic;

import objects.block.Block;
import tools.Vector;
import world.World;

public abstract class BlockBehaviorActivableManyTo1 extends BlockBehaviorConnectedExit {

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(ACTIVATION_STATE, null);
        block.setState(NETWORK, new int[0]);
    }

    @Override
    public void onStart(World world, Block block, Vector position) {
        super.onStart(world, block, position);
        int nbNetworks = getNetworks(block).length;
        block.setState(ACTIVATION_STATE, new boolean[nbNetworks]);
    }

    private void updateNetwork(World world, Block block, int network, boolean activate) {
        int[] networks = getNetworks(block);
        boolean[] states = getActivationStates(block);

        boolean wasActivated = isActivated(block);

        for (int i = 0; i < networks.length; i++) {
            if (networks[i] == network)
                states[i] = activate;
        }

        block.setState(ACTIVATION_STATE, states);
        
        boolean nowActivated = isActivated(block);

        if (!wasActivated && nowActivated)
            world.activate(getNetworkExit(block));
        else
            if (wasActivated && !nowActivated)
                world.desactivate(getNetworkExit(block));
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        updateNetwork(world, block, network, true);
    }

    @Override
    public void onDesactivated(World world, Block block, Vector position, int network) {
        updateNetwork(world, block, network, false);
    }

    protected abstract boolean isActivated(Block block);

    public boolean[] getActivationStates(Block block) {
        Object state = block.getState(ACTIVATION_STATE);
        if (state instanceof boolean[])
            return (boolean[])state;
        return new boolean[0];
    }

    public int[] getNetworks(Block block) {
        Object state = block.getState(NETWORK);
        if (state instanceof int[])
            return (int[])state;
        return new int[0];
    }
}
