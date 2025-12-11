package objects.block;

import java.util.function.Consumer;

import audio.SoundType;
import graphics.Texture;
import graphics.variantTexture.VariantBlockMaker;
import graphics.ligth.*;
import graphics.shape.*;
import objects.block.blockBehavior.*;
import objects.block.blockBehavior.logic.*;
import objects.collision.*;
import objects.property.*;
import tools.PathManager;

public enum BlockTemplate {
        WHITE_BLOCK(VariantBlockMaker.createBlockType("whiteBlock", "white-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        BLACK_BLOCK(VariantBlockMaker.createBlockType("blackBlock", "black-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        RED_BLOCK(VariantBlockMaker.createBlockType("redBlock", "red-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        PURPLE_BLOCK(VariantBlockMaker.createBlockType("purpleBlock", "purple-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        BROWN_BLOCK(VariantBlockMaker.createBlockType("brownBlock", "brown-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        GREEN_BLOCK(VariantBlockMaker.createBlockType("greenBlock", "green-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),

        RED_CARPET(VariantBlockMaker.createBlockType("redCarpet", "red-carpet-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        RED_GOLDEN_CARPET(new BlockType("redGoldenCarpet", "red-golden-carpet-block.png")),
        RED_GOLDEN_CARPET2(new BlockType("redGoldenCarpet2", "red-golden-carpet-2-block.png")),
        RED_GOLDEN_CARPET3(new BlockType("redGoldenCarpet3", "red-golden-carpet-3-block.png")),

        BLUE_BLOCK(VariantBlockMaker.createBlockType("blueBlock", "blue-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        BLUE_BLOCK_BROKEN(new BlockType("blueBlockBroken", "blue-block-broken.png")),
        BLUE_BLOCK_CLUBS(new BlockType("blueBlockClubs", "blue-block-clubs.png")),
        BLUE_BLOCK_RAYS(new BlockType("blueBlockRays", "blue-block-rays.png")),
        BLUE_BLOCK_COLUMN(VariantBlockMaker.createBlockType("blueBlockColumn", "blue-block-column.png", ShapeList.COLUMN)),

        BLOCK(VariantBlockMaker.createBlockType("block", "block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        TRAY(new BlockType("blockTray", "block-tray.png")),
        BRICK1(new BlockType("blockBrick1", "block-brick-1.png")),
        BRICK2(new BlockType("blockBrick2", "block-brick-2.png")),
        BROKEN(new BlockType("blockBroken", "block-broken.png",
                        null,
                        new Property[] { new Property(PropertyList.DESTRUCTIBLE)},
                        null
        )),
        HOLE1(new BlockType("blockHole1", "block-hole-1.png")),
        HOLE2(new BlockType("blockHole2", "block-hole-2.png")),
        LINES(new BlockType("blockLines", "block-lines.png")),
        BLOCK_FLOWER(new BlockType("blockFlower", "block-flower.png")),
        BLOCK_CLUBS(new BlockType("blockClubs", "block-clubs.png")),
        BLOCK_ALTERNATE(new BlockType("blockAlternate", "block-alternate.png")),
        BLOCK_COLUMN(VariantBlockMaker.createBlockType("blockColumn", "block-column.png", ShapeList.COLUMN)),
        BLOCK_BRICKS(VariantBlockMaker.createBlockType("blockBricks", "block-bricks.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        YELLOW_BRICKS(VariantBlockMaker.createBlockType("yellowBricks", "yellow-bricks.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        
        MOSSY1(VariantBlockMaker.createBlockType("blockMossy1", "block-mossy-1.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        MOSSY2(VariantBlockMaker.createBlockType("blockMossy2", "block-mossy-2.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        BLOCK_COLUMN_MOSSY(VariantBlockMaker.createBlockType("blockColumnMossy", "block-column-mossy.png", ShapeList.COLUMN)),

        HALF_GRASS1(new BlockType("blockHalfGrass1", "block-half-grass-1.png")),
        HALF_GRASS2(new BlockType("blockHalfGrass2", "block-half-grass-2.png")),
        HALF_GRASS3(new BlockType("blockHalfGrass3", "block-half-grass-3.png")),
        HALF_GRASS4(new BlockType("blockHalfGrass4", "block-half-grass-4.png")),
        BLOCK_GRASS_CORNER_OUT_LEFT(new BlockType("blockGrassCornerOuterLeft", "block-grass-corner-outer-left.png")),
        BLOCK_GRASS_CORNER_OUT_RIGHT(new BlockType("blockGrassCornerOuterRight", "block-grass-corner-outer-right.png")),
        BLOCK_GRASS_CORNER_OUT_TOP(new BlockType("blockGrassCornerOuterTop", "block-grass-corner-outer-top.png")),
        BLOCK_GRASS_CORNER_OUT_BOTTOM(
                        new BlockType("blockGrassCornerOuterBottom", "block-grass-corner-outer-bottom.png")),
        BLOCK_GRASS_CORNER_IN_LEFT(new BlockType("blockGrassCornerInnerLeft", "block-grass-corner-inner-left.png")),
        BLOCK_GRASS_CORNER_IN_RIGHT(new BlockType("blockGrassCornerInnerRight", "block-grass-corner-inner-right.png")),
        BLOCK_GRASS_CORNER_IN_TOP(new BlockType("blockGrassCornerInnerTop", "block-grass-corner-inner-top.png")),
        BLOCK_GRASS_CORNER_IN_BOTTOM(
                        new BlockType("blockGrassCornerInnerBottom", "block-grass-corner-inner-bottom.png")),

        GRASS_HALF_SLAB_1(new BlockType("grassHalfSlab1", "grass-half-slab-1.png")),
        GRASS_HALF_SLAB_2(new BlockType("grassHalfSlab2", "grass-half-slab-2.png")),
        GRASS_SLAB(new BlockType("grassSlab", "grass-slab.png")),
        GRASS_SLABS(new BlockType("grassSlabs", "grass-slabs.png")),
        GRASS_HALF_SLAB_3(new BlockType("GrassHalfSlab3", "grass-half-slab-3.png")),
        GRASS_HALF_SLAB_4(new BlockType("GrassHalfSlab4", "grass-half-slab-4.png")),
        GRASS(new BlockType("grassBlock", "grass.png")),
        
        DIRT(new BlockType("dirtBlock", "dirt.png")),
        SAND(new BlockType("sand", "sand-block.png")),
        ICE(new BlockType("ice", "ice.png") {
                {
                        setOpacity(0.5);
                        setAdherency(0.01);
                }
        }),
        SNOW(new BlockType("snow", "snow.png")),
        DISTORTED_BLOCK_BLUE(new BlockType("distortedBlockBlue", "distorted-block-blue.png")),
        DISTORTED_BLOCK_GREEN(new BlockType("distortedBlockGreen", "distorted-block-green.png")),
        DISTORTED_BLOCK_ORANGE(new BlockType("distortedBlockOrange", "distorted-block-orange.png")),
        DISTORTED_BLOCK_PURPLE(new BlockType("distortedBlockPurple", "distorted-block-purple.png")),
        DISTORTED_BLOCK_RED(new BlockType("distortedBlockRed", "distorted-block-red.png")),
        DISTORTED_BLOCK_YELLOW(new BlockType("distortedBlockYellow", "distorted-block-yellow.png")),
        GRAVEL_DARK(new BlockType("gravelDark", "gravel-dark.png")),
        GRAVEL(new BlockType("gravel", "gravel.png")),

        STONE(VariantBlockMaker.createBlockType("stone", "stone.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        STONE_MOSSY(VariantBlockMaker.createBlockType("stoneMossy", "stone-mossy.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        STONE_POLISHED(new BlockType("stonePolished", "stone-polished.png")),
        STONE_BROKEN(new BlockType("stoneBroken", "stone-broken.png")),

        WATER(new BlockType("water",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.RECTANGLE1,
                                                        new String[] { "water/water-1-0.png", "water/water-1-1.png",
                                                                        "water/water-1-2.png" },
                                                        5),
                                        Texture.createBasicTexture(ShapeList.RECTANGLE2,
                                                        new String[] { "water/water-2-0.png", "water/water-2-1.png",
                                                                        "water/water-2-2.png" },
                                                        5),
                                        Texture.createBasicTexture(ShapeList.RECTANGLE3,
                                                        new String[] { "water/water-3-0.png", "water/water-3-1.png",
                                                                        "water/water-3-2.png" },
                                                        5),
                                        Texture.createBasicTexture(
                                                        new String[] { "water/water-4-0.png", "water/water-4-1.png",
                                                                        "water/water-4-2.png" },
                                                        5) },
                        new Collision[] { CollisionList.RECTANGLE1, CollisionList.RECTANGLE2, CollisionList.RECTANGLE3,
                                        CollisionList.CUBE },
                        new Property[] {
                                        new Property(PropertyList.NO_COLLISION),
                                        new PropertySound(SoundType.WATER) },
                        new BlockBehavior[] { new BlockBehaviorLiquid(4, 2),
                                        new BlockBehaviorApplyForce(0, 0, 0.4) })),
        WATER_FAKE(new BlockType("waterFake",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.RECTANGLE3,
                                                        new String[] { "water/water-3-0.png", "water/water-3-1.png",
                                                                        "water/water-3-2.png" },
                                                        5) },
                        new Collision[] { CollisionList.RECTANGLE3 },
                        new Property[] {
                                        new Property(PropertyList.NO_COLLISION),
                                        new PropertySound(SoundType.WATER) },
                        new BlockBehavior[] {
                                        new BlockBehaviorApplyForce(0, 0, 0.4) })),
        LAVA(new BlockType("lava",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.RECTANGLE1,
                                                        new String[] { "lava/lava-1-0.png", "lava/lava-1-1.png",
                                                                        "lava/lava-1-2.png",
                                                                        "lava/lava-1-3.png" },
                                                        5),
                                        Texture.createBasicTexture(ShapeList.RECTANGLE2,
                                                        new String[] { "lava/lava-2-0.png", "lava/lava-2-1.png",
                                                                        "lava/lava-2-2.png",
                                                                        "lava/lava-2-3.png" },
                                                        5),
                                        Texture.createBasicTexture(ShapeList.RECTANGLE3,
                                                        new String[] { "lava/lava-3-0.png", "lava/lava-3-1.png",
                                                                        "lava/lava-3-2.png",
                                                                        "lava/lava-3-3.png" },
                                                        5),
                                        Texture.createBasicTexture(
                                                        new String[] { "lava/lava-4-0.png", "lava/lava-4-1.png",
                                                                        "lava/lava-4-2.png", "lava/lava-4-3.png" },
                                                        5) },
                        new Collision[] { CollisionList.RECTANGLE1, CollisionList.RECTANGLE2, CollisionList.RECTANGLE3,
                                        CollisionList.CUBE },
                        new Property[] {
                                        new Property(PropertyList.NO_COLLISION),
                                        new PropertySound(SoundType.LAVA),
                                        new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.4), 1.5, 0.5, 0.15)) },
                        new BlockBehavior[] { new BlockBehaviorLiquid(4, 6),
                                        new BlockBehaviorKill(),
                                        new BlockBehaviorApplyForce(0, 0, 0.4),
                                        new BlockBehaviorBlazable() })),
        LAVA_FAKE(new BlockType("lavaFake",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.RECTANGLE3,
                                                        new String[] { "lava/lava-3-0.png", "lava/lava-3-1.png",
                                                                        "lava/lava-3-2.png",
                                                                        "lava/lava-3-3.png" },
                                                        5) },
                        new Collision[] { CollisionList.RECTANGLE3 },
                        new Property[] {
                                        new Property(PropertyList.NO_COLLISION),
                                        new PropertySound(SoundType.LAVA),
                                        new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.4), 1.5, 0.5, 0.15)) },
                        new BlockBehavior[] {
                                        new BlockBehaviorKill(),
                                        new BlockBehaviorApplyForce(0, 0, 0.4),
                                        new BlockBehaviorBlazable() })),

        GRID_LEFT(new BlockType("gridLeft",
                        new Texture[] { new Texture(ShapeList.BORDER_LEFT,
                                        PathManager.loadImage("grids/grid-left.png")) },
                        new Collision[] { CollisionList.BARRIER_LEFT },
                        null,
                        null)),
        GRID_RIGHT(new BlockType("gridRight",
                        new Texture[] { new Texture(ShapeList.BORDER_RIGHT,
                                        PathManager.loadImage("grids/grid-right.png")) },
                        new Collision[] { CollisionList.BARRIER_RIGHT },
                        null,
                        null)),
        GRID_HORIZONTAL(new BlockType("gridHorizontal",
                        new Texture[] { new Texture(ShapeList.BORDER_HORIZONTAL,
                                        PathManager.loadImage("grids/grid-horizontal.png")) },
                        new Collision[] { CollisionList.BARRIER_HORIZONTAL },
                        null,
                        null)),

        SIGN_LEFT(new BlockType("SignLeft",
                        new Texture[] { new Texture(ShapeList.SIGN_LEFT, PathManager.loadImage("sign-left.png")) },
                        null,
                        null,
                        new BlockBehavior[] { new BlockBehaviorText() })),
        SIGN_RIGHT(new BlockType("SignRight",
                        new Texture[] { new Texture(ShapeList.SIGN_RIGHT, PathManager.loadImage("sign-right.png")) },
                        null,
                        null,
                        new BlockBehavior[] { new BlockBehaviorText() })),
        
        FRAME_LEFT(new BlockType("frameLeft",
                        new Texture[] { new Texture(ShapeList.INSIDE,
                                        PathManager.loadImage("frame-left.png")) },
                        null,
                        new Property[] {new Property(PropertyList.NO_COLLISION), new Property("floor")},
                        new BlockBehavior[] { new BlockBehaviorText() })),
        FRAME_RIGHT(new BlockType("frameRight",
                        new Texture[] { new Texture(ShapeList.INSIDE,
                                        PathManager.loadImage("frame-right.png")) },
                        null,
                        new Property[] {new Property(PropertyList.NO_COLLISION), new Property("floor")},
                        new BlockBehavior[] { new BlockBehaviorText() })),

        ACACIA_LOG(VariantBlockMaker.createBlockType("acaciaLog", "wood/acacia-log.png", ShapeList.COLUMN)),
        OAK_LOG(VariantBlockMaker.createBlockType("oakLog", "wood/oak-log.png", ShapeList.COLUMN)),
        PINE_LOG(VariantBlockMaker.createBlockType("pineLog", "wood/pine-log.png", ShapeList.COLUMN)),
        WHITE_LOG(VariantBlockMaker.createBlockType("whiteLog", "wood/white-log.png", ShapeList.COLUMN)),
        WOOD_ACACIA(VariantBlockMaker.createBlockType("woodAcacia", "wood/wood-acacia.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BARRIERS, ShapeList.SLABS)),
        WOOD_OAK(VariantBlockMaker.createBlockType("woodOak", "wood/wood-oak.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BARRIERS, ShapeList.SLABS)),
        WOOD_PINE(VariantBlockMaker.createBlockType("woodPine", "wood/wood-pine.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BARRIERS, ShapeList.SLABS)),
        WOOD_WHITE(VariantBlockMaker.createBlockType("woodWhite", "wood/wood-white.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BARRIERS, ShapeList.SLABS)),
        
        HEDGE(new BlockType("hedge", "hedge.png",
                null,
                new Property[] { new Property(PropertyList.BURNABLE) },
                null
        )),
        LEAVES(new BlockType("leaves", "leaves.png",
        null,
                new Property[] { new Property(PropertyList.BURNABLE) },
                null
        )),

        SHELVE(new BlockType("shelve",
                new Texture[] { new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("shelve.png")) },
                null
        )),
        BOOK_SHELVE_1(new BlockType("bookShelve1", "book-shelve-1.png")),
        BOOK_SHELVE_2(new BlockType("bookShelve2", "book-shelve-2.png")),

        GLASS(
                apply(
                        VariantBlockMaker.createBlockType(
                                "glassBlock",
                                new Texture(
                                        ShapeList.TRANSPARENT_CUBE,
                                        PathManager.loadImage("glass-block.png")
                                ),
                                ShapeList.BORDERS
                                ),
                        block -> 
                {
                        block.setColor(new ColorRGB(1, 0, 0));
                        block.setOpacity(0.1);
                        block.setAllowLight(Face.LEFT.index, true);
                        block.setAllowLight(Face.RIGHT.index, true);
                        block.setAllowLight(Face.TOP.index, true);
                })),
        STAINED_GLASS_RED_LEFT(new BlockType("stainedGlassRedLeft",
                        new Texture[] { new Texture(ShapeList.BORDER_LEFT,
                                        PathManager.loadImage("stained-glass/stained-glass-red-left.png")) },
                        new Collision[] { CollisionList.BARRIER_LEFT },
                        null,
                        null) {
                {
                        setColor(new ColorRGB(1, 0, 0));
                        setOpacity(0.1);
                        setAllowLight(Face.RIGHT.index,true);
                }
        }),
        STAINED_GLASS_RED_RIGHT(new BlockType("stainedGlassRedRight",
                        new Texture[] { new Texture(ShapeList.BORDER_RIGHT,
                                        PathManager.loadImage("stained-glass/stained-glass-red-right.png")) },
                        new Collision[] { CollisionList.BARRIER_RIGHT },
                        null,
                        null) {
                {
                        setColor(new ColorRGB(1, 0, 0));
                        setOpacity(0.1);
                        setAllowLight(Face.LEFT.index,true);
                }
        }),
        STAINED_GLASS_BLUE_LEFT(new BlockType("stainedGlassBlueLeft",
                        new Texture[] { new Texture(ShapeList.BORDER_LEFT,
                                        PathManager.loadImage("stained-glass/stained-glass-blue-left.png")) },
                        new Collision[] { CollisionList.BARRIER_LEFT },
                        null,
                        null) {
                {
                        setColor(new ColorRGB(0, 0, 1));
                        setOpacity(0.1);
                        setAllowLight(Face.RIGHT.index,true);
                }
        }),
        STAINED_GLASS_BLUE_RIGHT(new BlockType("stainedGlassBlueRight",
                        new Texture[] { new Texture(ShapeList.BORDER_RIGHT,
                                        PathManager.loadImage("stained-glass/stained-glass-blue-right.png")) },
                        new Collision[] { CollisionList.BARRIER_RIGHT },
                        null,
                        null) {
                {
                        setColor(new ColorRGB(0, 0, 1));
                        setOpacity(0.1);
                        setAllowLight(Face.LEFT.index,true);
                }
        }),
        STAINED_GLASS_GREEN_LEFT(new BlockType("stainedGlassGreenLeft",
                        new Texture[] {
                                        new Texture(ShapeList.BORDER_LEFT,
                                                        PathManager.loadImage(
                                                                        "stained-glass/stained-glass-green-left.png")) },
                        new Collision[] { CollisionList.BARRIER_LEFT },
                        null,
                        null) {
                {
                        setColor(new ColorRGB(0, 1, 0));
                        setOpacity(0.1);
                        setAllowLight(Face.RIGHT.index,true);
                }
        }),
        STAINED_GLASS_GREEN_RIGHT(new BlockType("stainedGlassGreenRight",
                        new Texture[] {
                                        new Texture(ShapeList.BORDER_RIGHT,
                                                        PathManager.loadImage(
                                                                        "stained-glass/stained-glass-green-right.png")) },
                        new Collision[] { CollisionList.BARRIER_RIGHT },
                        null,
                        null) {
                {
                        setColor(new ColorRGB(0, 1, 0));
                        setOpacity(0.1);
                        setAllowLight(Face.LEFT.index,true);
                }
        }),

        LEVER(new BlockType("lever",
                        new Texture[] { new Texture(ShapeList.LEVER_OFF, PathManager.loadImage("lever/lever-F.png")),
                                        new Texture(ShapeList.LEVER_ON, PathManager.loadImage("lever/lever-T.png")) },
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] {
                                        new BlockBehaviorLever()
                        })),
        LINKED_LEVER(new BlockType("linkedLever",
                        new Texture[] { new Texture(ShapeList.LEVER_OFF,
                                        PathManager.loadImage("linkedLever/linked-lever-F.png")),
                                        new Texture(ShapeList.LEVER_ON,
                                                        PathManager.loadImage("linkedLever/linked-lever-T.png")) },
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] { new BlockBehaviorLever(), new BlockBehaviorActivable() })),
        PRESSURE_PLATE(new BlockType("pressurePlate",
                        new Texture[] { Texture.createBasicTexture("pressure-plate-F.png"),
                                        Texture.createBasicTexture("pressure-plate-T.png") },
                        null,
                        new Property[] { new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.4), 0.2, 0.5, 0)) },
                        new BlockBehavior[] { new BlockBehaviorPressurePlate(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME) })),
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
        TNT(new BlockType("tnt",
                        new Texture[] { new Texture(ShapeList.CUBE,
                                        PathManager.loadImage("tnt.png")) },
                        null,
                        new Property[] {new Property(PropertyList.DESTRUCTIBLE)},
                        new BlockBehavior[] {new BlockBehaviorExploding(2.5)}
                        )),
        EXPLOSION(new BlockType("explosion",
                        new Texture[] { Texture.createBasicTexture(ShapeList.TRANSPARENT_CUBE,
                                                        new String[] { "explosion/explosion-0.png", "explosion/explosion-1.png", "explosion/explosion-2.png","explosion/explosion-3.png", "explosion/explosion-4.png", "explosion/explosion-5.png" }, 
                                                        2) },
                        null,
                        new Property[] {new Property(PropertyList.NO_COLLISION),
                                        new PropertyLight(new LightSource(
                                                new ColorRGB(1, 0.5, 0.5), 1, 0.5, 0.1))},
                        new BlockBehavior[] {new BlockBehaviorEffect(12), 
                                        new BlockBehaviorKill(),
                                        new BlockBehaviorBlazable()}
                        )),
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

        AND(new BlockType("and",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] { new BlockBehaviorAnd() })),
        OR(new BlockType("or",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] { new BlockBehaviorOr() })),
        NOT(new BlockType("not",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] { new BlockBehaviorNot() })),
        DELAY(new BlockType("delay",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] { new BlockBehaviorDelay() })),
        LOOP(new BlockType("loop",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] { new BlockBehaviorLoop() })),

