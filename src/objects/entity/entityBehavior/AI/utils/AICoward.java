package objects.entity.entityBehavior.AI.utils;

import objects.entity.Entity;
import tools.PathFinding.*;
import tools.PetriNet.*;
import tools.PetriNet.States.*;
import tools.PetriNet.Transitions.*;
import world.World;

public abstract class AICoward extends AI {
    public AICoward(World world, Entity entity, PathFindingType pathFindingType, double speedMultiplier, double detectionDistance, double safeDistance, int minTickWait, int maxTickWait, int minTickMove, int maxTickMove) {
        DestinationChooser destinationChooser = new DestinationChooser(pathFindingType);

        // State 0: move randomly
        PathChooser randomPathChooser = new PathChooser(pathFindingType);
        PetriNetBase state0 = new PetriNetMoveRandomly(world, entity, randomPathChooser, () -> destinationChooser.chooseRandomPosition(world, entity, 5, 10), minTickWait, maxTickWait, minTickMove, maxTickMove);
        addState(state0);

        // State 1: flee from player
        PathChooser fleePathChooser = new PathChooser(pathFindingType);
        PetriNetBase state1 = new PetriNetMoveStaticDestination(world, entity, fleePathChooser, () -> destinationChooser.chooseFurthestPosition(world, entity, world.getPlayer(), 5, 10));
        addState(state1);

        // Transitions
        TransitionBase toFleeTransition = new Transition(() -> detectionDistance >= (world.getPlayer().getPosition().sub(entity.getPosition()).getNorm()));
        toFleeTransition.addAction(() -> entity.setSpeed(entity.getSpeed() * speedMultiplier));
        addEdge(0, 1, toFleeTransition);

        TransitionBase toRandomTransition = new Transition(() -> safeDistance < (world.getPlayer().getPosition().sub(entity.getPosition()).getNorm()));
        toRandomTransition.addAction(() -> entity.setSpeed(entity.getSpeed() / speedMultiplier));
        addEdge(1, 0, toRandomTransition);
    }
}
