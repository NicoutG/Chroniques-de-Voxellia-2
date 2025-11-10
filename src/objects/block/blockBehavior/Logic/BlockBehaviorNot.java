package objects.block.blockBehavior.logic;

import objects.block.Block;
import tools.Vector;
import world.World;

public class BlockBehaviorNot extends BlockBehaviorConnectedExit {

    @Override
    public void onStart(World world, Block block, Vector position) {
        int network = getNetwork(block);
        int networkExit = getNetworkExit(block);
        if (network == networkExit) {
            System.out.println(NETWORK + " and " + NETWORK_EXIT + " have the same value ("+network+"). Interruption to avoid infinite loop");
            System.exit(0);
        }
    }

    @Override
    public void activate(World world, Block block, Vector position, int network) {
        world.desactivate(getNetworkExit(block));
    }

    @Override
    public void desactivate(World world, Block block, Vector position, int network) {
        world.activate(getNetworkExit(block));
    }
}
