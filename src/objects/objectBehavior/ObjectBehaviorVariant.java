package objects.objectBehavior;

import objects.ObjectInstance;
import objects.ObjectType;

public class ObjectBehaviorVariant<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
    > extends ObjectBehavior<T,I,B> {
    
    protected final static String TYPE = "type";

    @Override
    public void onAttachTo(I objectInstance) {
        objectInstance.setState(TYPE, 0);
    }

    public void onStart(I instance) {
        int type = getType(instance);
        instance.setIndexTexture(type);
        instance.setIndexCollision(type);
    }

    public int getType(I objectInstance) {
        Object state = objectInstance.getState(TYPE);
        if (state != null && state instanceof Integer)
            return (int)state;
        return 0;
    }
    
}
