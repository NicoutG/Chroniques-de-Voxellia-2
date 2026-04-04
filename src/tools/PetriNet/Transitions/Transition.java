package tools.PetriNet.Transitions;

import java.util.function.BooleanSupplier;

public class Transition extends TransitionBase {
    private final BooleanSupplier condition;

    public Transition(BooleanSupplier condition) {
        this.condition = condition;
    }

    protected boolean condition() {
        return condition.getAsBoolean();
    }
}
