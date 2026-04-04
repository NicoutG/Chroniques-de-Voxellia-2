package tools.PetriNet.Transitions;

import java.util.ArrayList;
import java.util.List;

public abstract class TransitionBase {
    private List<Runnable> actions = new ArrayList<Runnable>();
    
    public boolean isEnabled() {
        if (condition()) {
            for (Runnable action : actions)
                action.run();
            return true;
        }
        return false;
    }

    protected abstract boolean condition();

    public void addAction(Runnable action) {
        this.actions.add(action);
    }
}
