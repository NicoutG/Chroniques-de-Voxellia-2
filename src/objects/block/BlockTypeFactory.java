package objects.block;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import graphics.Texture;
import graphics.ligth.*;
import graphics.shape.Cube;
import model.world.World;
import objects.property.*;
import graphics.shape.*;

public class BlockTypeFactory {
    private static final String TEXTURE_PATH = "/resources/textures/";
    
    public static ArrayList<BlockType> loadBlockTypes() {
        ArrayList<BlockType> blockTypes = new ArrayList<>();
        blockTypes.add(loadBlueBlock());
        blockTypes.add(loadRedBlock());
        blockTypes.add(loadPurpleBlock());
        blockTypes.add(loadBlockFire());
        return blockTypes;
    }

    private static BufferedImage getImage(String filePath) {
        try {
            return ImageIO.read(World.class.getResource(TEXTURE_PATH + filePath));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static BlockType loadBlueBlock() {
        BlockType blockType = new BlockType("blueBlock");
        BufferedImage img = getImage("blue-block.png");
        Shape shape = new Cube();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        return blockType;
    }

    private static BlockType loadRedBlock() {
        BlockType blockType = new BlockType("redBlock");
        BufferedImage img = getImage("red-block.png");
        Shape shape = new Cube();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        return blockType;
    }

    private static BlockType loadPurpleBlock() {
        BlockType blockType = new BlockType("purpleBlock");
        BufferedImage img = getImage("purple-block.png");
        Shape shape = new Cube();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        return blockType;
    }

    private static BlockType loadBlockFire() {
        BlockType blockType = new BlockType("fire");
        BufferedImage[] frames = new BufferedImage[] {
            getImage("fire-0.png"),
            getImage("fire-1.png"),
            getImage("fire-2.png")
        };
        Shape shape = new Cube();
        Texture text = new Texture(shape, frames, 1);
        blockType.addTexture(text);
        LightSource light = new LightSource(new ColorRGB(1,0.9,0.6), 1.2, 0.8, 0.1);
        Property propLight = new PropertyLight(light);
        blockType.addProperty(propLight);
        return blockType;
    }
}
