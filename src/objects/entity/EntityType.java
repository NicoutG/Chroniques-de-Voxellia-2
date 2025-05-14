package objects.entity;

import objects.ObjectType;

public class EntityType extends ObjectType{

    public EntityType(String name) {
        super(name);
    }

    @SuppressWarnings("unchecked")
    public Entity getInstance() {
        return new Entity(this,0,0,0);
    }
}
