package objects.block;

import java.util.ArrayList;

import objects.ObjectType;
import objects.block.blockBehavior.*;

public class BlockType extends ObjectType{
    private ArrayList<BlockBehavior> blockBehaviors = new ArrayList<>();

    public BlockType (String name) {
        super(name);
    }

    public void addBlockBehavior(BlockBehavior blockBehavior) {
        blockBehaviors.add(blockBehavior);
    }

    @SuppressWarnings("unchecked")
    public Block getInstance() {
        Block block = new Block(this);
        for (BlockBehavior blockBehavior : blockBehaviors)
            block.addBlockBehavior(blockBehavior.clone());
        return block;
    }
}
