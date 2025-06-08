package objects.block;

import audio.SoundType;
import graphics.Texture;
import graphics.ligth.*;
import graphics.shape.*;
import objects.block.blockBehavior.*;
import objects.block.blockBehavior.logic.*;
import objects.collision.*;
import objects.property.*;
import tools.PathManager;

public enum BlockTemplate {
        /* 0 */
        BLUE_BLOCK(new BlockType("blueBlock", "blue-block.png")),
        RED_BLOCK(new BlockType("redBlock", "red-block.png")),
        PURPLE_BLOCK(new BlockType("purpleBlock", "purple-block.png")),
        FIRE_CAMP(new BlockType("fireCamp",
                        new Texture[] {
                                        Texture.createBasicTexture(
                                                        new String[] { "fire-0.png", "fire-1.png", "fire-2.png" }, 1) },
                        0,
                        null,
                        new Property[] { new PropertyLight(
                                        new LightSource(new ColorRGB(1, 0.6, 0.2), 1.2, 0.8, 0.1)),
                                        new Property("noCollision"),
                                        new PropertySound(SoundType.FIRE) },
                        null)),
        BLUE_STAIRS_RIGHT(new BlockType("blueStairsRight",
                        new Texture[] { new Texture(ShapeList.STAIR2, PathManager.loadImage("blue-stairs-right.png")) },
                        new Collision[] { CollisionList.STAIR2 },
                        null,
                        null)),
        /* 5 */
        LAVA(new BlockType("lava",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.RECTANGLE1,
                                                        new String[] { "lava/lava-1-0.png", "lava/lava-1-1.png",
                                                                        "lava/lava-1-2.png",
                                                                        "lava/lava-1-3.png" },
                                                        4),
                                        Texture.createBasicTexture(ShapeList.RECTANGLE2,
                                                        new String[] { "lava/lava-2-0.png", "lava/lava-2-1.png",
                                                                        "lava/lava-2-2.png",
                                                                        "lava/lava-2-3.png" },
                                                        4),
                                        Texture.createBasicTexture(ShapeList.RECTANGLE3,
                                                        new String[] { "lava/lava-3-0.png", "lava/lava-3-1.png",
                                                                        "lava/lava-3-2.png",
                                                                        "lava/lava-3-3.png" },
                                                        4),
                                        Texture.createBasicTexture(
                                                        new String[] { "lava/lava-4-0.png", "lava/lava-4-1.png",
                                                                        "lava/lava-4-2.png", "lava/lava-4-3.png" },
                                                        4) },
                        0,
                        new Collision[] { CollisionList.RECTANGLE1, CollisionList.RECTANGLE2, CollisionList.RECTANGLE3,
                                        CollisionList.CUBE },
                        new Property[] {
                                        new Property("noCollision"),
                                        new PropertySound(SoundType.LAVA),
                                        new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.4), 1.5, 0.5, 0.15)) },
                        new BlockBehavior[] { new BlockBehaviorLiquid(4, 6),
                                        new BlockBehaviorKill(),
                                        new BlockBehaviorApplyForce(0, 0, 0.5) })),
        SUN(new BlockType("sun",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision"),
                                        new PropertyLight(new LightSource(new ColorRGB[] {
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
                                        }, 1200, 0.75, 1, 0)) },
                        null)),
        GLASS(new BlockType("glassBlock", "glass-block.png") {
                {
                        setOpacity(0.2);
                }
        }),
        DIRT(new BlockType("dirtBlock", "dirt.png")),
        GRASS(new BlockType("grassBlock", "grass.png")),
        /* 10 */
        HEDGE(new BlockType("hedge", "hedge.png")),
        LEVER(new BlockType("lever",
                        new Texture[] { new Texture(ShapeList.LEVER_OFF, PathManager.loadImage("lever/lever-F.png")),
                                        new Texture(ShapeList.LEVER_ON, PathManager.loadImage("lever/lever-T.png")) },
                        0,
                        null,
                        new Property[] { new Property("noCollision") },
                        new BlockBehavior[] {
                                        new BlockBehaviorLever()
                        })),
        WORLD(new BlockType("worldBlock",
                        new Texture[] {
                                        new Texture(ShapeList.CUBE,
                                                        PathManager.loadImage("world-loader/world-loader-0.png")),
                                        Texture.createBasicTexture(
                                                        new String[] { "world-loader/world-loader-1-0.png",
                                                                        "world-loader/world-loader-1-1.png",
                                                                        "world-loader/world-loader-1-2.png" },
                                                        3)

                        },
                        new Property[] { new PropertyLight(new LightSource(new ColorRGB(0, 1, 1), 0.7, 0.7, 0)) },
                        new BlockBehavior[] { new BlockBehaviorChangeWorld(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME)
                        })),
        NEW_WORLD(new BlockType("newWorldBlock",
                        new Texture[] {
                                        new Texture(ShapeList.CUBE,
                                                        PathManager.loadImage("world-loader/world-loader-0.png")),
                                        Texture.createBasicTexture(
                                                        new String[] { "world-loader/world-loader-1-0.png",
                                                                        "world-loader/world-loader-1-1.png",
                                                                        "world-loader/world-loader-1-2.png" },
                                                        3)

                        },
                        new Property[] { new PropertyLight(new LightSource(new ColorRGB(0, 1, 1), 0.7, 0.7, 0)) },
                        new BlockBehavior[] { new BlockBehaviorNewWorld(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME)
                        })),
        RED_CARPET(new BlockType("redCarpet", "red-carpet-block.png")),
        /* 15 */
        RED_GOLDEN_CARPET(new BlockType("redGoldenCarpet", "red-golden-carpet-block.png")),
        RED_GOLDEN_CARPET2(new BlockType("redGoldenCarpet2", "red-golden-carpet-2-block.png")),
        RED_GOLDEN_CARPET3(new BlockType("redGoldenCarpet3", "red-golden-carpet-3-block.png")),
        LEAVES(new BlockType("leaves", "leaves.png")),
        SAND(new BlockType("sand", "sand-block.png")),
        /* 20 */
        WOOD(new BlockType("wood", "wood.png")),
        BLOCK(new BlockType("block", "block.png")),
        TRAY(new BlockType("blockTray", "block-tray.png")),
        BRICK1(new BlockType("blockBrick1", "block-brick-1.png")),
        BRICK2(new BlockType("blockBrick2", "block-brick-2.png")),
        /* 25 */
        BROKEN(new BlockType("blockBroken", "block-broken.png")),
        COLUMN_MOSSY(new BlockType("blockColumnMossy", "block-column-mossy.png")),
        COLUMN(new BlockType("blockColumn", "block-column.png")),
        HALF_GRASS1(new BlockType("blockHalfGrass1", "block-half-grass-1.png")),
        HALF_GRASS2(new BlockType("blockHalfGrass2", "block-half-grass-2.png")),
        /* 30 */
        HALF_GRASS3(new BlockType("blockHalfGrass3", "block-half-grass-3.png")),
        HALF_GRASS4(new BlockType("blockHalfGrass4", "block-half-grass-4.png")),
        HALF_HORIZONTAL1(new BlockType("blockHalfHorizontal1",
                        new Texture[] { new Texture(ShapeList.SLAB_BOTTOM,
                                        PathManager.loadImage("block-half-horizontal-1.png")) },
                        0,
                        new Collision[] { CollisionList.SLAB_BOTTOM },
                        null,
                        null)),
        HALF_HORIZONTAL2(new BlockType("blockHalfHorizontal2",
                        new Texture[] { new Texture(ShapeList.SLAB_TOP,
                                        PathManager.loadImage("block-half-horizontal-2.png")) },
                        0,
                        new Collision[] { CollisionList.SLAB_TOP },
                        null,
                        null)),
        HALF_VERTICAL1(new BlockType("blockHalfVertical1",
                        new Texture[] { new Texture(ShapeList.SLAB_VERTICAL1,
                                        PathManager.loadImage("block-half-vertical-1.png")) },
                        new Collision[] { CollisionList.SLAB_VERTICAL1 },
                        null,
                        null) {
                {
                        setAllowLight(Face.TOP.index, true);
                        setAllowLight(Face.RIGHT.index, true);
                }
        }),
        /* 35 */
        HALF_VERTICAL2(new BlockType("blockHalfVertical2",
                        new Texture[] { new Texture(ShapeList.SLAB_VERTICAL2,
                                        PathManager.loadImage("block-half-vertical-2.png")) },
                        new Collision[] { CollisionList.SLAB_VERTICAL2 },
                        null,
                        null) {
                {
                        setAllowLight(Face.TOP.index, true);
                        setAllowLight(Face.LEFT.index, true);
                }
        }),
        HALF_VERTICAL3(new BlockType("blockHalfVertical3",
                        new Texture[] { new Texture(ShapeList.SLAB_VERTICAL3,
                                        PathManager.loadImage("block-half-vertical-3.png")) },
                        new Collision[] { CollisionList.SLAB_VERTICAL3 },
                        null,
                        null) {
                {
                        setAllowLight(Face.TOP.index, true);
                        setAllowLight(Face.RIGHT.index, true);
                }
        }),
        HALF_VERTICAL4(new BlockType("blockHalfVertical4",
                        new Texture[] { new Texture(ShapeList.SLAB_VERTICAL4,
                                        PathManager.loadImage("block-half-vertical-4.png")) },
                        new Collision[] { CollisionList.SLAB_VERTICAL4 },
                        null,
                        null) {
                {
                        setAllowLight(Face.TOP.index, true);
                        setAllowLight(Face.LEFT.index, true);
                }
        }),
        HOLE1(new BlockType("blockHole1", "block-hole-1.png")),
        HOLE2(new BlockType("blockHole2", "block-hole-2.png")),
        /* 40 */
        LINES(new BlockType("blockLines", "block-lines.png")),
        MOSSY1(new BlockType("blockMossy1", "block-mossy-1.png")),
        MOSSY2(new BlockType("blockMossy2", "block-mossy-2.png")),
        SLOPE1(new BlockType("blockSlope1",
                        new Texture[] { new Texture(ShapeList.SLOPE1, PathManager.loadImage("block-slope-1.png")) },
                        new Collision[] { CollisionList.SLOPE1 },
                        null,
                        null) {
                {
                        setAllowLight(Face.TOP.index, true);
                        setAllowLight(Face.RIGHT.index, true);
                }
        }),
        SLOPE2(new BlockType("blockSlope2",
                        new Texture[] { new Texture(ShapeList.SLOPE2, PathManager.loadImage("block-slope-2.png")) },
                        new Collision[] { CollisionList.SLOPE2 },
                        null,
                        null) {
                {
                        setAllowLight(Face.TOP.index, true);
                        setAllowLight(Face.RIGHT.index, true);
                }
        }),
        /* 45 */
        SLOPE3(new BlockType("blockSlope3",
                        new Texture[] { new Texture(ShapeList.SLOPE3, PathManager.loadImage("block-slope-3.png")) },
                        new Collision[] { CollisionList.SLOPE3 },
                        null,
                        null) {
                {
                        setAllowLight(Face.TOP.index, true);
                        setAllowLight(Face.LEFT.index, true);
                }
        }),
        SLOPE4(new BlockType("blockSlope4",
                        new Texture[] { new Texture(ShapeList.SLOPE4, PathManager.loadImage("block-slope-4.png")) },
                        new Collision[] { CollisionList.SLOPE4 },
                        null,
                        null) {
                {
                        setAllowLight(Face.TOP.index, true);
                        setAllowLight(Face.LEFT.index, true);
                }
        }),
        STAIRS_LEFT_MOSSY(new BlockType("blockStairsLeftMossy",
                        new Texture[] { new Texture(ShapeList.STAIR1,
                                        PathManager.loadImage("block-stairs-left-mossy.png")) },
                        new Collision[] { CollisionList.STAIR1 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_RIGHT_MOSSY(new BlockType("blockStairsRightMossy",
                        new Texture[] { new Texture(ShapeList.STAIR2,
                                        PathManager.loadImage("block-stairs-right-mossy.png")) },
                        new Collision[] { CollisionList.STAIR2 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_LEFT(new BlockType("blockStairsLeft",
                        new Texture[] { new Texture(ShapeList.STAIR1,
                                        PathManager.loadImage("blockStairs/block-stairs-left.png")) },
                        new Collision[] { CollisionList.STAIR1 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        /* 50 */
        STAIRS_RIGHT(new BlockType("blockStairsRight",
                        new Texture[] { new Texture(ShapeList.STAIR2,
                                        PathManager.loadImage("blockStairs/block-stairs-right.png")) },
                        new Collision[] { CollisionList.STAIR2 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        GRASS_HALF_SLAB_1(new BlockType("grassHalfSlab1", "grass-half-slab-1.png")),
        GRASS_HALF_SLAB_2(new BlockType("grassHalfSlab2", "grass-half-slab-2.png")),
        GRASS_SLAB(new BlockType("grassSlab", "grass-slab.png")),
        GRASS_SLABS(new BlockType("grassSlabs", "grass-slabs.png")),
        /* 55 */
        BLOCK_GRASS_CORNER_OUT_LEFT(new BlockType("blockGrassCornerOuterLeft", "block-grass-corner-outer-left.png")),
        BLOCK_GRASS_CORNER_OUT_RIGHT(new BlockType("blockGrassCornerOuterRight", "block-grass-corner-outer-right.png")),
        BLOCK_GRASS_CORNER_OUT_TOP(new BlockType("blockGrassCornerOuterTop", "block-grass-corner-outer-top.png")),
        BLOCK_GRASS_CORNER_OUT_BOTTOM(
                        new BlockType("blockGrassCornerOuterBottom", "block-grass-corner-outer-bottom.png")),
        BLOCK_GRASS_CORNER_IN_LEFT(new BlockType("blockGrassCornerInnerLeft", "block-grass-corner-inner-left.png")),
        /* 60 */
        BLOCK_GRASS_CORNER_IN_RIGHT(new BlockType("blockGrassCornerInnerRight", "block-grass-corner-inner-right.png")),
        BLOCK_GRASS_CORNER_IN_TOP(new BlockType("blockGrassCornerInnerTop", "block-grass-corner-inner-top.png")),
        BLOCK_GRASS_CORNER_IN_BOTTOM(
                        new BlockType("blockGrassCornerInnerBottom", "block-grass-corner-inner-bottom.png")),
        GRASS_HALF_SLAB_3(new BlockType("GrassHalfSlab3", "grass-half-slab-3.png")),
        GRASS_HALF_SLAB_4(new BlockType("GrassHalfSlab4", "grass-half-slab-4.png")),
        /* 65 */
        BLOCK_FLOWER(new BlockType("blockFlower", "block-flower.png")),
        BLOCK_CLUBS(new BlockType("blockClubs", "block-clubs.png")),
        BLOCK_ALTERNATE(new BlockType("blockAlternate", "block-alternate.png")),
        TELEPORTER(new BlockType("teleporter",
                        new Texture[] { new Texture(ShapeList.CUBE,
                                        PathManager.loadImage("teleporter/teleporter-F.png")),
                                        Texture.createBasicTexture(
                                                        new String[] { "teleporter/teleporter-0-T.png",
                                                                        "teleporter/teleporter-1-T.png",
                                                                        "teleporter/teleporter-2-T.png",
                                                                        "teleporter/teleporter-3-T.png" },
                                                        3) },
                        null,
                        new Property[] { new PropertyLight(
                                        new LightSource(new ColorRGB(0.8, 0.2, 0.7), 0.35, 0.5, 0.05)) },
                        new BlockBehavior[] { new BlockBehaviorTeleportation(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME) })),
        BLACK_BLOCK(new BlockType("blackBlock", "black-block.png")),
        /* 70 */
        BLUE_BLOCK_BROKEN(new BlockType("blueBlockBroken", "blue-block-broken.png")),
        BLUE_BLOCK_CLUBS(new BlockType("blueBlockClubs", "blue-block-clubs.png")),
        BLUE_BLOCK_COLUMN(new BlockType("blueBlockColumn", "blue-block-column.png")),
        BLUE_BLOCK_RAYS(new BlockType("blueBlockRays", "blue-block-rays.png")),
        BOOK_SHELVE_1(new BlockType("bookShelve1", "book-shelve-1.png")),
        /* 75 */
        BOOK_SHELVE_2(new BlockType("bookShelve2", "book-shelve-2.png")),
        BROWN_BLOCK(new BlockType("brownBlock", "brown-block.png")),
        DISTORTED_BLOCK_BLUE(new BlockType("distortedBlockBlue", "distorted-block-blue.png")),
        DISTORTED_BLOCK_GREEN(new BlockType("distortedBlockGreen", "distorted-block-green.png")),
        DISTORTED_BLOCK_ORANGE(new BlockType("distortedBlockOrange", "distorted-block-orange.png")),
        /* 80 */
        DISTORTED_BLOCK_PURPLE(new BlockType("distortedBlockPurple", "distorted-block-purple.png")),
        DISTORTED_BLOCK_RED(new BlockType("distortedBlockRed", "distorted-block-red.png")),
        DISTORTED_BLOCK_YELLOW(new BlockType("distortedBlockYellow", "distorted-block-yellow.png")),
        GRAVEL_DARK(new BlockType("gravelDark", "gravel-dark.png")),
        GRAVEL(new BlockType("gravel", "gravel.png")),
        /* 85 */
        GREEN_BLOCK(new BlockType("greenBlock", "green-block.png")),
        SHELVE(new BlockType("shelve", "shelve.png") {
                {
                        setOpacity(0);
                }
        }),
        STONE_BROKEN(new BlockType("stoneBroken", "stone-broken.png")),
        STONE_FRAME(new BlockType("stoneFrame", "stone-frame.png")),
        STONE_MOSSY(new BlockType("stoneMossy", "stone-mossy.png")),
        /* 90 */
        STONE_POLISHED(new BlockType("stonePolished", "stone-polished.png")),
        STONE(new BlockType("stone", "stone.png")),
        WHITE_BLOCK(new BlockType("whiteBlock", "white-block.png")),
        WOOD_ACACIA(new BlockType("woodAcacia", "wood-acacia.png")),
        WOOD_OAK(new BlockType("woodOak", "wood-oak.png")),
        /* 95 */
        WOOD_PINE(new BlockType("woodPine", "wood-pine.png")),
        ICE(new BlockType("ice", "ice.png") {
                {
                        setOpacity(0.5);
                }
        }),
        SNOW(new BlockType("snow", "snow.png")),
        PRESSURE_PLATE(new BlockType("pressurePlate",
                        new Texture[] { Texture.createBasicTexture("pressure-plate-F.png"),
                                        Texture.createBasicTexture("pressure-plate-T.png") },
                        null,
                        new Property[] { new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.4), 0.2, 0.5, 0)) },
                        new BlockBehavior[] { new BlockBehaviorPressurePlate(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME) })),
        STAINED_GLASS_RED_LEFT(new BlockType("stainedGlassRedLeft",
                        new Texture[] {
                                        new Texture(ShapeList.BORDER_LEFT,
                                                        PathManager.loadImage(
                                                                        "stained-glass/stained-glass-red-left.png")) },
                        new Collision[] { CollisionList.CUBE },
                        null,
                        null) {
                {
                        setOpacity(0.1);
                        setColor(new ColorRGB(1, 0, 0));
                }
        }),
        /* 100 */
        STAINED_GLASS_RED_RIGHT(new BlockType("stainedGlassRedRight",
                        new Texture[] {
                                        new Texture(ShapeList.BORDER_RIGHT,
                                                        PathManager.loadImage(
                                                                        "stained-glass/stained-glass-red-right.png")) },
                        new Collision[] { CollisionList.CUBE },
                        null,
                        null) {
                {
                        setOpacity(0.1);
                        setColor(new ColorRGB(1, 0, 0));
                }
        }),
        STAINED_GLASS_BLUE_LEFT(new BlockType("stainedGlassBlueLeft",
                        new Texture[] {
                                        new Texture(ShapeList.BORDER_LEFT,
                                                        PathManager.loadImage(
                                                                        "stained-glass/stained-glass-blue-left.png")) },
                        new Collision[] { CollisionList.CUBE },
                        null,
                        null) {
                {
                        setOpacity(0.1);
                        setColor(new ColorRGB(0, 0, 1));
                }
        }),
        STAINED_GLASS_BLUE_RIGHT(new BlockType("stainedGlassBlueRight",
                        new Texture[] {
                                        new Texture(ShapeList.BORDER_RIGHT,
                                                        PathManager.loadImage(
                                                                        "stained-glass/stained-glass-blue-right.png")) },
                        new Collision[] { CollisionList.CUBE },
                        null,
                        null) {
                {
                        setOpacity(0.1);
                        setColor(new ColorRGB(0, 0, 1));
                }
        }),
        STAINED_GLASS_GREEN_LEFT(new BlockType("stainedGlassGreenLeft",
                        new Texture[] {
                                        new Texture(ShapeList.BORDER_LEFT,
                                                        PathManager.loadImage(
                                                                        "stained-glass/stained-glass-green-left.png")) },
                        new Collision[] { CollisionList.CUBE },
                        null,
                        null) {
                {
                        setOpacity(0.1);
                        setColor(new ColorRGB(0, 1, 0));
                }
        }),
        STAINED_GLASS_GREEN_RIGHT(new BlockType("stainedGlassGreenRight",
                        new Texture[] {
                                        new Texture(ShapeList.BORDER_RIGHT,
                                                        PathManager.loadImage(
                                                                        "stained-glass/stained-glass-green-right.png")) },
                        new Collision[] { CollisionList.CUBE },
                        null,
                        null) {
                {
                        setOpacity(0.1);
                        setColor(new ColorRGB(0, 1, 0));
                }
        }),
        /* 105 */
        SUNLIGHT(new BlockType("sunlight",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision"),
                                        new PropertyLight(new LightSource(new ColorRGB[] {
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
                                        }, 1200, 1, 0.9, 0)) },
                        null)),
        LINKED_LEVER(new BlockType("linkedLever",
                        new Texture[] { new Texture(ShapeList.LEVER_OFF,
                                        PathManager.loadImage("linkedLever/linked-lever-F.png")),
                                        new Texture(ShapeList.LEVER_ON,
                                                        PathManager.loadImage("linkedLever/linked-lever-T.png")) },
                        0,
                        null,
                        new Property[] { new Property("noCollision") },
                        new BlockBehavior[] { new BlockBehaviorLever(), new BlockBehaviorActivable() })),
        WATER(new BlockType("water",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.RECTANGLE1,
                                                        new String[] { "water/water-1-0.png", "water/water-1-1.png",
                                                                        "water/water-1-2.png" },
                                                        4),
                                        Texture.createBasicTexture(ShapeList.RECTANGLE2,
                                                        new String[] { "water/water-2-0.png", "water/water-2-1.png",
                                                                        "water/water-2-2.png" },
                                                        4),
                                        Texture.createBasicTexture(ShapeList.RECTANGLE3,
                                                        new String[] { "water/water-3-0.png", "water/water-3-1.png",
                                                                        "water/water-3-2.png" },
                                                        4),
                                        Texture.createBasicTexture(
                                                        new String[] { "water/water-4-0.png", "water/water-4-1.png",
                                                                        "water/water-4-2.png" },
                                                        4) },
                        0,
                        new Collision[] { CollisionList.RECTANGLE1, CollisionList.RECTANGLE2, CollisionList.RECTANGLE3,
                                        CollisionList.CUBE },
                        new Property[] {
                                        new Property("noCollision"),
                                        new PropertySound(SoundType.WATER) },
                        new BlockBehavior[] { new BlockBehaviorLiquid(4, 2),
                                        new BlockBehaviorApplyForce(0, 0, 0.5) })),
        AND(new BlockType("and",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision") },
                        new BlockBehavior[] { new BlockBehaviorAnd() })),
        OR(new BlockType("or",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision") },
                        new BlockBehavior[] { new BlockBehaviorOr() })),
        /* 110 */
        NOT(new BlockType("not",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision") },
                        new BlockBehavior[] { new BlockBehaviorNot() })),
        DELAY(new BlockType("delay",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision") },
                        new BlockBehavior[] { new BlockBehaviorDelay() })),
        LOOP(new BlockType("loop",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision") },
                        new BlockBehavior[] { new BlockBehaviorLoop() })),
        AMBIENT1(new BlockType("ambient1",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision"),
                                        new PropertySound(SoundType.AMBIENT1) },
                        null)),
        STAIRS3(new BlockType("blockStairs3",
                        new Texture[] { new Texture(ShapeList.STAIR3,
                                        PathManager.loadImage("blockStairs/block-stairs3.png")) },
                        new Collision[] { CollisionList.STAIR3 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        /* 115 */
        STAIRS4(new BlockType("blockStairs4",
                        new Texture[] { new Texture(ShapeList.STAIR4,
                                        PathManager.loadImage("blockStairs/block-stairs4.png")) },
                        new Collision[] { CollisionList.STAIR4 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_ANGLE1(new BlockType("blockStairsAngle1",
                        new Texture[] { new Texture(ShapeList.STAIR_ANGLE1,
                                        PathManager.loadImage("blockStairs/block-stairs-angle1.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE1 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_ANGLE2(new BlockType("blockStairsAngle2",
                        new Texture[] { new Texture(ShapeList.STAIR_ANGLE2,
                                        PathManager.loadImage("blockStairs/block-stairs-angle2.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE2 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_ANGLE3(new BlockType("blockStairsAngle3",
                        new Texture[] { new Texture(ShapeList.STAIR_ANGLE3,
                                        PathManager.loadImage("blockStairs/block-stairs-angle3.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE3 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_ANGLE4(new BlockType("blockStairsAngle4",
                        new Texture[] { new Texture(ShapeList.STAIR_ANGLE4,
                                        PathManager.loadImage("blockStairs/block-stairs-angle4.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE4 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        /* 120 */
        STAIRS_ANGLE5(new BlockType("blockStairsAngle5",
                        new Texture[] { new Texture(ShapeList.STAIR_ANGLE5,
                                        PathManager.loadImage("blockStairs/block-stairs-angle5.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE5 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_ANGLE6(new BlockType("blockStairsAngle6",
                        new Texture[] { new Texture(ShapeList.STAIR_ANGLE6,
                                        PathManager.loadImage("blockStairs/block-stairs-angle6.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE6 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_ANGLE7(new BlockType("blockStairsAngle7",
                        new Texture[] { new Texture(ShapeList.STAIR_ANGLE7,
                                        PathManager.loadImage("blockStairs/block-stairs-angle7.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE7 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),
        STAIRS_ANGLE8(new BlockType("blockStairsAngle8",
                        new Texture[] { new Texture(ShapeList.STAIR_ANGLE8,
                                        PathManager.loadImage("blockStairs/block-stairs-angle8.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE8 },
                        null,
                        null) {
                {
                        setOpacity(0);
                }
        }),

        SIGN_LEFT(new BlockType("SignLeft",
                        new Texture[] { new Texture(ShapeList.SIGN_LEFT, PathManager.loadImage("sign-left.png")) },
                        null,
                        new BlockBehavior[] { new BlockBehaviorText() }) {
                {
                        setOpacity(0);
                }
        }),
        /* 125 */
        SIGN_RIGHT(new BlockType("SignRight",
                        new Texture[] { new Texture(ShapeList.SIGN_RIGHT, PathManager.loadImage("sign-right.png")) },
                        null,
                        new BlockBehavior[] { new BlockBehaviorText() }) {
                {
                        setOpacity(0);
                }
        }),
        AMBIENT2(new BlockType("ambient2",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision"),
                                        new PropertySound(SoundType.AMBIENT2) },
                        null)),
        BACKGROUND_NATURE(new BlockType("backgroundNature",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property("noCollision"),
                                        new PropertySound(SoundType.BACKGROUND_NATURE) },
                        null)),
        FLOWERS(new BlockType("flowers",
                        new Texture[] { new Texture(ShapeList.FLOWERS, PathManager.loadImage("herbs/flowers.png")) },
                        null,
                        new Property[] { new Property("noCollision"), new Property("floor") },
                        null) {
                {
                        setOpacity(0);
                }
        }),
        GRASS_BIG(new BlockType("grassBig",
                        new Texture[] { new Texture(ShapeList.GRASS_BIG,
                                        PathManager.loadImage("herbs/grass-big.png")) },
                        null,
                        new Property[] { new Property("noCollision"), new Property("floor") },
                        null) {
                {
                        setOpacity(0);
                }
        }),
        /* 130 */
        GRASS_SMALL(new BlockType("grassSmall",
                        new Texture[] { new Texture(ShapeList.GRASS_SMALL,
                                        PathManager.loadImage("herbs/grass-small.png")) },
                        null,
                        new Property[] { new Property("noCollision"), new Property("floor") },
                        null) {
                {
                        setOpacity(0);
                }
        }),
        WATER_FAKE(new BlockType("waterFake",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.RECTANGLE2,
                                                        new String[] { "water/water-2-0.png", "water/water-2-1.png",
                                                                        "water/water-2-2.png" },
                                                        4) },
                        0,
                        new Collision[] { CollisionList.RECTANGLE2 },
                        new Property[] {
                                        new Property("noCollision"),
                                        new PropertySound(SoundType.WATER) },
                        new BlockBehavior[] {
                                        new BlockBehaviorApplyForce(0, 0, 0.5) })),
        LAVA_FAKE(new BlockType("lavaFake",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.RECTANGLE2,
                                                        new String[] { "lava/lava-2-0.png", "lava/lava-2-1.png",
                                                                        "lava/lava-2-2.png",
                                                                        "lava/lava-2-3.png" },
                                                        4) },
                        0,
                        new Collision[] { CollisionList.RECTANGLE2 },
                        new Property[] {
                                        new Property("noCollision"),
                                        new PropertySound(SoundType.LAVA),
                                        new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.4), 1.5, 0.5, 0.15)) },
                        new BlockBehavior[] {
                                        new BlockBehaviorKill(),
                                        new BlockBehaviorApplyForce(0, 0, 0.5) })),

        ;

        public final BlockType blockType;

        BlockTemplate(BlockType blockType) {
                this.blockType = blockType;
        }

        public static String toString(BlockTemplate[] blockTypes) {
                String res = "";
                for (int i = 0; i < blockTypes.length; i++)
                        res += i + " " + blockTypes[i].blockType.getName() + "\n";
                return res;
        }
}
