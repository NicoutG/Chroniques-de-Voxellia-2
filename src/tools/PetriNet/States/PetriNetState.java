package tools.PetriNet.States;

public class PetriNetState extends PetriNetBase {
    private Runnable action = null;

    public PetriNetState() {}

    public PetriNetState(Runnable action) {
        setAction(action);
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public int getCurrentStateId() {
        return 0;
    }

    public PetriNetBase getCurrentState() {
        return this;
    }

    protected void transit() {}

    protected void act() {
        if (action != null)
            action.run();
    }

    protected void restart() {}
}
