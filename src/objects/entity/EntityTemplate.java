package objects.entity;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import audio.SoundType;
import graphics.Texture;
import graphics.ligth.ColorRGB;
import graphics.ligth.LightSource;
import graphics.shape.ShapeList;
import objects.entity.entityBehavior.*;
import objects.entity.entityBehavior.pathFinding.*;
import objects.property.*;
import tools.PathManager;
import tools.PathFinding.*;
import objects.collision.*;

public enum EntityTemplate {
    /* 0 */
    PLAYER(new EntityType("player",
            new Texture[] {
                    new Texture(
                            ShapeList.PLAYER, new BufferedImage[] {
                                    PathManager.loadImage("player/right/player-right-0.png"),
                                    PathManager.loadImage("player/right/player-right-1.png"),
                                    PathManager.loadImage("player/right/player-right-2.png"),
                                    PathManager.loadImage("player/right/player-right-3.png")
                            }, 2)
            },
            new Collision[] { CollisionList.BLOCK_ENTITY },
            null,
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable() })),
    CRATE_WOOD(new EntityType("crateWood",
            new Texture[] { new Texture(ShapeList.CUBE, PathManager.loadImage("crate-wood.png")),
                new Texture(ShapeList.COLUMN, PathManager.loadImage("barrel.png"))
            },
            new Collision[] { CollisionList.BLOCK_ENTITY },
            new Property[] { new Property(PropertyList.BURNABLE) },
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable() })),
    CRATE_METAL(new EntityType("crateMetal",
            new Texture[] { new Texture(ShapeList.CUBE, PathManager.loadImage("crate-metal.png")),
                new Texture(ShapeList.COLUMN, PathManager.loadImage("barrel-metal.png"))
            },
            new Collision[] { CollisionList.BLOCK_ENTITY },
            null,
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable() })),
    FIRE(new EntityType("fire",
            new Texture[] { Texture.createBasicTexture(ShapeList.COMPLETE,
                        new String[] { "fire/fire-0.png", "fire/fire-1.png", "fire/fire-2.png", "fire/fire-3.png" }, 
                        3) },
            null,
            new Property[]{ new Property(PropertyList.NO_COLLISION_ENTITY),
                        new PropertySound(SoundType.FIRE),
                        new PropertyLight(
                                new LightSource(new ColorRGB(1, 0.6, 0.2), 0.5, 0.8, 0.1)) },
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorBlazable(), new EntityBehaviorEffect(100) })),
    MOVING_BLOCK(new EntityType("movingBlock",
            "block.png",
            new Collision[] { CollisionList.CUBE },
            new Property[] { new Property(PropertyList.NO_COLLISION_SAME), new Property(PropertyList.NO_COLLISION_BLOCK)},
            new EntityBehavior[] { new EntityBehaviorActivableMoving() })),
    MOVING_GRID(new EntityType("movingGrid",
            new Texture[] { new Texture(ShapeList.BORDER_LEFT, PathManager.loadImage("grids/grid-left.png")),
                                new Texture(ShapeList.BORDER_RIGHT, PathManager.loadImage("grids/grid-right.png")),
                                new Texture(ShapeList.BORDER_HORIZONTAL, PathManager.loadImage("grids/grid-horizontal.png")),
            },
            new Collision[] { CollisionList.BARRIER_LEFT, CollisionList.BARRIER_RIGHT, CollisionList.BARRIER_HORIZONTAL },
            new Property[] { new Property(PropertyList.NO_COLLISION_SAME), new Property(PropertyList.NO_COLLISION_BLOCK)},
            new EntityBehavior[] { new EntityBehaviorActivableMoving() })),
    FOLLOW_CRATE_WOOD(new EntityType("followCrateWood",
            "crate-wood.png",
            new Collision[] { CollisionList.BLOCK_ENTITY },
            null,
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable(), new EntityBehaviorMovingObserver(new PathFindingFalling()) })),
    FLYING_SLIME(new EntityType("flyingSlime",
            new Texture[] {
                    new Texture(
                            ShapeList.BUMP, new BufferedImage[] {
                                    PathManager.loadImage("slime/flying-slime-0.png"),
                                    PathManager.loadImage("slime/flying-slime-1.png"),
                                    PathManager.loadImage("slime/flying-slime-2.png"),
                            }, 1)
            },
            new Collision[] { CollisionList.BLOCK_ENTITY },
            new Property[] { 
                new PropertySound(SoundType.HELICOPTER) ,
                new PropertyLight(
                    new LightSource(new ColorRGB(0.2, 0.9, 0.2), 0.5, 0.8, 0))},
            new EntityBehavior[] { new EntityBehaviorPushable(), new EntityBehaviorMovingPassive(new PathFindingFly()) })),
    LILY(new EntityType("lily",
            "lily.png",
            new Collision[] { CollisionList.BLOCK_ENTITY },
            null,
            new EntityBehavior[] { new EntityBehaviorApplyForce() })),

    ;

    public final EntityType entityType;

    EntityTemplate(EntityType entityType) {
        this.entityType = entityType;
    }

    public static String toString(EntityTemplate[] entityTypes) {
        String res = "";
        for (int i = 0; i < entityTypes.length; i++)
            res += i + " " + entityTypes[i].entityType.getName() + "\n";
        return res;
    }

    public static <T> T apply(T obj, Consumer<T> fn) {
        fn.accept(obj);
        return obj;
    }
}
