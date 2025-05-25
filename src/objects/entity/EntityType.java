package objects.entity;

import graphics.Texture;
import objects.ObjectType;
import objects.collision.Collision;
import objects.entity.entityBehavior.EntityBehavior;
import objects.property.Property;

public class EntityType extends ObjectType<Entity, EntityBehavior>{

    public EntityType(String name, Texture[] textures, double opacity, Collision[] collisions, Property[] properties, EntityBehavior[] behaviors) {
        super(name, textures, opacity, collisions, properties, behaviors);
    }

    public EntityType(String name, Texture[] textures, Collision[] collisions, Property[] properties, EntityBehavior[] behaviors) {
        super(name,textures,collisions,properties,behaviors);
    }

    public EntityType(String name, String texturePath, Collision[] collisions, Property[] properties, EntityBehavior[] behaviors) {
        super(name,texturePath,collisions,properties,behaviors);
    }

    public EntityType(String name, Texture[] textures, Property[] properties, EntityBehavior[] behaviors) {
        super(name,textures,properties,behaviors);
    }

    public EntityType(String name, String texturePath, Property[] properties, EntityBehavior[] behaviors) {
        super(name, texturePath, properties, behaviors);
    }

    public EntityType(String name, Texture[] textures, EntityBehavior[] behaviors) {
        super(name, textures, behaviors);
    }

    public EntityType(String name, String texturePath, EntityBehavior[] behaviors) {
        super(name, texturePath, behaviors);
    }

    public EntityType(String name, String texturePath) {
        super(name, texturePath);
    }

    public EntityType(String name) {
        super(name);
    }

    public Entity getInstance() {
        return getInstance(0,0,0);
    }

    public Entity getInstance(double x, double y, double z) {
        Entity entity = new Entity(this,x,y,z);
        for (EntityBehavior entityBehavior : behaviors)
            entity.addBehavior(entityBehavior.clone());
        return entity;
    }

    public Player getInstancePlayer() {
        return getInstancePlayer(0, 0, 0);
    }

    public Player getInstancePlayer(double x, double y, double z) {
        Player entity = new Player(this,x,y,z);
        for (EntityBehavior entityBehavior : behaviors)
            entity.addBehavior(entityBehavior.clone());
        return entity;
    }
}
