package objects.entity;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import audio.SoundType;
import graphics.Texture;
import graphics.ligth.ColorRGB;
import graphics.ligth.LightSource;
import graphics.shape.ShapeList;
import objects.entity.entityBehavior.*;
import objects.entity.entityBehavior.AI.*;
import objects.property.*;
import tools.PathManager;
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
    FOX(
        apply(
        new EntityType("fox",
            new Texture[] {
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("fox/fox-rest-0.png"),
                                        PathManager.loadImage("fox/fox-rest-1.png"),
                                        PathManager.loadImage("fox/fox-rest-2.png"),
                                        PathManager.loadImage("fox/fox-rest-3.png"),
                                }, 6),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("fox/fox-left-0.png"),
                                        PathManager.loadImage("fox/fox-left-1.png"),
                                        PathManager.loadImage("fox/fox-left-2.png"),
                                        PathManager.loadImage("fox/fox-left-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("fox/fox-right-0.png"),
                                        PathManager.loadImage("fox/fox-right-1.png"),
                                        PathManager.loadImage("fox/fox-right-2.png"),
                                        PathManager.loadImage("fox/fox-right-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("fox/fox-top-0.png"),
                                        PathManager.loadImage("fox/fox-top-1.png"),
                                        PathManager.loadImage("fox/fox-top-2.png"),
                                        PathManager.loadImage("fox/fox-top-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("fox/fox-bottom-0.png"),
                                        PathManager.loadImage("fox/fox-bottom-1.png"),
                                        PathManager.loadImage("fox/fox-bottom-2.png"),
                                        PathManager.loadImage("fox/fox-bottom-3.png"),
                                }, 3),
            },
            new Collision[] { CollisionList.BLOCK_ENTITY },
            new Property[] { 
                new PropertySound(SoundType.FOX, 50, 300),
            },
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable(), new EntityBehaviorExecuteAI((world, entity) -> new AIFox(world, entity)),
            }),
            entity -> {
                entity.setSpeed(0.21);
            }
        )),
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
            new EntityBehavior[] { new EntityBehaviorPushable(), new EntityBehaviorExecuteAI((world, entity) -> new AISlime(world, entity)) })),
    CHICKEN(
        apply(
        new EntityType("chicken",
            new Texture[] {
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("chicken/chicken-rest-0.png"),
                                        PathManager.loadImage("chicken/chicken-rest-1.png"),
                                        PathManager.loadImage("chicken/chicken-rest-2.png"),
                                        PathManager.loadImage("chicken/chicken-rest-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("chicken/chicken-left-0.png"),
                                        PathManager.loadImage("chicken/chicken-left-1.png"),
                                        PathManager.loadImage("chicken/chicken-left-2.png"),
                                        PathManager.loadImage("chicken/chicken-left-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("chicken/chicken-right-0.png"),
                                        PathManager.loadImage("chicken/chicken-right-1.png"),
                                        PathManager.loadImage("chicken/chicken-right-2.png"),
                                        PathManager.loadImage("chicken/chicken-right-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("chicken/chicken-top-0.png"),
                                        PathManager.loadImage("chicken/chicken-top-1.png"),
                                        PathManager.loadImage("chicken/chicken-top-2.png"),
                                        PathManager.loadImage("chicken/chicken-top-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("chicken/chicken-bottom-0.png"),
                                        PathManager.loadImage("chicken/chicken-bottom-1.png"),
                                        PathManager.loadImage("chicken/chicken-bottom-2.png"),
                                        PathManager.loadImage("chicken/chicken-bottom-3.png"),
                                }, 3),
            },
            new Collision[] { CollisionList.BLOCK_ENTITY },
            new Property[] { 
                new PropertySound(SoundType.CHICKEN, 100, 300),
            },
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable(), new EntityBehaviorExecuteAI((world, entity) -> new AIChicken(world, entity)),
            }),
            entity -> {
                entity.setSpeed(0.08);
            }
        )),
   BALL_PROJECTILE(new EntityType("ballProjectile",
            new Texture[] { new Texture(ShapeList.ALL, PathManager.loadImage("projectile/ball.png")) },
            new Collision[] { CollisionList.SMALL_CUBE },
            new Property[] {
                new PropertyLight(new LightSource(new ColorRGB(0.2, 0.2, 0.2), 0.5, 0.8, 0))},
            new EntityBehavior[] { new EntityBehaviorConstantVelocity(), new EntityBehaviorKill(), new EntityBehaviorFragile() }
        )),
   EARTH_DEFENSER(
        apply(
        new EntityType("earthDefenser",
            new Texture[] {
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-rest-0.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-rest-1.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-rest-0.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-rest-2.png"),
                                }, 10),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-left-0.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-left-1.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-left-2.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-left-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-right-0.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-right-1.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-right-2.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-right-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-top-0.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-top-1.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-top-2.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-top-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-bottom-0.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-bottom-1.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-bottom-2.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-bottom-3.png"),
                                }, 3),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-left-0.png"),
                                }, 10),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-charge-right-0.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-charge-right-1.png"),
                                }, 10),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-top-0.png"),
                                }, 10),
                        new Texture(
                                ShapeList.ALL, new BufferedImage[] {
                                        PathManager.loadImage("earthDefenser/earthDefenser-charge-bottom-0.png"),
                                        PathManager.loadImage("earthDefenser/earthDefenser-charge-bottom-1.png"),
                                }, 10),
            },
            new Collision[] { CollisionList.BLOCK_ENTITY },
            new Property[] { 
                new PropertySound(SoundType.CHICKEN, 100, 300),
            },
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable(), new EntityBehaviorExecuteAI((world, entity) -> new AIEarthDefenser(world, entity)),
            }),
            entity -> {
                entity.setSpeed(0.08);
            }
        )),

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
