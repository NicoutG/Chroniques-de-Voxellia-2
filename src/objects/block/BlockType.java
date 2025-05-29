package objects.block;

import graphics.Texture;
import objects.ObjectType;
import objects.block.blockBehavior.*;
import objects.collision.Collision;
import objects.property.Property;

public class BlockType extends ObjectType<Block, BlockBehavior>{

    public BlockType(String name, Texture[] textures, double opacity, Collision[] collisions, Property[] properties, BlockBehavior[] behaviors) {
        super(name, textures, opacity, collisions, properties, behaviors);
    }

    public BlockType(String name, Texture[] textures, Collision[] collisions, Property[] properties, BlockBehavior[] behaviors) {
        super(name,textures,collisions,properties,behaviors);
    }

    public BlockType(String name, String texturePath, Collision[] collisions, Property[] properties, BlockBehavior[] behaviors) {
        super(name,texturePath,collisions,properties,behaviors);
    }

    public BlockType(String name, Texture[] textures, Property[] properties, BlockBehavior[] behaviors) {
        super(name,textures,properties,behaviors);
    }

    public BlockType(String name, String texturePath, Property[] properties, BlockBehavior[] behaviors) {
        super(name, texturePath, properties, behaviors);
    }

    public BlockType(String name, Texture[] textures, BlockBehavior[] behaviors) {
        super(name, textures, behaviors);
    }

    public BlockType(String name, String texturePath, BlockBehavior[] behaviors) {
        super(name, texturePath, behaviors);
    }

    public BlockType(String name, String texturePath) {
        super(name, texturePath);
    }

    public BlockType(String name) {
        super(name);
    }

    public Block getInstance() {
        Block block = new Block(this);
        for (BlockBehavior blockBehavior : behaviors) {
            BlockBehavior behaviorClone = blockBehavior.clone();
            block.addBehavior(behaviorClone);
        }
        return block;
    }
}
