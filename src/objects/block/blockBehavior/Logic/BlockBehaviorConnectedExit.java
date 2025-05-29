package objects.block.blockBehavior.Logic;

import objects.block.Block;
import objects.block.blockBehavior.BlockBehaviorConnected;

public class BlockBehaviorConnectedExit extends BlockBehaviorConnected{
    public final static String NETWORK_EXIT = "networkExit";
    
    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(NETWORK_EXIT, 1);
    }

    public int getNetworkExit(Block block) {
        Object state = block.getState(NETWORK_EXIT);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
