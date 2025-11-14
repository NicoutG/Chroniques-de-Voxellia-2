package objects.objectBehavior;

import objects.ObjectInstance;
import objects.ObjectType;
import world.World;

public class ObjectBehaviorEffect<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
    > extends ObjectBehavior<T,I,B> {
    
    public int effectDuration = 0;

    public ObjectBehaviorEffect(int effectDuration) {
        this.effectDuration = effectDuration;
    }

    @Override
    public void onAttachTo(I objectInstance) {
        long tick0 = World.getTick();
        objectInstance.initTickFrame0(tick0);
    }
    
}
