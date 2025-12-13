package objects.block.blockBehavior;

import objects.block.Block;
import tools.Vector;
import world.World;

public class BlockBehaviorSwitchTexture extends BlockBehaviorConnected {

    @Override
    public void onStart(World world, Block block, Vector position) {
        if ((block.getType() % 2) == 1)
            block.setState(ACTIVATION_STATE, true);
    }

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        if (getActivationState(block)) {
            int index = block.getIndexTexture() - (block.getIndexTexture() % 2) + 1;
            block.setIndexTexture(index);
            block.setIndexCollision(index);
        }
        else {
            int index = block.getIndexTexture() - (block.getIndexTexture() % 2);
            block.setIndexTexture(index);
            block.setIndexCollision(index);
        }
    }
}
