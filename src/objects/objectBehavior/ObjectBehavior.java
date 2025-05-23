package objects.objectBehavior;

import objects.ObjectInstance;
import objects.ObjectType;

@SuppressWarnings("rawtypes")
public abstract class ObjectBehavior<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
> implements Cloneable{

    public abstract void onAttachTo(I instance);

    @SuppressWarnings("unchecked")
    public B clone() {
		B clone = null;
		try {
			clone = (B)super.clone();
		} catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		return clone;
	}
}
