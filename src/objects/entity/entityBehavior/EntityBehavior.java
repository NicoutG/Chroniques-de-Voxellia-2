package objects.entity.entityBehavior;

import objects.entity.Entity;
import objects.entity.EntityType;
import objects.objectBehavior.ObjectBehavior;
import tools.Vector;
import world.World;

public abstract class EntityBehavior extends ObjectBehavior<EntityType, Entity, EntityBehavior>{

    public void onAttachTo(Entity entity) {
    }

    public void onStart(World world,Entity entity) {
    }

    public void onUpdate(World world, Entity entity) {
    }

    public void onInteraction(World world, Entity entity, Entity entityInteract) {
    }

    public void onPush(World world, Entity entity, Vector move, Entity entityPush) {
    }

    public EntityBehavior clone() {
        return this;
    }


}
