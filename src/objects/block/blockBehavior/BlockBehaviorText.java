package objects.block.blockBehavior;

import audio.SoundManager;
import audio.SoundType;
import objects.block.*;
import objects.entity.Entity;
import objects.objectBehavior.ObjectBehaviorText;
import tools.Vector;
import world.World;

public class BlockBehaviorText extends BlockBehavior {
    private final ObjectBehaviorText<BlockType, Block, BlockBehavior> commonBehavior = new ObjectBehaviorText<>();

    @Override
    public void onAttachTo(Block block) {
        commonBehavior.onAttachTo(block);
    }

    @Override
    public void onInteraction(World world, Block block, Vector position, Entity entity) {
        commonBehavior.onInteraction(world, block);
        SoundManager.playSound(SoundType.TYPING);
    }

    @Override
    public BlockBehaviorText clone() {
        return new BlockBehaviorText();
    }
}
