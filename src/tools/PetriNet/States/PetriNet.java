package tools.PetriNet.States;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

import tools.PetriNet.Transitions.TransitionBase;

public class PetriNet extends PetriNetBase {
    private int currentStateId = 0;
    private int currentStateTick = 0;
    private IntSupplier restartStateId;
    private BooleanSupplier resetOnRestart;
    private List<PetriNetBase> states = new ArrayList<PetriNetBase>();
    private HashMap<Integer, List<Edge>> edges = new HashMap<Integer, List<Edge>>();

    public PetriNet(Runnable ... actions) {
        setRestartStateId(0);
        setResetOnRestart(true);
        for (Runnable a : actions)
            addAction(a);
    }
    
    public PetriNet() {
        setRestartStateId(0);
        setResetOnRestart(true);
    }

    public void setResetOnRestart(boolean resetOnRestart) {
        this.resetOnRestart = () -> resetOnRestart;
    }

    public void setResetOnRestart(BooleanSupplier resetOnRestart) {
        this.resetOnRestart = resetOnRestart;
    }

    public void setRestartStateId(int restartStateId) {
        this.restartStateId = () -> restartStateId;
    }

    public void setRestartStateId(IntSupplier restartStateId) {
        this.restartStateId = restartStateId;
    }

    public int getCurrentStateId() {
        return currentStateId;
    }

    public PetriNetBase getCurrentState() {
        verifyStateId(currentStateId);
        return states.get(currentStateId);
    }

    protected void restart() {
        if (!resetOnRestart.getAsBoolean())
            return;

        currentStateId = restartStateId.getAsInt();
        currentStateTick = 0;

        getCurrentState().restart();
    }

    protected void transit() {
        getCurrentState().transit();
        List<Edge> nextEdges = getSortedEdges(currentStateId);
        for (Edge edge : nextEdges)
            if (edge.transition.isEnabled()) {
                currentStateId = edge.destination;
                currentStateTick = 0;
                states.get(currentStateId).restart();
                return;
            }
    }

    protected void act() {
        currentStateTick++;
        getCurrentState().act();
        super.act();
    }

    public int getTick() {
        return currentStateTick;
    }

    public int getNbStates() {
        return states.size();
    }

    public void addState(PetriNetBase state) {
        states.add(state);
    }

    public void addEdge(int start, int end, int priority, TransitionBase transition) {
        verifyStateId(start);
        verifyStateId(end);
        edges
            .computeIfAbsent(start, k -> new ArrayList<>())
            .add(new Edge(transition, end, priority));
    }

    public void addEdge(int start, int end, TransitionBase transition) {
        addEdge(start, end, 0, transition);
    }

    public void tick() {
        if (0 < getTick())
            transit();
        act();
    }

    protected boolean randomTrueBeetween(int minTick, int maxTick) {
        int tick = getTick();
        if (tick < minTick)
            return false;
        if (tick >= maxTick)
            return true;
        double proba = 1.0 / (maxTick - tick + 1);
        return (random.nextDouble() <= proba);
    }

    private List<Edge> getSortedEdges(int source) {
        List<Edge> list = edges.get(source);
        if (list == null || list.isEmpty())
            return List.of();

        Map<Integer, List<Edge>> byPriority = new HashMap<>();
        for (Edge e : list) {
            byPriority
                .computeIfAbsent(e.priority, k -> new ArrayList<>())
                .add(e);
        }

        List<Integer> priorities = new ArrayList<>(byPriority.keySet());
        priorities.sort((a, b) -> Integer.compare(b, a));

        List<Edge> result = new ArrayList<>();

        for (int p : priorities) {
            List<Edge> group = new ArrayList<>(byPriority.get(p));
            Collections.shuffle(group);
            result.addAll(group);
        }

        return result;
    }

    private void verifyStateId(int stateId) {
        if (stateId < 0 || getNbStates() <= stateId)
            throw new IllegalArgumentException("Error : stateId is "+stateId+" but only "+getNbStates()+" states");
    }

    class Edge {
        public TransitionBase transition;
        public int destination;
        public int priority;

        public Edge(TransitionBase transition, int destination, int priority) {
            this.transition = transition;
            this.destination = destination;
            this.priority = priority;
        }
    }
}
