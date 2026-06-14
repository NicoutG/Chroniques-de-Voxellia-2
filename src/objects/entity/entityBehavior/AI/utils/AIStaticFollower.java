package objects.entity.entityBehavior.AI.utils;

import audio.ISoundType;
import objects.entity.Entity;
import tools.PathFinding.*;
import tools.PetriNet.*;
import tools.PetriNet.States.*;
import tools.PetriNet.Transitions.*;
import world.World;

public abstract class AIStaticFollower extends AI {
    public AIStaticFollower(World world, Entity entity, PathFindingType pathFindingType, 
        double detectionDistance, double safeDistance, ISoundType followingSound
    ) {

        // State 0: Nothing
        PetriNetBase state0 = new PetriNetState();
        addState(state0);

        // State 1: Follow
        PathChooser followPathChooser = new PathChooser(pathFindingType);
        PetriNetBase state1 = new PetriNetMoveDynamicDestination(world, entity, followPathChooser, () -> world.getPlayer().getPosition());
        addState(state1);

        // Transitions
        TransitionBase nothingToFollowTransition = new Transition(() -> detectionDistance >= (world.getPlayer().getPosition().sub(entity.getPosition()).getNorm()));
        addEdge(0, 1, nothingToFollowTransition);
        nothingToFollowTransition.addAction(() -> {
            entity.stopSound(followingSound);
            entity.playSound(followingSound);
        });

        TransitionBase followToNothingTransition = new Transition(() -> safeDistance < (world.getPlayer().getPosition().sub(entity.getPosition()).getNorm()));
        addEdge(1, 0, followToNothingTransition);
        followToNothingTransition.addAction(() -> {
            entity.stopSound(followingSound);
        });
    }
}
