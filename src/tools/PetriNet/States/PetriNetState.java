package tools.PetriNet.States;

import objects.entity.EntityAction;
import tools.PetriNet.IPetriNetExecuteActions;

public class PetriNetState extends PetriNetBase implements IPetriNetExecuteActions {

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

    public EntityAction[] getCurrentActions() {
        return new EntityAction[0];
    }

    protected void transit() {}

    protected void restart() {}
}
