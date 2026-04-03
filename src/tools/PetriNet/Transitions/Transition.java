package tools.PetriNet.Transitions;

import java.util.function.BooleanSupplier;

public class Transition implements ITransition {
    private final BooleanSupplier condition;

    public Transition(BooleanSupplier condition) {
        this.condition = condition;
    }

    public boolean isEnabled() {
        return condition.getAsBoolean();
    }
}
