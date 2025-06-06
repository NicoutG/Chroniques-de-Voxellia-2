package objects.block.blockBehavior.logic;

import objects.block.Block;
import objects.block.blockBehavior.BlockBehaviorActivable;
import tools.Vector;
import world.World;

public class BlockBehaviorLoop extends BlockBehaviorActivable {
    private static final String DURATION_ON = "durationOn";
    private static final String DURATION_OFF = "durationOff";
    private static final String NB_LOOP = "nbLoop";
    private int tick = 0;
    private int numLoop = 0;
    private boolean isActivated = false;

    @Override
    protected void desactivate(World world, Block block, Vector position, int network) {
        numLoop = 0;
        isActivated = false;
    }

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(DURATION_ON, 1);
        block.setState(DURATION_OFF, 1);
        block.setState(NB_LOOP, -1);
    }

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        tick++;
        if (getActivationState(block)) {
            int nbLoop = getNbLoop(block);
            if (nbLoop <= 0 || numLoop < nbLoop) {
                if (isActivated) {
                    int durationOn = getDurationOn(block);
                    if (durationOn <= tick) {
                        isActivated = !isActivated;
                        world.activate(getNetwork(block));
                        numLoop++;
                        tick = 0;
                    }
                }
                else {
                    int durationOff = getDurationOff(block);
                    if (durationOff <= tick) {
                        isActivated = !isActivated;
                        world.desactivate(getNetwork(block));
                        tick = 0;
                    }
                }
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

    public int getNbLoop(Block block) {
        Object state = block.getState(NB_LOOP);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
