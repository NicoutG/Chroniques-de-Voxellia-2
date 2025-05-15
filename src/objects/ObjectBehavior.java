package objects;

@SuppressWarnings("rawtypes")
public abstract class ObjectBehavior<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
> {

    public abstract void onAttachTo(I instance);

    public abstract ObjectBehavior clone();
}
