package objects.block;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import graphics.Texture;
import graphics.ligth.*;
import objects.block.blockBehavior.BlockBehaviorLever;
import objects.collision.BoundingCollision;
import objects.collision.ComplexCollision;
import objects.property.*;
import world.World;
import graphics.shape.*;

public class BlockTypeFactory {
    private static final String TEXTURE_PATH = "/resources/textures/outlined/";
    private static final String MASK_PATH = "/resources/masks/";

    public static ArrayList<BlockType> loadBlockTypes() {
        ArrayList<BlockType> blockTypes = new ArrayList<>();
        // 0
        blockTypes.add(loadBlueBlock());
        blockTypes.add(loadRedBlock());
        blockTypes.add(loadPurpleBlock());
        blockTypes.add(loadFireBlock());
        blockTypes.add(loadBlueStairsRight());
        // 5
        blockTypes.add(loadLavaBlock());
        blockTypes.add(loadSunBlock());
        blockTypes.add(loadGlassBlock());
        blockTypes.add(loadDirtBlock());
        blockTypes.add(loadGrassBlock());
        // 10
        blockTypes.add(loadHedgeBlock());
        blockTypes.add(loadLeverBlock());
        return blockTypes;
    }

    private static BufferedImage getImage(String filePath) {
        try {
            return ImageIO.read(World.class.getResource(TEXTURE_PATH + filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BlockType createBasicBlockType(String name, String textureName) {
        BlockType blockType = new BlockType(name);
        BufferedImage img = getImage(textureName);
        Shape shape = new Cube();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        return blockType;
    }

    private static BlockType loadBlueBlock() {
        return createBasicBlockType("blueBlock","blue-block.png");
    }

    private static BlockType loadRedBlock() {
        return createBasicBlockType("redBlock","red-block.png");
    }

    private static BlockType loadPurpleBlock() {
        return createBasicBlockType("purpleBlock","purple-block.png");
    }

    private static BlockType loadFireBlock() {
        BlockType blockType = new BlockType("fire");
        BufferedImage[] frames = new BufferedImage[] {
                getImage("fire-0.png"),
                getImage("fire-1.png"),
                getImage("fire-2.png")
        };
        Shape shape = new Cube();
        Texture text = new Texture(shape, frames, 1);
        blockType.addTexture(text);
        LightSource light = new LightSource(new ColorRGB(1, 0.6, 0.2), 1.2, 0.8, 0.1);
        Property propLight = new PropertyLight(light);
        blockType.addProperty(propLight);
        blockType.addProperty(new Property("noCollision"));
        return blockType;
    }

    private static BlockType loadBlueStairsRight() {
        BlockType blockType = new BlockType("blueStairsRight");
        BufferedImage img = getImage("blue-stairs-right.png");
        Shape shape = new StairsRight();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        ComplexCollision collision = new ComplexCollision();
        collision.addCollision(new BoundingCollision(-0.5,0.5,-0.5,0.5,-0.5,-0.17));
        collision.addCollision(new BoundingCollision(-0.5,0.5,-0.5,0.17,-0.17,0.17));
        collision.addCollision(new BoundingCollision(-0.5,0.5,-0.5,-0.17,0.17,0.5));
        blockType.addCollision(collision);
        return blockType;
    }

    private static BlockType loadLavaBlock() {
        BlockType blockType = new BlockType("lava");
        Shape shape = new Rectangle();
        Texture text = new Texture(shape, getImage("lava.png"));
        blockType.addTexture(text);
        LightSource light = new LightSource(new ColorRGB(1, 0.6, 0.4), 1.5, 0.5, 0.05);
        Property propLight = new PropertyLight(light);
        blockType.setOpacity(0);
        blockType.addProperty(propLight);
        return blockType;
    }

    private static BlockType loadSunBlock() {
        BlockType blockType = new BlockType("sun");
        ColorRGB[] pulse = {
            new ColorRGB(1, 1, 1), // plain day (white)
            new ColorRGB(1, 1, 1), // (white)
            new ColorRGB(1, 1, 1),
            new ColorRGB(1, 1, 1), 
            new ColorRGB(1, 0.9, 0.75), 
            new ColorRGB(1, 0.5, 0.3), // evening (orange-pink)
            new ColorRGB(0.6, 0.3, 0.35), 
            new ColorRGB(0.2, 0.2, 0.4), // night (low blue-white)
            new ColorRGB(0.2, 0.2, 0.4), 
            new ColorRGB(0.2, 0.2, 0.4), 
            new ColorRGB(0.2, 0.2, 0.4), 
            new ColorRGB(0.6, 0.3, 0.35), 
            new ColorRGB(0.8, 0.3, 0.3), // morning (reddish)
            new ColorRGB(1, 0.9, 0.75), 
        };
        LightSource light = new LightSource(pulse, 1200, 0.75, 1, 0);
        Property propLight = new PropertyLight(light);
        blockType.addProperty(propLight);
        return blockType;
    }

      private static BlockType loadGlassBlock() {
        BlockType blockType = createBasicBlockType("glassBlock","glass-block.png");
        blockType.setOpacity(0.2);
        return blockType;
    }

    private static BlockType loadDirtBlock() {
        return createBasicBlockType("dirtBlock","dirt-block.png");
    }
   
    private static BlockType loadGrassBlock() {
        return createBasicBlockType("grassBlock","grass-block.png");
    }
   
    private static BlockType loadHedgeBlock() {
        return createBasicBlockType("hedge","hedge.png");
    }

    private static BlockType loadLeverBlock() {
        BlockType blockType = new BlockType("lever");
        try {
            BufferedImage img = getImage("lever-F.png");
            String maskPath = MASK_PATH + "lever/false/";
            Shape shape = new CustomShape(maskPath + "lever-F-left.png", maskPath + "lever-F-right.png", maskPath + "lever-F-top.png");
            Texture text = new Texture(shape, img);
            blockType.addTexture(text);

            img = getImage("lever-T.png");
            maskPath = MASK_PATH + "lever/true/";
            shape = new CustomShape(maskPath + "lever-T-left.png", maskPath + "lever-T-right.png", maskPath + "lever-T-top.png");
            text = new Texture(shape, img);
            blockType.addTexture(text);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        blockType.addProperty(new Property("noCollision"));
        blockType.setOpacity(0);
        blockType.addBehavior(new BlockBehaviorLever());
        return blockType;
    }
}
