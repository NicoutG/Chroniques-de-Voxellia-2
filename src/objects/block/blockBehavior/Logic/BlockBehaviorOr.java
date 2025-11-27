package objects.block.blockBehavior.logic;

import objects.block.Block;
public class BlockBehaviorOr extends BlockBehaviorActivableManyTo1 {

    @Override
    protected boolean isActivated(Block block) {
        boolean[] activationStates = getActivationStates(block);
        for (boolean activationState : activationStates)
            if (activationState)
                return true;
        return false;
    }
}
