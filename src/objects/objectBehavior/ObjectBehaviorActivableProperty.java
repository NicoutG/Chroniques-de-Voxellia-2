package objects.objectBehavior;

import objects.ObjectInstance;
import objects.ObjectType;
import tools.Vector;
import world.World;

public class ObjectBehaviorActivableProperty<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
    > extends ObjectBehaviorActivable<T,I,B> {

    public final String PROPERTY;

    public ObjectBehaviorActivableProperty(String property) {
        PROPERTY = property;
    }

    public void onStart(World world, I objectInstance, Vector position) {
        int network = getNetwork(objectInstance);
        onStart(world, objectInstance, position, () -> activate(world, objectInstance, position, network), () -> desactivate(world, objectInstance, position, network));
    }

    private void activate(World world, I objectInstance, Vector position, int network) {
        objectInstance.setState(PROPERTY, true);
    }

    private void desactivate(World world, I objectInstance, Vector position, int network) {
        objectInstance.setState(PROPERTY, false);
    }

    @Override
    public void onAttachTo(I objectInstance) {
        super.onAttachTo(objectInstance);
        objectInstance.setState(PROPERTY, false);
    }

    public void onActivated(World world, I objectInstance, Vector position, int network) {
        onActivated(world, objectInstance, position, network, () -> activate(world, objectInstance, position, network));
    }

    public void onDesactivated(World world, I objectInstance, Vector position, int network) {
        onDesactivated(world, objectInstance, position, network, () -> desactivate(world, objectInstance, position, network));
    }
}
