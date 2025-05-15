package objects.entity;

import objects.ObjectBehavior;
import objects.ObjectType;

public class EntityType extends ObjectType<Entity, ObjectBehavior>{

    public EntityType(String name) {
        super(name);
    }

    public Entity getInstance() {
        return new Entity(this,0,0,0);
    }
}