        ROOF_TILES_RED1(new BlockType("roofTilesRed1",
                        new Texture[] { new Texture(ShapeList.SLOPE1,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red1.png")) },
                        new Collision[] { CollisionList.STAIR1 },
                        null,
                        null)),
        ROOF_TILES_RED2(new BlockType("roofTilesRed2",
                        new Texture[] { new Texture(ShapeList.SLOPE2,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red2.png")) },
                        new Collision[] { CollisionList.STAIR2 },
                        null,
                        null)),
        ROOF_TILES_RED_ANGLE1(new BlockType("roofTilesRedAngle1",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE1,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-angle1.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE1 },
                        null,
                        null)),
        ROOF_TILES_RED_ANGLE2(new BlockType("roofTilesRedAngle2",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE2,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-angle2.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE2 },
                        null,
                        null)),
        ROOF_TILES_RED_ANGLE4(new BlockType("roofTilesRedAngle4",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE4,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-angle4.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE4 },
                        null,
                        null)),
        ROOF_TILES_RED_ANGLE5(new BlockType("roofTilesRedAngle5",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE5,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-angle5.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE5 },
                        null,
                        null)),
        ROOF_TILES_RED_ANGLE6(new BlockType("roofTilesRedAngle6",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE6,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-angle6.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE6 },
                        null,
                        null)),
        ROOF_TILES_RED_ANGLE8(new BlockType("roofTilesRedAngle8",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE8,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-angle8.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE8 },
                        null,
                        null)),
        ROOF_TILES_RED_WOOD_ANGLE1(new BlockType("roofTilesRedWoodAngle1",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE1,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-wood-angle1.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE1 },
                        null,
                        null)),
        ROOF_TILES_RED_WOOD_ANGLE2(new BlockType("roofTilesRedWoodAngle2",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE2,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-wood-angle2.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE2 },
                        null,
                        null)),
        ROOF_TILES_RED_WOOD_ANGLE4(new BlockType("roofTilesRedWoodAngle4",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE4,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-wood-angle4.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE4 },
                        null,
                        null)),
        ROOF_TILES_RED_WOOD_ANGLE5(new BlockType("roofTilesRedWoodAngle5",
                        new Texture[] { new Texture(ShapeList.SLOPE_ANGLE5,
                                        PathManager.loadImage("roofTilesRed/roof-tiles-red-wood-angle5.png")) },
                        new Collision[] { CollisionList.STAIR_ANGLE5 },
                        null,
                        null)),

        HOLDER(new BlockType("holder",
                        new Texture[] { new Texture(ShapeList.HOLDER,
                                        PathManager.loadImage("holders/holder.png")) },
                        new Collision[] { CollisionList.POLE },
                        null,
                        null)),
        HOLDER_ROPE1(new BlockType("holderRope1",
                        new Texture[] { new Texture(ShapeList.HOLDER_ROPE1,
                                        PathManager.loadImage("holders/holder-rope1.png")) },
                        new Collision[] { CollisionList.POLE },
                        null,
                        null)),
        HOLDER_ROPE2(new BlockType("holderRope2",
                        new Texture[] { new Texture(ShapeList.HOLDER_ROPE2,
                                        PathManager.loadImage("holders/holder-rope2.png")) },
                        new Collision[] { CollisionList.POLE },
                        null,
                        null)),
        HOLDER_ROPE3(new BlockType("holderRope3",
                        new Texture[] { new Texture(ShapeList.HOLDER_ROPE3,
                                        PathManager.loadImage("holders/holder-rope3.png")) },
                        new Collision[] { CollisionList.POLE },
                        null,
                        null)),

        SUN(new BlockType("sun",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION),
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
                                        }, 1500, 0.75, 1, 0)) },
                        null)),
        SUNLIGHT(new BlockType("sunlight",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION),
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
        WHITE_LIGHT(new BlockType("whiteLight",
                        (Texture[]) null,
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION),
                                        new PropertyLight(new LightSource(
                                                        new ColorRGB(1, 1, 1), 1, 0.65, 0)) },
                        null)),

        FIRE_CAMP(new BlockType("fireCamp",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.TRANSPARENT_CUBE,
                                                        new String[] { "fire-camp/fire-0.png", "fire-camp/fire-1.png", "fire-camp/fire-2.png" }, 
                                                        2) },
                        null,
                        new Property[] { new PropertyLight(
                                        new LightSource(new ColorRGB(1, 0.6, 0.2), 1.2, 0.8, 0.1)),
                                        new Property(PropertyList.NO_COLLISION),
                                        new PropertySound(SoundType.FIRE) },
                        new BlockBehavior[] { new BlockBehaviorBlazable() })),
        BRAZIER(new BlockType("brazier",
                        new Texture[] {
                                        Texture.createBasicTexture(ShapeList.TRANSPARENT_CUBE,
                                                        new String[] { "brazier/fire-0.png", "brazier/fire-1.png", "brazier/fire-2.png" }, 
                                                        2) },
                        new Collision[] {CollisionList.COLUMN},
                        new Property[] { new PropertyLight(
                                        new LightSource(new ColorRGB(1, 0.6, 0.2), 1.2, 0.8, 0.1)),
                                        new PropertySound(SoundType.FIRE) },
                        new BlockBehavior[] { new BlockBehaviorBlazable() })),
        
        FLOWERS(new BlockType("flowers",
                        new Texture[] { new Texture(ShapeList.FLOWERS, PathManager.loadImage("herbs/flowers.png")) },
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION), new Property("floor") },
                        null)),
        GRASS_BIG(new BlockType("grassBig",
                        new Texture[] { new Texture(ShapeList.GRASS_BIG,
                                        PathManager.loadImage("herbs/grass-big.png")) },
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION), new Property("floor") },
                        null)),
        GRASS_SMALL(new BlockType("grassSmall",
                        new Texture[] { new Texture(ShapeList.GRASS_SMALL,
                                        PathManager.loadImage("herbs/grass-small.png")) },
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION), new Property("floor") },
                        null)),
        
        LIAN_LEFT(new BlockType("lianLeft",
                        new Texture[] { new Texture(ShapeList.INSIDE,
                                        PathManager.loadImage("lian-left.png")) },
                        null,
                        new Property[] {new Property(PropertyList.NO_COLLISION), new Property(PropertyList.BURNABLE)},
                        null)),
        LIAN_RIGHT(new BlockType("lianRight",
                        new Texture[] { new Texture(ShapeList.INSIDE,
                                        PathManager.loadImage("lian-right.png")) },
                        null,
                        new Property[] {new Property(PropertyList.NO_COLLISION), new Property(PropertyList.BURNABLE)},
                        null)),
        
        WIND(new BlockType("wind",
                        new Texture[] {},
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] {
                                        new BlockBehaviorApplyForce(1,0,0) })),

        STAINED_GLASS_RED(
                apply(
                        VariantBlockMaker.createBlockType(
                                "stainedGlassRed",
                                new Texture(
                                        ShapeList.TRANSPARENT_CUBE,
                                        PathManager.loadImage("stained-glass/stained-glass-red.png")
                                ),
                                ShapeList.BORDERS
                                ),
                        block -> 
                {
                        block.setColor(new ColorRGB(1, 0, 0));
                        block.setOpacity(0.1);
                        block.setAllowLight(Face.LEFT.index, true);
                        block.setAllowLight(Face.RIGHT.index, true);
                        block.setAllowLight(Face.TOP.index, true);
                })),
        STAINED_GLASS_BLUE(
                apply(
                        VariantBlockMaker.createBlockType(
                                "stainedGlassBlue",
                                new Texture(
                                        ShapeList.TRANSPARENT_CUBE,
                                        PathManager.loadImage("stained-glass/stained-glass-blue.png")
                                ),
                                ShapeList.BORDERS
                                ),
                        block -> 
                {
                        block.setColor(new ColorRGB(0, 0, 1));
                        block.setOpacity(0.1);
                        block.setAllowLight(Face.LEFT.index, true);
                        block.setAllowLight(Face.RIGHT.index, true);
                        block.setAllowLight(Face.TOP.index, true);
                })),
        STAINED_GLASS_GREEN(
                apply(
                        VariantBlockMaker.createBlockType(
                                "stainedGlassGreen",
                                new Texture(
                                        ShapeList.TRANSPARENT_CUBE,
                                        PathManager.loadImage("stained-glass/stained-glass-green.png")
                                ),
                                ShapeList.BORDERS
                                ),
                        block -> 
                {
                        block.setColor(new ColorRGB(0, 1, 0));
                        block.setOpacity(0.1);
                        block.setAllowLight(Face.LEFT.index, true);
                        block.setAllowLight(Face.RIGHT.index, true);
                        block.setAllowLight(Face.TOP.index, true);
                })),

        
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

        public static <T> T apply(T obj, Consumer<T> fn) {
                fn.accept(obj);
                return obj;
        }

}
