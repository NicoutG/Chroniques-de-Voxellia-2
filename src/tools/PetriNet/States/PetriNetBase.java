package tools.PetriNet.States;

import java.util.Random;

public abstract class PetriNetBase {
    protected static Random random = new Random();

    public abstract int getCurrentStateId();

    public abstract PetriNetBase getCurrentState();

    protected abstract void transit();

    protected abstract void act();

    protected abstract void restart();
}
