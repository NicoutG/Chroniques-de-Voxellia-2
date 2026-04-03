package tools.PetriNet.Transitions;

import java.util.Random;

public class ProbaTransition implements ITransition {
    private double proba = 1;
    private static Random random = new Random();

    public ProbaTransition() {

    }

    public ProbaTransition(double proba) {
        this.proba = proba;
    }
    
    public boolean isEnabled() {
        if (1 <= proba)
            return true;
        return (random.nextDouble() <= proba);
    }
}
