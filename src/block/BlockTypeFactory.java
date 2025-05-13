package block;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import block.blockProperty.*;
import graphics.Texture;
import graphics.ligth.*;
import graphics.shape.Cube;
import model.world.World;
import graphics.shape.*;

public class BlockTypeFactory {
    private static final String TEXTURE_PATH = "/resources/textures/";
    
    public static ArrayList<BlockType> loadBlockTypes() {
        ArrayList<BlockType> blockTypes = new ArrayList<>();
        blockTypes.add(loadBlueBlock());
        blockTypes.add(loadRedBlock());
        blockTypes.add(loadPurpleBlock());
        blockTypes.add(loadPurpleBlock2());
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
        blockType.setTexture(text);
        return blockType;
    }

    private static BlockType loadRedBlock() {
        BlockType blockType = new BlockType("redBlock");
        BufferedImage img = getImage("red-block.png");
        Shape shape = new Cube();
        Texture text = new Texture(shape, img);
        blockType.setTexture(text);
        return blockType;
    }

    private static BlockType loadPurpleBlock() {
        BlockType blockType = new BlockType("purpleBlock");
        BufferedImage img = getImage("purple-block.png");
        Shape shape = new Cube();
        Texture text = new Texture(shape, img);
        blockType.setTexture(text);
        LightSource light = new LightSource(new ColorRGB(1,0,0), 1, 0.9, 0.1);
        BlockProperty propLight = new BlockPropertyLight(light);
        blockType.addBlockProperty(propLight);
        return blockType;
    }

    private static BlockType loadPurpleBlock2() {
        BlockType blockType = new BlockType("purpleBlock2");
        BufferedImage img = getImage("purple-block.png");
        Shape shape = new Cube();
        Texture text = new Texture(shape, img);
        blockType.setTexture(text);
        LightSource light = new LightSource(new ColorRGB(0,0,1), 1, 0.9, 0);
        BlockProperty propLight = new BlockPropertyLight(light);
        blockType.addBlockProperty(propLight);
        return blockType;
    }
}
