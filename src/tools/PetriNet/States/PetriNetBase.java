package tools.PetriNet.States;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class PetriNetBase {
    protected static Random random = new Random();
    private List<Runnable> actions = new ArrayList<Runnable>();

    public abstract int getCurrentStateId();

    public abstract PetriNetBase getCurrentState();

    public void addAction(Runnable action) {
        this.actions.add(action);
    }

    protected abstract void transit();

    protected void act() {
        for (Runnable action : actions)
            action.run();
    }

    protected abstract void restart();
}
