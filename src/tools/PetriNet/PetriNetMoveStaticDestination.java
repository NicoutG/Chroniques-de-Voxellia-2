package tools.PetriNet;

import java.util.function.Supplier;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.Vector;
import tools.PathFinding.*;
import world.World;

public class PetriNetMoveStaticDestination extends PetriNetMoveBase {

    public PetriNetMoveStaticDestination(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier) {
        super(world, entity, pathChooser, destinationSupplier);
    }

    protected void restart() {
        super.restart();
        getPathChooser().resetSearch();
    }

    protected EntityAction[] chooseActions(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier) {
        PathState pathState = pathChooser.getState();
        if (pathState == PathState.REACHED || pathState == PathState.FAILED || pathState == PathState.NO_PATH) {
            Vector nextPosition = destinationSupplier.get();
            pathChooser.setDestination(world, entity, nextPosition);
        }
        pathChooser.updatePath(world, entity);
        return pathChooser.moveToDestination(world, entity);
    }
}
