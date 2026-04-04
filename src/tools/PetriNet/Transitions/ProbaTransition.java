package tools.PetriNet.Transitions;

import java.util.Random;

public class ProbaTransition extends TransitionBase {
    private double proba = 1;
    private static Random random = new Random();

    public ProbaTransition() {

    }

    public ProbaTransition(double proba) {
        this.proba = proba;
    }
    
    protected boolean condition() {
        if (1 <= proba)
            return true;
        return (random.nextDouble() <= proba);
    }
}
