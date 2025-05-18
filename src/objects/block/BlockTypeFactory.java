package objects.block;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import graphics.Texture;
import graphics.ligth.*;
import objects.block.blockBehavior.*;
import objects.collision.*;
import objects.property.*;
import tools.PathManager;
import world.World;
import graphics.shape.*;

public class BlockTypeFactory {

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
        blockTypes.add(loadWorldBlock());
        blockTypes.add(loadNewWorldBlock());
        blockTypes.add(loadRedCarpetBlock());
        // 15
        blockTypes.add(loadRedGoldenCarpetBlock());
        blockTypes.add(loadRedGoldenCarpetBlock2());
        blockTypes.add(loadRedGoldenCarpetBlock3());
        blockTypes.add(loadLeavesBlock());
        blockTypes.add(loadSandBlock());
        // 20
        blockTypes.add(loadWoodBlock());
        blockTypes.add(loadBlock());
        blockTypes.add(loadBlockTray());
        blockTypes.add(loadBlockBrick1());
        blockTypes.add(loadBlockBrick2());
        // 25
        blockTypes.add(loadBlockBroken());
        blockTypes.add(loadBlockColumnMossy());
        blockTypes.add(loadBlockColumn());
        blockTypes.add(loadBlockHalfGrass1());
        blockTypes.add(loadBlockHalfGrass2());
        // 30
        blockTypes.add(loadBlockHalfGrass3());
        blockTypes.add(loadBlockHalfGrass4());
        blockTypes.add(loadBlockHalfHorizontal1());
        blockTypes.add(loadBlockHalfHorizontal2());
        blockTypes.add(loadBlockHalfVertical1());
        // 35
        blockTypes.add(loadBlockHalfVertical2());
        blockTypes.add(loadBlockHalfVertical3());
        blockTypes.add(loadBlockHalfVertical4());
        blockTypes.add(loadBlockHole1());
        blockTypes.add(loadBlockHole2());
        // 40
        blockTypes.add(loadBlockLines());
        blockTypes.add(loadBlockMossy1());
        blockTypes.add(loadBlockMossy2());
        blockTypes.add(loadBlockSlope1());
        blockTypes.add(loadBlockSlope2());
        // 45
        blockTypes.add(loadBlockSlope3());
        blockTypes.add(loadBlockSlope4());
        blockTypes.add(loadBlockStairsLeftMossy());
        blockTypes.add(loadBlockStairsRightMossy());
        blockTypes.add(loadBlockStairsLeft());
        // 50
        blockTypes.add(loadBlockStairsRight());
        blockTypes.add(loadGrassHalfSlab1());
        blockTypes.add(loadGrassHalfSlab2());
        blockTypes.add(loadGrassSlab());
        blockTypes.add(loadGrassSlabs());
        // 55
        blockTypes.add(loadBlockGrassCornerOutLeft());
        blockTypes.add(loadBlockGrassCornerOutRight());
        blockTypes.add(loadBlockGrassCornerOutTop());
        blockTypes.add(loadBlockGrassCornerOutBottom());
        blockTypes.add(loadBlockGrassCornerInLeft());
        // 60
        blockTypes.add(loadBlockGrassCornerInRight());
        blockTypes.add(loadBlockGrassCornerInTop());
        blockTypes.add(loadBlockGrassCornerInBottom());
        blockTypes.add(loadGrassHalfSlab3());
        blockTypes.add(loadGrassHalfSlab4());
        // 65
        blockTypes.add(loadBlockFlower());
        blockTypes.add(loadBlockClubs());
        blockTypes.add(loadBlockAlternate());

