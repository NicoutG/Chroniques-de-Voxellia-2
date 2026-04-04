package tools.PetriNet;

import java.util.function.Supplier;

import objects.entity.Entity;
import tools.Vector;
import tools.PathFinding.*;
import tools.PetriNet.States.*;
import tools.PetriNet.Transitions.*;
import world.World;

public class PetriNetMoveRandomly extends PetriNet {
    private PathChooser pathChooser;

    public PetriNetMoveRandomly(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier, int minTickWait, int maxTickWait, int minTickMove, int maxTickMove) {
        this.pathChooser = pathChooser;

        // State 0: move randomly
        PetriNetState state0 = new PetriNetMoveStaticDestination(world, entity, pathChooser, destinationSupplier);
        addState(state0);

        // State 1: do nothing
        addState(new PetriNetState());

        // Transitions
        addEdge(0, 1, new Transition(() -> randomTrueBeetween(minTickMove, maxTickMove)));

        addEdge(1, 0, new Transition(() -> randomTrueBeetween(minTickMove, maxTickMove)));
    }

    public PathChooser getPathChooser() {
        return pathChooser;
    }
}
