package objects.block.blockBehavior;

import audio.SoundType;
import objects.block.*;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorSound extends BlockBehavior {

    private SoundType sound;

    public BlockBehaviorSound(SoundType sound) {
        this.sound = sound;
    }

    @Override
    public void onAttachTo(Block block) {
    }

    @Override
    public void onInteraction(World world, Block block, Vector position, Entity entity) {
        world.playSound(sound);
    }

}
