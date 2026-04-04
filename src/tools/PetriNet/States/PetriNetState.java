package tools.PetriNet.States;

public class PetriNetState extends PetriNetBase {

    public PetriNetState() {}

    public PetriNetState(Runnable ... actions) {
        for (Runnable action : actions)
            addAction(action);
    }

    public int getCurrentStateId() {
        return 0;
    }

    public PetriNetBase getCurrentState() {
        return this;
    }

    protected void transit() {}

    protected void restart() {}
}
