package objects.block.blockBehavior;

import audio.SoundManager;
import audio.SoundType;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorLever extends BlockBehaviorConnected {

    @Override
    public void onInteraction(World world, Block block, Vector position, Entity entity) {
        boolean activationState = getActivationState(block);
        world.executeAfterUpdate(() -> block.setState(ACTIVATION_STATE, !activationState));
        int network = getNetwork(block);
        SoundManager.playSoundFromCoordinates(SoundType.LEVER, position.x, position.y, position.z);
        if (activationState)
            world.desactivate(network);
        else
            world.activate(network);
    }
}
