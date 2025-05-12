package model.block.blockBehavior;

import model.block.Block;
import model.entity.Entity;

public abstract class BlockBehavior {

    public abstract void onAttachToBlock(Block block);

    public abstract BlockBehavior clone();

    public void onUpdate(Block block) {
    }

    public void onEntityIn(Block block, Entity entity) {
    }


}
