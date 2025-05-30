package objects.entity;

import java.awt.image.BufferedImage;

import graphics.Texture;
import graphics.ligth.ColorRGB;
import graphics.ligth.LightSource;
import graphics.shape.PlayerShape;
import objects.entity.entityBehavior.*;
import objects.property.*;
import tools.PathManager;
import objects.collision.*;

public enum EntityTemplate {
    /* 0 */
    PLAYER(new EntityType("player",
            new Texture[] {
                    new Texture(
                            new PlayerShape(), new BufferedImage[] {
                                    PathManager.loadImage("player/right/player-right-0.png"),
                                    PathManager.loadImage("player/right/player-right-1.png"),
                                    PathManager.loadImage("player/right/player-right-2.png"),
                                    PathManager.loadImage("player/right/player-right-3.png")
                            }, 1)
            },
            new Collision[] { CollisionList.BLOCK_ENTITY },
            null,
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable(), new EntityBehaviorPlayer() })),
    CRATE_WOOD(new EntityType("crateWood",
            "crate-wood.png",
            new Collision[] { CollisionList.BLOCK_ENTITY },
            null,
            new EntityBehavior[] { new EntityBehaviorApplyForce(), new EntityBehaviorPushable() })),
    MOVING_BLOCK(new EntityType("movingBlock",
            "block.png",
            new Collision[] { CollisionList.CUBE },
            new Property[] { new Property("noCollisionSame"), new Property("noCollisionBlock")},
            new EntityBehavior[] { new EntityBehaviorActivableMoving() })),

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
}
