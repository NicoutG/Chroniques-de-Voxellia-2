package objects.block.blockBehavior;

import audio.SoundManager;
import audio.SoundType;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorLever extends BlockBehaviorConnected {

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        if (getActivationState(block))
            block.setIndexTexture(1);
        else
            block.setIndexTexture(0);
    }

    @Override
    public void onInteraction(World world, Block block, Vector position, Entity entity) {
        boolean activationState = getActivationState(block);
        world.executeAfterUpdate(() -> block.setState(ACTIVATION_STATE, !activationState));
        int network = getNetwork(block);
        SoundManager.playSound(SoundType.LEVER);
        if (activationState) {
            world.desactivate(network);
            block.setIndexTexture(0);
        }
        else {
            world.activate(network);
            block.setIndexTexture(1);
        }
    }
}
