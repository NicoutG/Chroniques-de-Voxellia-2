package tools.PetriNet;

import java.util.function.Supplier;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.Vector;
import tools.PathFinding.*;
import world.World;

public class PetriNetMoveDynamicDestination extends PetriNetMoveBase {

    public PetriNetMoveDynamicDestination(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier) {
        super(world, entity, pathChooser, destinationSupplier);
    }

    protected EntityAction[] chooseActions(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier) {
        PathState pathState = pathChooser.getState();
        if (pathState != PathState.SEARCHING) {
            Vector nextPosition = destinationSupplier.get();
            pathChooser.setDestination(world, entity, nextPosition);
        }
        pathChooser.updatePath(world, entity);
        return pathChooser.moveToDestination(world, entity);
    }
}
