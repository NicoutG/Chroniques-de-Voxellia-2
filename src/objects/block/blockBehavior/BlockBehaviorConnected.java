package objects.block.blockBehavior;

import objects.block.Block;
import objects.block.BlockType;
import objects.objectBehavior.ObjectBehaviorConnected;

public class BlockBehaviorConnected extends BlockBehavior {
    public final static String ACTIVATION_STATE = ObjectBehaviorConnected.ACTIVATION_STATE;
    public final static String NETWORK = ObjectBehaviorConnected.NETWORK;

    private final ObjectBehaviorConnected<BlockType, Block, BlockBehavior> commonBehavior = new ObjectBehaviorConnected<>();

    @Override
    public void onAttachTo(Block block) {
        commonBehavior.onAttachTo(block);
    }

    public boolean getActivationState(Block block) {
        return commonBehavior.getActivationState(block);
    }

    public int getNetwork(Block block) {
        return commonBehavior.getNetwork(block);
    }
}
