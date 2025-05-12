package block.blockBehavior;

import block.Block;
import entity.Entity;

public abstract class BlockBehavior {

    public abstract void onAttachToBlock(Block block);

    public abstract BlockBehavior clone();

    public void onUpdate(Block block) {
    }

    public void onEntityIn(Block block, Entity entity) {
    }


}
