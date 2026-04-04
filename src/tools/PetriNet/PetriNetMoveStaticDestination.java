package tools.PetriNet;

import java.util.function.Supplier;

import objects.entity.Entity;
import tools.Vector;
import tools.PathFinding.*;
import tools.PetriNet.States.*;
import world.World;

public class PetriNetMoveStaticDestination extends PetriNetState {
    private PathChooser pathChooser;

    public PetriNetMoveStaticDestination(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier) {
        super(() -> moveToDestination(world, entity, pathChooser, destinationSupplier));
        this.pathChooser = pathChooser;
    }

    public PathChooser getPathChooser() {
        return pathChooser;
    }

    private static void moveToDestination(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier) {
        PathState pathState = pathChooser.getState();
        if (pathState == PathState.REACHED || pathState == PathState.FAILED || pathState == PathState.NO_PATH) {
            Vector nextPosition = destinationSupplier.get();
            pathChooser.setDestination(world, entity, nextPosition);
        }
        pathChooser.updatePath(world, entity);
        pathChooser.moveToDestination(world, entity);
    }
}
