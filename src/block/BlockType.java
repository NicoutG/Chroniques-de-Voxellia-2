package block;

import java.util.ArrayList;
import java.util.HashMap;

import block.blockBehavior.*;
import block.blockProperty.*;
import graphics.Texture;

public class BlockType {
    private String name;
    private Texture tex;
    private double opacity;
    private HashMap<String, BlockProperty> blockProperties = new HashMap<>();
    private ArrayList<BlockBehavior> blockBehaviors = new ArrayList<>();

    public BlockType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() { 
        return tex;
    }

    public void setTexture(Texture tex) { 
        this.tex = tex;
    }

    public double getOpacity() { 
        return opacity;
    }

    public void setOpacity(double opacity) { 
        this.opacity = opacity;
    }

    public boolean addBlockProperty(BlockProperty blockProperty) {
        if (blockProperties.containsKey(blockProperty.name))
            return false;
        blockProperties.put(blockProperty.name, blockProperty);
        return true;
    }

    public BlockProperty getBlockProperty(String propertyName) {
        return blockProperties.get(propertyName);
    }

    public void addBlockBehavior(BlockBehavior blockBehavior) {
        blockBehaviors.add(blockBehavior);
    }

    public Block createBlock() {
        Block block = new Block(this);
        for (BlockBehavior blockBehavior : blockBehaviors)
            block.addBlockBehavior(blockBehavior.clone());
        return block;
    }
}