        return blockTypes;
    }

    private static BufferedImage getImage(String filePath) {
        try {
            return ImageIO.read(World.class.getResource(PathManager.TEXTURE_PATH + filePath));
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

    private static BlockType createBasicBlockType(String name, String[] textureNames, int ticksPerFrame) {
        BlockType blockType = new BlockType(name);
        BufferedImage[] images = new BufferedImage[textureNames.length];
        for (int i = 0; i < textureNames.length; i++)
            images[i] = getImage(textureNames[i]);
        Shape shape = new Cube();
        Texture text = new Texture(shape, images, ticksPerFrame);
        blockType.addTexture(text);
        return blockType;
    }

    private static BlockType loadBlueBlock() {
        return createBasicBlockType("blueBlock", "blue-block.png");
    }

    private static BlockType loadRedBlock() {
        return createBasicBlockType("redBlock", "red-block.png");
    }

    private static BlockType loadPurpleBlock() {
        return createBasicBlockType("purpleBlock", "purple-block.png");
    }

    private static BlockType loadFireBlock() {
        BlockType blockType = createBasicBlockType("fire", new String[] { "fire-0.png", "fire-1.png", "fire-2.png" },
                1);
        LightSource light = new LightSource(new ColorRGB(1, 0.6, 0.2), 1.2, 0.8, 0.1);
        Property propLight = new PropertyLight(light);
        blockType.addProperty(propLight);
        blockType.addProperty(new Property("noCollision"));
        blockType.setOpacity(0);
        return blockType;
    }

    private static BlockType loadBlueStairsRight() {
        BlockType blockType = new BlockType("blueStairsRight");
        BufferedImage img = getImage("blue-stairs-right.png");
        Shape shape = new StairsRight();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.STEP_RIGHT);
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
        BlockType blockType = createBasicBlockType("glassBlock", "glass-block.png");
        blockType.setOpacity(0.2);
        return blockType;
    }

    private static BlockType loadDirtBlock() {
        return createBasicBlockType("dirtBlock", "dirt.png");
    }

    private static BlockType loadGrassBlock() {
        return createBasicBlockType("grassBlock", "grass.png");
    }

    private static BlockType loadHedgeBlock() {
        return createBasicBlockType("hedge", "hedge.png");
    }

    private static BlockType loadLeverBlock() {
        BlockType blockType = new BlockType("lever");

        BufferedImage img = getImage("lever-F.png");
        Shape shape = new Lever(false);
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);

        img = getImage("lever-T.png");
        shape = new Lever(true);
        text = new Texture(shape, img);
        blockType.addTexture(text);

        blockType.addProperty(new Property("noCollision"));
        blockType.setOpacity(0);
        blockType.addBehavior(new BlockBehaviorLever());
        return blockType;
    }

    private static BlockType loadWorldBlock() {
        BlockType blockType = createBasicBlockType("worldBlock",
                new String[] { "world-block-0.png", "world-block-1.png", "world-block-2.png" }, 3);
        blockType.addBehavior(new BlockBehaviorChangeWorld());
        LightSource light = new LightSource(new ColorRGB(0, 1, 1), 0.7, 0.7, 0);
        Property propLight = new PropertyLight(light);
        blockType.addProperty(propLight);
        return blockType;
    }

    private static BlockType loadNewWorldBlock() {
        BlockType blockType = createBasicBlockType("newWorldBlock",
                new String[] { "world-block-0.png", "world-block-1.png", "world-block-2.png" }, 3);
        blockType.addBehavior(new BlockBehaviorNewWorld());
        LightSource light = new LightSource(new ColorRGB(0, 1, 1), 0.7, 0.7, 0);
        Property propLight = new PropertyLight(light);
        blockType.addProperty(propLight);
        return blockType;
    }

    private static BlockType loadRedCarpetBlock() {
        return createBasicBlockType("redCarpet", "red-carpet-block.png");
    }

    private static BlockType loadRedGoldenCarpetBlock() {
        return createBasicBlockType("redGoldenCarpet", "red-golden-carpet-block.png");
    }

    private static BlockType loadRedGoldenCarpetBlock2() {
        return createBasicBlockType("redGoldenCarpet2", "red-golden-carpet-2-block.png");
    }

    private static BlockType loadRedGoldenCarpetBlock3() {
        return createBasicBlockType("redGoldenCarpet3", "red-golden-carpet-3-block.png");
    }

    private static BlockType loadLeavesBlock() {
        return createBasicBlockType("leaves", "leaves.png");
    }

    private static BlockType loadSandBlock() {
        return createBasicBlockType("sand", "sand-block.png");
    }

    private static BlockType loadWoodBlock() {
        return createBasicBlockType("wood", "wood.png");
    }

    private static BlockType loadBlock() {
        return createBasicBlockType("block", "block.png");
    }

    private static BlockType loadBlockTray() {
        return createBasicBlockType("blockTray", "block-tray.png");
    }

    private static BlockType loadBlockBrick1() {
        return createBasicBlockType("blockBrick1", "block-brick-1.png");
    }

    private static BlockType loadBlockBrick2() {
        return createBasicBlockType("blockBrick2", "block-brick-2.png");
    }

    private static BlockType loadBlockBroken() {
        return createBasicBlockType("blockBroken", "block-broken.png");
    }

    private static BlockType loadBlockColumnMossy() {
        return createBasicBlockType("blockColumnMossy", "block-column-mossy.png");
    }

    private static BlockType loadBlockColumn() {
        return createBasicBlockType("blockColumn", "block-column.png");
    }

    private static BlockType loadBlockHalfGrass1() {
        return createBasicBlockType("blockHalfGrass1", "block-half-grass-1.png");
    }

    private static BlockType loadBlockHalfGrass2() {
        return createBasicBlockType("blockHalfGrass2", "block-half-grass-2.png");
    }

    private static BlockType loadBlockHalfGrass3() {
        return createBasicBlockType("blockHalfGrass3", "block-half-grass-3.png");
    }

    private static BlockType loadBlockHalfGrass4() {
        return createBasicBlockType("blockHalfGrass4", "block-half-grass-4.png");
    }

    private static BlockType loadBlockHalfHorizontal1() {
        BlockType blockType = new BlockType("blockHalfHorizontal1");
        BufferedImage img = getImage("block-half-horizontal-1.png");
        Shape shape = new SlabBottom();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLAB_BOTTOM);
        blockType.setOpacity(0);
        return blockType;
    }

    private static BlockType loadBlockHalfHorizontal2() {
        BlockType blockType = new BlockType("blockHalfHorizontal2");
        BufferedImage img = getImage("block-half-horizontal-2.png");
        Shape shape = new SlabTop();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLAB_TOP);
        blockType.setOpacity(0);
        return blockType;
    }

    private static BlockType loadBlockHalfVertical1() {
        BlockType blockType = new BlockType("blockHalfVertical1");
        BufferedImage img = getImage("block-half-vertical-1.png");
        Shape shape = new SlabVertical1();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLAB_VERTICAL1);
        blockType.setAllowLight(Face.TOP.index, true);
        blockType.setAllowLight(Face.RIGHT.index, true);
        return blockType;
    }

    private static BlockType loadBlockHalfVertical2() {
        BlockType blockType = new BlockType("blockHalfVertical2");
        BufferedImage img = getImage("block-half-vertical-2.png");
        Shape shape = new SlabVertical2();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLAB_VERTICAL2);
        blockType.setAllowLight(Face.TOP.index, true);
        blockType.setAllowLight(Face.LEFT.index, true);
        return blockType;
    }

    private static BlockType loadBlockHalfVertical3() {
        BlockType blockType = new BlockType("blockHalfVertical3");
        BufferedImage img = getImage("block-half-vertical-3.png");
        Shape shape = new SlabVertical3();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLAB_VERTICAL3);
        blockType.setAllowLight(Face.TOP.index, true);
        blockType.setAllowLight(Face.RIGHT.index, true);
        return blockType;
    }

    private static BlockType loadBlockHalfVertical4() {
        BlockType blockType = new BlockType("blockHalfVertical4");
        BufferedImage img = getImage("block-half-vertical-4.png");
        Shape shape = new SlabVertical4();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLAB_VERTICAL4);
        blockType.setAllowLight(Face.TOP.index, true);
        blockType.setAllowLight(Face.LEFT.index, true);
        return blockType;
    }

    private static BlockType loadBlockHole1() {
        return createBasicBlockType("blockHole1", "block-hole-1.png");
    }

    private static BlockType loadBlockHole2() {
        return createBasicBlockType("blockHole1", "block-hole-2.png");
    }

    private static BlockType loadBlockLines() {
        return createBasicBlockType("blockLines", "block-lines.png");
    }

    private static BlockType loadBlockMossy1() {
        return createBasicBlockType("blockMossy1", "block-mossy-1.png");
    }

    private static BlockType loadBlockMossy2() {
        return createBasicBlockType("blockMossy2", "block-mossy-2.png");
    }

    private static BlockType loadBlockSlope1() {
        BlockType blockType = new BlockType("blockSlope1");
        BufferedImage img = getImage("block-slope-1.png");
        Shape shape = new Slope1();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLOPE1);
        blockType.setAllowLight(Face.TOP.index, true);
        blockType.setAllowLight(Face.RIGHT.index, true);
        return blockType;
    }

    private static BlockType loadBlockSlope2() {
        BlockType blockType = new BlockType("blockSlope2");
        BufferedImage img = getImage("block-slope-2.png");
        Shape shape = new Slope2();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLOPE2);
        blockType.setAllowLight(Face.TOP.index, true);
        blockType.setAllowLight(Face.LEFT.index, true);
        return blockType;
    }

    private static BlockType loadBlockSlope3() {
        BlockType blockType = new BlockType("blockSlope3");
        BufferedImage img = getImage("block-slope-3.png");
        Shape shape = new Slope3();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLOPE3);
        blockType.setAllowLight(Face.TOP.index, true);
        blockType.setAllowLight(Face.LEFT.index, true);
        return blockType;
    }

    private static BlockType loadBlockSlope4() {
        BlockType blockType = new BlockType("blockSlope4");
        BufferedImage img = getImage("block-slope-4.png");
        Shape shape = new Slope4();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.SLOPE4);
        blockType.setAllowLight(Face.TOP.index, true);
        blockType.setAllowLight(Face.LEFT.index, true);
        return blockType;
    }

    private static BlockType loadBlockStairsLeftMossy() {
        BlockType blockType = new BlockType("blockStairsLeftMossy");
        BufferedImage img = getImage("block-stairs-left-mossy.png");
        Shape shape = new StairsLeft();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.STEP_LEFT);
        return blockType;
    }

    private static BlockType loadBlockStairsRightMossy() {
        BlockType blockType = new BlockType("blockStairsRightMossy");
        BufferedImage img = getImage("block-stairs-right-mossy.png");
        Shape shape = new StairsRight();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.STEP_RIGHT);
        return blockType;
    }

    private static BlockType loadBlockStairsLeft() {
        BlockType blockType = new BlockType("blockStairsLeft");
        BufferedImage img = getImage("block-stairs-left.png");
        Shape shape = new StairsLeft();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.STEP_LEFT);
        return blockType;
    }

    private static BlockType loadBlockStairsRight() {
        BlockType blockType = new BlockType("blockStairsRight");
        BufferedImage img = getImage("block-stairs-right.png");
        Shape shape = new StairsRight();
        Texture text = new Texture(shape, img);
        blockType.addTexture(text);
        blockType.addCollision(CollisionList.STEP_RIGHT);
        return blockType;
    }

    private static BlockType loadGrassHalfSlab1() {
        return createBasicBlockType("grassHalfSlab1", "grass-half-slab-1.png");
    }

    private static BlockType loadGrassHalfSlab2() {
        return createBasicBlockType("grassHalfSlab2", "grass-half-slab-2.png");
    }

    private static BlockType loadGrassSlab() {
        return createBasicBlockType("grassSlab", "grass-slab.png");
    }

    private static BlockType loadGrassSlabs() {
        return createBasicBlockType("grassSlabs", "grass-slabs.png");
    }
   
    private static BlockType loadBlockGrassCornerOutLeft() {
        return createBasicBlockType("blockGrassCornerOuterLeft", "block-grass-corner-outer-left.png");
    }

    private static BlockType loadBlockGrassCornerOutRight() {
        return createBasicBlockType("blockGrassCornerOuterRight", "block-grass-corner-outer-right.png");
    }

    private static BlockType loadBlockGrassCornerOutTop() {
        return createBasicBlockType("blockGrassCornerOuterTop", "block-grass-corner-outer-top.png");
    }

    private static BlockType loadBlockGrassCornerOutBottom() {
        return createBasicBlockType("blockGrassCornerOuterBottom", "block-grass-corner-outer-bottom.png");
    }

    private static BlockType loadBlockGrassCornerInLeft() {
        return createBasicBlockType("blockGrassCornerInnerLeft", "block-grass-corner-inner-left.png");
    }

    private static BlockType loadBlockGrassCornerInRight() {
        return createBasicBlockType("blockGrassCornerInnerRight", "block-grass-corner-inner-right.png");
    }

    private static BlockType loadBlockGrassCornerInTop() {
        return createBasicBlockType("blockGrassCornerInnerTop", "block-grass-corner-inner-top.png");
    }

    private static BlockType loadBlockGrassCornerInBottom() {
        return createBasicBlockType("blockGrassCornerInnerBottom", "block-grass-corner-inner-bottom.png");
    }
 
    private static BlockType loadGrassHalfSlab3() {
        return createBasicBlockType("GrassHalfSlab3", "grass-half-slab-3.png");
    }
 
    private static BlockType loadGrassHalfSlab4() {
        return createBasicBlockType("GrassHalfSlab4", "grass-half-slab-4.png");
    }
   
    private static BlockType loadBlockFlower() {
        return createBasicBlockType("BlockFlower", "block-flower.png");
    }
    private static BlockType loadBlockClubs() {
        return createBasicBlockType("BlockClubs", "block-clubs.png");
    }
    private static BlockType loadBlockAlternate() {
        return createBasicBlockType("BlockAlternate", "block-alternate.png");
    }

}
