package objects.block.blockBehavior.Logic;

import objects.block.Block;
import objects.block.blockBehavior.BlockBehaviorConnected;
import tools.Vector;
import world.World;

public class BlockBehaviorAlternate extends BlockBehaviorConnected {
    private static final String DURATION_ON = "durationOn";
    private static final String DURATION_OFF = "durationOff";
    private int tick = 0;

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(DURATION_ON, 1);
        block.setState(DURATION_OFF, 1);
    }

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        tick++;
        boolean activationState = getActivationState(block);
        if (activationState) {
            int durationOn = getDurationOn(block);
            if (durationOn <= tick) {
                block.setState(ACTIVATION_STATE, false);
                world.activate(getNetwork(block));
                tick = 0;
            }
        }
        else {
            int durationOff = getDurationOff(block);
            if (durationOff <= tick) {
                block.setState(ACTIVATION_STATE, true);
                world.desactivate(getNetwork(block));
                tick = 0;
            }
        }
    }

    public int getDurationOn(Block block) {
        Object state = block.getState(DURATION_ON);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }

    public int getDurationOff(Block block) {
        Object state = block.getState(DURATION_OFF);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
