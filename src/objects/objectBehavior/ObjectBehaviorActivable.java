package objects.objectBehavior;

import objects.ObjectInstance;
import objects.ObjectType;
import tools.Vector;
import world.World;

public class ObjectBehaviorActivable<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
    > extends ObjectBehaviorConnected<T,I,B> {

    public void onStart(World world, I objectInstance, Vector position, Runnable activate, Runnable desactivate) {
        boolean activationState = getActivationState(objectInstance);
        objectInstance.setState(ACTIVATION_STATE, !activationState);
        if (activationState)
            activate.run();
        else
            desactivate.run();
        objectInstance.setState(ACTIVATION_STATE, activationState);
    }

    public void onActivated(World world, I objectInstance, Vector position, int network, Runnable activate) {
        if (network == getNetwork(objectInstance)) {
            activate.run();
            world.executeAfterUpdate(() -> objectInstance.setState(ACTIVATION_STATE, true));
        }
    }

    public void onDesactivated(World world, I objectInstance, Vector position, int network, Runnable desactivate) {
        if (network == getNetwork(objectInstance)) {
            desactivate.run();
            world.executeAfterUpdate(() -> objectInstance.setState(ACTIVATION_STATE, false));
        }
    }
}
