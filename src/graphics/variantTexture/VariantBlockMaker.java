package graphics.variantTexture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.Texture;
import graphics.shape.Shape;
import objects.block.BlockType;
import objects.block.blockBehavior.BlockBehavior;
import objects.collision.Collision;
import objects.property.Property;

public class VariantBlockMaker extends VariantTextureMaker {

    public static BlockType createBlockType(String name, Texture texture, Property[] properties, BlockBehavior[] behaviors, Shape ... variants) {
        Texture[] textures = createTextures(texture, variants);
        Collision[] collisions = getCollisions(textures);
        BlockType blockType = new BlockType(name, textures, collisions, properties, behaviors);
        return blockType;
    }

    public static BlockType createBlockType(String name, Texture texture, Property[] properties, BlockBehavior[] behaviors, Shape[]... variants)
    {
        List<Shape> shapes = new ArrayList<>();
        for (Shape[] arr : variants)
            shapes.addAll(Arrays.asList(arr));

        return createBlockType(name, texture, properties, behaviors, shapes.toArray(new Shape[0]));
    }

    public static BlockType createBlockType(String name, Texture texture, Shape ... variants) {
        return createBlockType(name, texture, null, null, variants);
    }

    public static BlockType createBlockType(String name, Texture texture, Shape[]... variants) {
        return createBlockType(name, texture, null, null, variants);
    }

    public static BlockType createBlockType(String name, String texturePath, Property[] properties, BlockBehavior[] behaviors, Shape ... variants) {
        return createBlockType(name, Texture.createBasicTexture(texturePath), properties, behaviors, variants);
    }

    public static BlockType createBlockType(String name, String texturePath, Property[] properties, BlockBehavior[] behaviors, Shape[]... variants) {
        return createBlockType(name, Texture.createBasicTexture(texturePath), properties, behaviors, variants);
    }

    public static BlockType createBlockType(String name, String texturePath, Shape ... variants) {
        return createBlockType(name, texturePath, null, null, variants);
    }

    public static BlockType createBlockType(String name, String texturePath, Shape[]... variants) {
        return createBlockType(name, texturePath, null, null, variants);
    }
}