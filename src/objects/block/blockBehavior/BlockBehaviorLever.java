package objects.block.blockBehavior;

import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorLever extends BlockBehaviorConnected {

    private final static String ALLOW_INTERACTION = "allowInteraction";

    @Override
    public void onInteraction(World world, Block block, Vector position, Entity entity) {
        if (getAllowInteraction(block)) {
            boolean activationState = getActivationState(block);
            world.executeAfterUpdate(() -> block.setState(ACTIVATION_STATE, !activationState));
            int network = getNetwork(block);
            if (activationState)
                world.desactivate(network);
            else
                world.activate(network);
        }
    }

    public boolean getAllowInteraction(Block block) {
        Object state = block.getState(ALLOW_INTERACTION);
        if (state != null && state instanceof Boolean)
            return (boolean)state;
        return true;
    }
}
