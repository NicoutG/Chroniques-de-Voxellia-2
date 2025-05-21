package objects.entity;

import graphics.Texture;
import graphics.ligth.*;
import graphics.shape.*;
import objects.entity.entityBehavior.*;
import objects.collision.*;
import objects.property.*;
import tools.PathManager;

public enum EntityTemplate {
    /* 0 */
    PLAYER(new EntityType("player", 
        "blue-block.png",
        new Collision[] {CollisionList.BLOCK_ENTITY},
        null,
        null
        )),
    CRATE_WOOD(new EntityType("crateWood", 
        "crate-wood.png",
        new Collision[] {CollisionList.BLOCK_ENTITY},
        null,
        new EntityBehavior[] {new EntityBehaviorPushable()}
        )),

    ;

    public final EntityType entityType;

    EntityTemplate(EntityType entityType) {
        this.entityType = entityType;
    }

    public static String toString(EntityTemplate[] entityTypes) {
        String res = "";
        for (int i = 0; i < entityTypes.length; i++)
            res += i + " " + entityTypes[i].entityType.getName()+"\n";
        return res;
    }
}
