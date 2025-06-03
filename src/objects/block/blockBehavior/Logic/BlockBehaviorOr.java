package objects.block.blockBehavior.logic;

import objects.block.Block;
import tools.Vector;
import world.World;

public class BlockBehaviorOr extends BlockBehaviorActivable2to1 {

    @Override
    public void onStart(World world, Block block, Vector position) {
        int network = getNetwork(block);
        int networkExit = getNetworkExit(block);
        if (network == networkExit) {
            System.out.println(NETWORK + " and " + NETWORK_EXIT + " have the same value ("+network+"). Interruption to avoid infinite loop");
            System.exit(0);
        }
        int network2 = getNetwork2(block);
        if (network2 == networkExit) {
            System.out.println(NETWORK2 + " and " + NETWORK_EXIT + " have the same value ("+network2+"). Interruption to avoid infinite loop");
            System.exit(0);
        }
    }

    @Override
    protected boolean isActivated(Block block) {
        boolean activationState = getActivationState(block);
        boolean activationState2 = getActivationState2(block);
        return activationState || activationState2;
    }
}
