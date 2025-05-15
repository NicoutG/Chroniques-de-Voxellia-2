package objects.entity;

import objects.ObjectType;
import objects.entity.entityBehavior.EntityBehavior;

public class EntityType extends ObjectType<Entity, EntityBehavior>{

    public EntityType(String name) {
        super(name);
    }

    public Entity getInstance() {
        Entity entity = new Entity(this,0,0,0);
        for (EntityBehavior entityBehavior : behaviors)
            entity.addBehavior(entityBehavior.clone());
        return entity;
    }
}
