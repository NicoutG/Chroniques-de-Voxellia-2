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
        RED_GOLDEN_CARPET(new BlockType("redGoldenCarpet",
                new Texture[] { Texture.createBasicTexture("red-golden-carpet-block.png"),
                        Texture.createBasicTexture("red-golden-carpet-2-block.png"),
                        Texture.createBasicTexture("red-golden-carpet-3-block.png")
                 },
                null
        )),

        BLUE_BLOCK(VariantBlockMaker.createBlockType("blueBlock", "blue-block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        BLUE_BLOCK_BROKEN(new BlockType("blueBlockBroken", "blue-block-broken.png")),
        BLUE_BLOCK_CLUBS(new BlockType("blueBlockClubs", "blue-block-clubs.png")),
        BLUE_BLOCK_RAYS(new BlockType("blueBlockRays", "blue-block-rays.png")),
        BLUE_BLOCK_COLUMN(VariantBlockMaker.createBlockType("blueBlockColumn", "blue-block-column.png", ShapeList.COLUMN)),

        BLOCK(VariantBlockMaker.createBlockType("block", "block.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        TRAY(new BlockType("blockTray", "block-tray.png")),
        BRICK(new BlockType("blockBrick",
                new Texture[] { Texture.createBasicTexture("block-brick-1.png"),
                        Texture.createBasicTexture("block-brick-2.png")
                },
                null
        )),
        BROKEN(new BlockType("blockBroken", "block-broken.png",
                        null,
                        new Property[] { new Property(PropertyList.DESTRUCTIBLE)},
                        null
        )),
        HOLE(new BlockType("blockHole",
                new Texture[] { Texture.createBasicTexture("block-hole-1.png"),
                        Texture.createBasicTexture("block-hole-2.png")
                 },
                null
        )),
        LINES(new BlockType("blockLines", "block-lines.png")),
        BLOCK_FLOWER(new BlockType("blockFlower", "block-flower.png")),
        BLOCK_CLUBS(new BlockType("blockClubs", "block-clubs.png")),
        BLOCK_ALTERNATE(new BlockType("blockAlternate", "block-alternate.png")),
        BLOCK_COLUMN(VariantBlockMaker.createBlockType("blockColumn", "block-column.png", ShapeList.COLUMN)),
        BLOCK_BRICKS(VariantBlockMaker.createBlockType("blockBricks", "block-bricks.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        YELLOW_BRICKS(VariantBlockMaker.createBlockType("yellowBricks", "yellow-bricks.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        
        MOSSY(VariantBlockMaker.createBlockType("blockMossy", "block-mossy-1.png", ShapeList.STAIRS, ShapeList.SLOPES, ShapeList.BORDERS, ShapeList.SLABS)),
        BLOCK_COLUMN_MOSSY(VariantBlockMaker.createBlockType("blockColumnMossy", "block-column-mossy.png", ShapeList.COLUMN)),

        HALF_GRASS(new BlockType("blockHalfGrass",
                new Texture[] { Texture.createBasicTexture("block-half-grass-1.png"),
                        Texture.createBasicTexture("block-half-grass-2.png"),
                        Texture.createBasicTexture("block-half-grass-3.png"),
                        Texture.createBasicTexture("block-half-grass-4.png"),
                        Texture.createBasicTexture("block-grass-corner-outer-left.png"),
                        Texture.createBasicTexture("block-grass-corner-outer-right.png"),
                        Texture.createBasicTexture("block-grass-corner-outer-top.png"),
                        Texture.createBasicTexture("block-grass-corner-outer-bottom.png"),
                        Texture.createBasicTexture("block-grass-corner-inner-left.png"),
                        Texture.createBasicTexture("block-grass-corner-inner-right.png"),
                        Texture.createBasicTexture("block-grass-corner-inner-top.png"),
                        Texture.createBasicTexture("block-grass-corner-inner-bottom.png")
                 },
                null
        )),
        GRASS(new BlockType("grassBlock",
                new Texture[] { Texture.createBasicTexture("grass.png"),
                        Texture.createBasicTexture("grass-half-slab-1.png"),
                        Texture.createBasicTexture("grass-half-slab-2.png"),
                        Texture.createBasicTexture("grass-half-slab-3.png"),
                        Texture.createBasicTexture("grass-half-slab-4.png"),
                        Texture.createBasicTexture("grass-slab.png"),
                        Texture.createBasicTexture("grass-slabs.png"),
                 },
                null
        )),
        
        DIRT(new BlockType("dirtBlock", "dirt.png")),
        SAND(new BlockType("sand", "sand-block.png")),
        ICE(new BlockType("ice", "ice.png") {
                {
                        setOpacity(0.5);
                        setAdherency(0.01);
                }
        }),
        SNOW(new BlockType("snow", "snow.png")),
        DISTORTED_BLOCK(new BlockType("distortedBlock",
                new Texture[] { Texture.createBasicTexture("distorted-block-blue.png"),
                        Texture.createBasicTexture("distorted-block-green.png"),
                        Texture.createBasicTexture("distorted-block-orange.png"),
                        Texture.createBasicTexture("distorted-block-purple.png"),
                        Texture.createBasicTexture("distorted-block-red.png"),
                        Texture.createBasicTexture("distorted-block-yellow.png")
                 },
                null
        )),
        GRAVEL(new BlockType("gravel",
                new Texture[] { Texture.createBasicTexture("gravel-dark.png"),
                        Texture.createBasicTexture("gravel.png")
                 },
                null
        )),

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

        GRID(new BlockType("grid",
                        new Texture[] { new Texture(ShapeList.BORDER_LEFT, PathManager.loadImage("grids/grid-left.png")), 
                                new Texture(ShapeList.BORDER_RIGHT, PathManager.loadImage("grids/grid-right.png")),
                                new Texture(ShapeList.BORDER_RIGHT, PathManager.loadImage("grids/grid-horizontal.png")),
                        },
                        new Collision[] { CollisionList.BARRIER_LEFT, CollisionList.BARRIER_RIGHT, CollisionList.BARRIER_HORIZONTAL },
                        null,
                        null)),

        SIGN(new BlockType("sign",
                        new Texture[] { new Texture(ShapeList.SIGN_LEFT, PathManager.loadImage("sign-left.png")), 
                                new Texture(ShapeList.SIGN_RIGHT, PathManager.loadImage("sign-right.png")),
                        },
                        null,
                        null,
                        new BlockBehavior[] { new BlockBehaviorText() })),
        
        FRAME(new BlockType("frame",
                        new Texture[] { new Texture(ShapeList.INSIDE, PathManager.loadImage("frame-left.png")),
                                new Texture(ShapeList.INSIDE, PathManager.loadImage("frame-right.png"))
                        },
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

        LEAVES(new BlockType("leaves",
                new Texture[] { Texture.createBasicTexture("hedge.png"),
                        Texture.createBasicTexture("leaves.png")
                },
                null,
                new Property[] { new Property(PropertyList.BURNABLE) },
                null
        )),

        SHELVE(new BlockType("shelve",
                new Texture[] { new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("shelve.png")),
                        new Texture(ShapeList.CUBE, PathManager.loadImage("book-shelve-1.png")),
                        new Texture(ShapeList.CUBE, PathManager.loadImage("book-shelve-2.png"))
                 },
                null
        )),

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

        LEVER(new BlockType("lever",
                        new Texture[] { new Texture(ShapeList.LEVER_OFF, PathManager.loadImage("lever/lever-F.png")),
                                        new Texture(ShapeList.LEVER_ON, PathManager.loadImage("lever/lever-T.png")) },
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] { new BlockBehaviorSwitchTexture(), 
                                new BlockBehaviorLever(), 
                                new BlockBehaviorActivableSound(SoundType.LEVER, SoundType.LEVER) 
                        })),
        LINKED_LEVER(new BlockType("linkedLever",
                        new Texture[] { new Texture(ShapeList.LEVER_OFF,
                                        PathManager.loadImage("linkedLever/linked-lever-F.png")),
                                        new Texture(ShapeList.LEVER_ON, PathManager.loadImage("linkedLever/linked-lever-T.png")) },
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] { new BlockBehaviorSwitchTexture(), 
                                new BlockBehaviorLever(), 
                                new BlockBehaviorActivable(),
                                new BlockBehaviorActivableSound(SoundType.LEVER, SoundType.LEVER)
                        })),
        PRESSURE_PLATE(new BlockType("pressurePlate",
                        new Texture[] { Texture.createBasicTexture("pressure-plate-F.png"),
                                        Texture.createBasicTexture("pressure-plate-T.png") },
                        null,
                        new Property[] { new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.4), 0.2, 0.5, 0)) },
                        new BlockBehavior[] { new BlockBehaviorSwitchTexture(), new BlockBehaviorPressurePlate(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME),
                                        new BlockBehaviorActivableSound(SoundType.LEVER, SoundType.LEVER)
                        })),
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
                        new BlockBehavior[] { new BlockBehaviorSwitchTexture(), 
                                        new BlockBehaviorTeleportation(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME),
                                        new BlockBehaviorActivableSound(SoundType.WORLD_LOADER_ACTIVATION, SoundType.WORLD_LOADER_ACTIVATION)
                                 })),
        TNT(new BlockType("tnt",
                        new Texture[] { new Texture(ShapeList.CUBE, PathManager.loadImage("tnt.png")),
                                new Texture(ShapeList.COLUMN, PathManager.loadImage("tnt-barrel.png")),
                        },
                        new Collision[] {CollisionList.CUBE, CollisionList.COLUMN},
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
                        new BlockBehavior[] { new BlockBehaviorSwitchTexture(),
                                        new BlockBehaviorChangeWorld(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME),
                                        new BlockBehaviorActivableSound(SoundType.WORLD_LOADER_ACTIVATION, SoundType.WORLD_LOADER_ACTIVATION)
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
                        new BlockBehavior[] { new BlockBehaviorSwitchTexture(),
                                        new BlockBehaviorNewWorld(),
                                        new BlockBehaviorActivableProperty(PropertyLight.NAME),
                                        new BlockBehaviorActivableSound(SoundType.WORLD_LOADER_ACTIVATION, SoundType.WORLD_LOADER_ACTIVATION)
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

        ROOF_TILES_RED(new BlockType("roofTilesRed",
                        new Texture[] { new Texture(ShapeList.SLOPE1, PathManager.loadImage("roofTilesRed/roof-tiles-red1.png")),
                                new Texture(ShapeList.SLOPE2, PathManager.loadImage("roofTilesRed/roof-tiles-red2.png")),
                                new Texture(ShapeList.SLOPE3, PathManager.loadImage("roofTilesRed/roof-tiles-red3.png")),
                                new Texture(ShapeList.SLOPE4, PathManager.loadImage("roofTilesRed/roof-tiles-red4.png")),
                                new Texture(ShapeList.SLOPE_ANGLE1, PathManager.loadImage("roofTilesRed/roof-tiles-red-angle1.png")),
                                new Texture(ShapeList.SLOPE_ANGLE1, PathManager.loadImage("roofTilesRed/roof-tiles-red-wood-angle1.png")),
                                new Texture(ShapeList.SLOPE_ANGLE2, PathManager.loadImage("roofTilesRed/roof-tiles-red-angle2.png")),
                                new Texture(ShapeList.SLOPE_ANGLE2, PathManager.loadImage("roofTilesRed/roof-tiles-red-wood-angle2.png")),
                                new Texture(ShapeList.SLOPE_ANGLE3, PathManager.loadImage("roofTilesRed/roof-tiles-red-angle3.png")),
                                new Texture(ShapeList.SLOPE_ANGLE4, PathManager.loadImage("roofTilesRed/roof-tiles-red-angle4.png")),
                                new Texture(ShapeList.SLOPE_ANGLE4, PathManager.loadImage("roofTilesRed/roof-tiles-red-wood-angle4.png")),
                                new Texture(ShapeList.SLOPE_ANGLE5, PathManager.loadImage("roofTilesRed/roof-tiles-red-angle5.png")),
                                new Texture(ShapeList.SLOPE_ANGLE5, PathManager.loadImage("roofTilesRed/roof-tiles-red-wood-angle5.png")),
                                new Texture(ShapeList.SLOPE_ANGLE6, PathManager.loadImage("roofTilesRed/roof-tiles-red-angle6.png")),
                                new Texture(ShapeList.SLOPE_ANGLE7, PathManager.loadImage("roofTilesRed/roof-tiles-red-angle7.png")),
                                new Texture(ShapeList.SLOPE_ANGLE8, PathManager.loadImage("roofTilesRed/roof-tiles-red-angle8.png"))
                         },
                        new Collision[] { CollisionList.STAIR1, CollisionList.STAIR2, CollisionList.STAIR3, CollisionList.STAIR4,
                                CollisionList.STAIR_ANGLE1, CollisionList.STAIR_ANGLE1, CollisionList.STAIR_ANGLE2, CollisionList.STAIR_ANGLE2, CollisionList.STAIR_ANGLE3, CollisionList.STAIR_ANGLE4, CollisionList.STAIR_ANGLE4,
                                CollisionList.STAIR_ANGLE5, CollisionList.STAIR_ANGLE5, CollisionList.STAIR_ANGLE6, CollisionList.STAIR_ANGLE7, CollisionList.STAIR_ANGLE8
                         },
                        null,
                        null)),

        HOLDER_ROPE(new BlockType("holderRope",
                        new Texture[] { new Texture(ShapeList.HOLDER, PathManager.loadImage("holders/holder.png")),
                                new Texture(ShapeList.HOLDER_ROPE1, PathManager.loadImage("holders/holder-rope1.png")),
                                new Texture(ShapeList.HOLDER_ROPE2, PathManager.loadImage("holders/holder-rope2.png")),
                                new Texture(ShapeList.HOLDER_ROPE3, PathManager.loadImage("holders/holder-rope3.png")),
                        },
                        new Collision[] { CollisionList.POLE, CollisionList.POLE, CollisionList.POLE, CollisionList.POLE },
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
        
        HERBS(new BlockType("herbs",
                        new Texture[] { new Texture(ShapeList.GRASS_SMALL, PathManager.loadImage("herbs/grass-small.png")),
                                new Texture(ShapeList.GRASS_BIG, PathManager.loadImage("herbs/grass-big.png")),
                                new Texture(ShapeList.FLOWERS, PathManager.loadImage("herbs/flowers-1.png")),
                                new Texture(ShapeList.FLOWERS, PathManager.loadImage("herbs/flowers-2.png")),
                                new Texture(ShapeList.FLOWERS, PathManager.loadImage("herbs/flowers-3.png")),
                                new Texture(ShapeList.FLOWERS, PathManager.loadImage("herbs/flowers-4.png")),
                                new Texture(ShapeList.FLOWERS, PathManager.loadImage("herbs/flowers-5.png")),
                                new Texture(ShapeList.FLOWERS, PathManager.loadImage("herbs/flowers-6.png")),
                        },
                        null,
                        new Property[] { new Property(PropertyList.NO_COLLISION), new Property("floor") },
                        null)),
        
        LIAN(new BlockType("lian",
                        new Texture[] { new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-left.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-right.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-flower-left-1.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-flower-right-1.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-flower-left-2.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-flower-right-2.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-flower-left-3.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-flower-right-3.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-flower-left-4.png")),
                                        new Texture(ShapeList.INSIDE, PathManager.loadImage("lian/lian-flower-right-4.png")),
                                },
                        null,
                        new Property[] {new Property(PropertyList.NO_COLLISION), new Property(PropertyList.BURNABLE)},
                        null)),
        
        WIND(new BlockType("wind",
                        new Texture[] {},
                        new Property[] { new Property(PropertyList.NO_COLLISION) },
                        new BlockBehavior[] {
                                        new BlockBehaviorApplyForce(1,0,0) })),
        
        POT(new BlockType("pot",
                        new Texture[] { new Texture(ShapeList.ALL, PathManager.loadImage("accessories/pot-flower.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("accessories/pot.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("accessories/pot-small.png")),
                                },
                        new Collision[] { CollisionList.SMALL_CUBE_BOTTOM },
                        null,
                        null)),
        TOOL(new BlockType("tool",
                        new Texture[] { new Texture(ShapeList.ALL, PathManager.loadImage("rope.png")),
                                },
                        new Collision[] { CollisionList.SLAB_BOTTOM },
                        null,
                        null)),
        CANDLE(new BlockType("candle",
                        new Texture[] { new Texture(ShapeList.ALL, PathManager.loadImage("candle/candle-off.png")),
                                Texture.createBasicTexture(ShapeList.ALL,
                                                                new String[] { "candle/candle-on-0.png", "candle/candle-on-1.png", "candle/candle-on-0.png", "candle/candle-on-2.png" }, 
                                                                3),
                                new Texture(ShapeList.ALL, PathManager.loadImage("candle/candlestick/candlestick-left-off.png")),
                                Texture.createBasicTexture(ShapeList.ALL,
                                                                new String[] { "candle/candlestick/candlestick-left-on-0.png", "candle/candlestick/candlestick-left-on-1.png", "candle/candlestick/candlestick-left-on-0.png", "candle/candlestick/candlestick-left-on-2.png" }, 
                                                                3),
                                new Texture(ShapeList.ALL, PathManager.loadImage("candle/candlestick/candlestick-right-off.png")),
                                Texture.createBasicTexture(ShapeList.ALL,
                                                                new String[] { "candle/candlestick/candlestick-right-on-0.png", "candle/candlestick/candlestick-right-on-1.png", "candle/candlestick/candlestick-right-on-0.png", "candle/candlestick/candlestick-right-on-2.png" }, 
                                                                3),
                        },
                        new Collision[] { CollisionList.SMALL_CUBE_BOTTOM },
                        new Property[] { new PropertyLight(
                                        new LightSource(new ColorRGB(1, 0.6, 0.2), 0.6, 0.7, 0.1)), },
                        new BlockBehavior[] { new BlockBehaviorSwitchTexture(), 
                                new BlockBehaviorLever(), 
                                new BlockBehaviorActivableProperty(PropertyLight.NAME),
                                new BlockBehaviorActivableSound(SoundType.CANDLE_OFF, SoundType.CANDLE_ON) 
                        })),
        TABLE_GLASS(new BlockType("tableGlass",
                        new Texture[] { new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass1.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass2.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass3.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass4.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass5.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass6.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass7.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass8.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass9.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass10.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass11.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass12.png")),
                                        new Texture(ShapeList.ALL, PathManager.loadImage("glass/glass13.png")),
                                },
                        new Collision[] { CollisionList.SMALL_CUBE_BOTTOM},
                        null,
                        null)),
        PLATE(new BlockType("plate",
                        new Texture[] { 
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate0.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate1.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate2.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate3.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate4.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate5.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate6.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("plate/plate7.png")),
                                        
                                },
                        new Collision[] { CollisionList.RECTANGLE1 },
                        null,
                        null)),
        BOTTLE(new BlockType("bottle",
                        new Texture[] {
                                new Texture(ShapeList.ALL, PathManager.loadImage("bottle/bottle.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("bottle/bottle1.png")),
                                new Texture(ShapeList.ALL, PathManager.loadImage("bottle/bottle2.png")),
                                },
                        new Collision[] { CollisionList.SMALL_CUBE_BOTTOM },
                        null,
                        null)),
        TABLE(new BlockType("table",
                        new Texture[] {
                                new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("table/table02.png")),
                                new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("table/table03.png")),
                                new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("table/table04.png")),
                                new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("table/table05.png")),
                                new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("table/table06.png")),
                                new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("table/table07.png")),
                                new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("table/table08.png")),
                                new Texture(ShapeList.TRANSPARENT_CUBE, PathManager.loadImage("table/table09.png")),

                               
                                },
                       null,
                        null,
                        null)),
        BRIDGE(new BlockType("bridge",
                        new Texture[] {
                                new Texture(ShapeList.SLAB_TOP, PathManager.loadImage("bridge/bridge-left.png")),
                                new Texture(ShapeList.SLAB_TOP, PathManager.loadImage("bridge/bridge-right.png")),
                                },
                        new Collision[] { CollisionList.SLAB_TOP },
                        null,
                        null)),
        
        
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
