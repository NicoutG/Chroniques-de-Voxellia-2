package objects.entity.entityBehavior;

import objects.ObjectBehavior;
import objects.entity.Entity;
import objects.entity.EntityType;

public abstract class EntityBehavior extends ObjectBehavior<EntityType, Entity, EntityBehavior>{

    public void onAttachTo(Entity entity) {
    }

    public EntityBehavior clone() {
        return this;
    }


}
