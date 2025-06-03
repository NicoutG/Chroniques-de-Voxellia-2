package objects.block.blockBehavior.logic;

import objects.block.Block;
import tools.Vector;
import world.World;

public class BlockBehaviorAnd extends BlockBehaviorActivable2to1 {

    @Override
    public void onStart(World world, Block block, Vector position) {
        int network = getNetwork(block);
        int network2 = getNetwork2(block);
        int networkExit = getNetworkExit(block);
        if (network == networkExit && network2 == networkExit) {
            System.out.println(NETWORK + " and " + NETWORK2 + " and " + NETWORK_EXIT + " have the same value ("+network+"). Interruption to avoid infinite loop");
            System.exit(0);
        }
    }

    @Override
    protected boolean isActivated(Block block) {
        boolean activationState = getActivationState(block);
        boolean activationState2 = getActivationState2(block);
        return activationState && activationState2;
    }
}
