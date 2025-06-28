package objects.block.blockBehavior;

import engine.GamePanel;
import objects.block.Block;
import objects.block.BlockType;
import objects.objectBehavior.ObjectBehaviorEffect;
import tools.Vector;
import world.World;

public class BlockBehaviorEffect extends BlockBehavior {
    private final ObjectBehaviorEffect<BlockType, Block, BlockBehavior> commonBehavior;

    public BlockBehaviorEffect(int effectDuration) {
        commonBehavior = new ObjectBehaviorEffect<>(effectDuration);
    }

    @Override
    public void onAttachTo(Block block) {
        commonBehavior.onAttachTo(block);
    }

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        if (block.getTickFrame0() + commonBehavior.effectDuration <= GamePanel.getTick()) {
            Block[][][] blocks = world.getBlocks();
            blocks[(int)position.x][(int)position.y][(int)position.z] = null;
        }
    }
}
