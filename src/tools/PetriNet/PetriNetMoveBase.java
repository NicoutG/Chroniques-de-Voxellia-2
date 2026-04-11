package tools.PetriNet;

import java.util.function.Supplier;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.Vector;
import tools.PathFinding.PathChooser;
import tools.PetriNet.States.PetriNetState;
import world.World;

public abstract class PetriNetMoveBase extends PetriNetState {
    private PathChooser pathChooser;
    private EntityAction[] currentActions = new EntityAction[0];
    private int lastActionTick = Integer.MIN_VALUE;

    public PetriNetMoveBase(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier) {
        super();
        addAction(() -> moveToDestination(world, entity, pathChooser, destinationSupplier));
        this.pathChooser = pathChooser;
    }

    public PathChooser getPathChooser() {
        return pathChooser;
    }

    public EntityAction[] getCurrentActions() {
        return currentActions;
    }

    public int getLastActionTick() {
        return lastActionTick;
    }

    private void moveToDestination(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier) {
        EntityAction[] actions = chooseActions(world, entity, pathChooser, destinationSupplier);
        currentActions = actions;
        if (actions.length > 0) 
            lastActionTick = World.getTick();
    }

    protected abstract EntityAction[] chooseActions(World world, Entity entity, PathChooser pathChooser, Supplier<Vector> destinationSupplier);
}
