package objects.block;

import objects.ObjectType;
import objects.block.blockBehavior.*;

public class BlockType extends ObjectType<Block, BlockBehavior>{

    public BlockType (String name) {
        super(name);
    }

    public Block getInstance() {
        Block block = new Block(this);
        for (BlockBehavior blockBehavior : behaviors)
            block.addBehavior(blockBehavior.clone());
        return block;
    }
}
