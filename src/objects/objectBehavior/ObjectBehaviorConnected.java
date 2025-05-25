package objects.objectBehavior;

import objects.ObjectInstance;
import objects.ObjectType;

public class ObjectBehaviorConnected<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
    > extends ObjectBehavior<T,I,B> {

    public final static String ACTIVATION_STATE = "activated";
    public final static String NETWORK = "network";

    @Override
    public void onAttachTo(I objectInstance) {
        objectInstance.setState(ACTIVATION_STATE, false);
        objectInstance.setState(NETWORK, 0);
    }

    public boolean getActivationState(I objectInstance) {
        Object state = objectInstance.getState(ACTIVATION_STATE);
        if (state != null && state instanceof Boolean)
            return (boolean)state;
        return false;
    }

    public int getNetwork(I objectInstance) {
        Object state = objectInstance.getState(NETWORK);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
