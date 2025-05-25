package objects.entity.entityBehavior;

import objects.entity.*;
import objects.objectBehavior.ObjectBehaviorConnected;

public class EntityBehaviorConnected extends EntityBehavior{
    public final static String ACTIVATION_STATE = ObjectBehaviorConnected.ACTIVATION_STATE;
    public final static String NETWORK = ObjectBehaviorConnected.NETWORK;

    private final ObjectBehaviorConnected<EntityType, Entity, EntityBehavior> commonBehavior = new ObjectBehaviorConnected<>();

    @Override
    public void onAttachTo(Entity entity) {
        commonBehavior.onAttachTo(entity);
    }

    public boolean getActivationState(Entity entity) {
        return commonBehavior.getActivationState(entity);
    }

    public int getNetwork(Entity entity) {
        return commonBehavior.getNetwork(entity);
    }
}
