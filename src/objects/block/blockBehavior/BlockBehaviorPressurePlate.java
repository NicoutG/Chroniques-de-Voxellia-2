package objects.block.blockBehavior;

import objects.collision.CollisionList;
import audio.SoundManager;
import audio.SoundType;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorPressurePlate extends BlockBehaviorConnected {
    public boolean entityOn = true;

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        boolean activationState = getActivationState(block);
        if (activationState && !entityOn) {
            int network = getNetwork(block);
            world.desactivate(network);
            block.setIndexTexture(0);
            SoundManager.playSoundFromCoordinates(SoundType.LEVER, position.x, position.y, position.z);
            world.executeAfterUpdate(() -> block.setState(ACTIVATION_STATE, false));
        }
        entityOn = false;
    }

    @Override
    public void onEntityClose(World world, Block block, Vector position, Entity entity) {
        if (!entityOn)
            if (CollisionList.ON_TOP_BLOCK.collision(position, entity.getCollision(), entity.getPosition())) {
                int network = getNetwork(block);
                boolean activationState = getActivationState(block);
                if (!activationState) {
                    world.activate(network);
                    SoundManager.playSoundFromCoordinates(SoundType.LEVER, position.x, position.y, position.z);
                }
                block.setIndexTexture(1);
                world.executeAfterUpdate(() -> block.setState(ACTIVATION_STATE, true));
                entityOn = true;
            }
    }
}
