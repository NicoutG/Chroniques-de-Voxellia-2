package tools.PetriNet;

import tools.PetriNet.States.*;
import tools.PetriNet.Transitions.*;

public class PetriNetTest extends PetriNet {

    public PetriNetTest() {
        PetriNet state0 = createSubPetriNet();
        addState(state0);
        PetriNet state1 = createSubPetriNet();
        state1.setResetOnRestart(false);
        addState(state1);

        addEdge(0, 1, new Transition(() -> 6 <= getTick()));
        addEdge(1, 0, new Transition(() -> 3 <= getTick()));
    }

    private PetriNet createSubPetriNet() {
        PetriNet subPetriNet = new PetriNet();
        PetriNetState state0 = new PetriNetState();
        state0.addAction(() -> System.out.println(getCurrentStateId()+"."+subPetriNet.getCurrentStateId()));
        subPetriNet.addState(state0);
        PetriNetState state1 = new PetriNetState();
        state1.addAction(() -> System.out.println(getCurrentStateId()+"."+subPetriNet.getCurrentStateId()));
        subPetriNet.addState(state1);
        PetriNetState state2 = new PetriNetState();
        state2.addAction(() -> System.out.println(getCurrentStateId()+"."+subPetriNet.getCurrentStateId()));
        subPetriNet.addState(state2);

        subPetriNet.addEdge(0, 1, 1, new ProbaTransition(0.2));
        subPetriNet.addEdge(0, 2, 0, new ProbaTransition());

        subPetriNet.addEdge(1, 0, new Transition(() -> 5 <= subPetriNet.getTick()));

        subPetriNet.addEdge(2, 1, new Transition(() -> 2 <= subPetriNet.getTick()));

        return subPetriNet;
    }

    public static void main(String[] args) {
        PetriNetTest test = new PetriNetTest();
        for (int i = 0; i < 100; i++) {
            System.out.println("Tick "+i);
            test.tick();
        }
    }
}
