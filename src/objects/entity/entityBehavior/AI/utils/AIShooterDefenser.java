package objects.entity.entityBehavior.AI.utils;

import audio.SoundType;
import objects.ObjectAnimation;
import objects.entity.Entity;
import tools.Vector;
import tools.PathFinding.*;
import tools.PetriNet.*;
import tools.PetriNet.States.*;
import tools.PetriNet.Transitions.*;
import world.World;

public abstract class AIShooterDefenser extends AI {
    public AIShooterDefenser(World world, Entity entity, PathFindingType pathFindingType, 
        double detectionDistance, double safeDistance, 
        int minTickWait, int maxTickWait, int minTickMove, int maxTickMove, 
        int loadShootDuration, int afterShootDuration,
        String projectileName, double projectileSpeed, SoundType shootSound,
        int leftLoadTexture, int rightLoadTexture, int topLoadTexture, int bottomLoadTexture,
        int leftShootTexture, int rightShootTexture, int topShootTexture, int bottomShootTexture
    ) {

        DestinationChooser destinationChooser = new DestinationChooser(pathFindingType);

        // State 0: move randomly
        PathChooser randomPathChooser = new PathChooser(pathFindingType);
        PetriNetBase state0 = new PetriNetMoveRandomly(world, entity, randomPathChooser, () -> destinationChooser.chooseRandomPosition(world, entity, 5, 10), minTickWait, maxTickWait, minTickMove, maxTickMove);
        addState(state0);

        // State 1: Load
        PetriNetBase state1 = new PetriNetState();
        state1.addAction(() -> {
            Vector direction = entity.getPosition().getDirectionTo(world.getPlayer().getPosition());
            int textureIndex = PetriNetShoot.getDirectedShootTexture(direction, leftLoadTexture, rightLoadTexture, topLoadTexture, bottomLoadTexture);
            entity.playEffectAnimation(new ObjectAnimation(textureIndex, 1, 2), getTick() - 1);
        });
        addState(state1);

        // State 2: Shoot
        PetriNetBase state2 = new PetriNetState();
        addState(state2);

        // Transitions
        TransitionBase randomToLoadTransition = new Transition(() -> detectionDistance >= (world.getPlayer().getPosition().sub(entity.getPosition()).getNorm()));
        addEdge(0, 1, randomToLoadTransition);

        TransitionBase loadToRandomTransition = new Transition(() -> safeDistance < (world.getPlayer().getPosition().sub(entity.getPosition()).getNorm()));
        addEdge(1, 0, loadToRandomTransition);
        TransitionBase loadToShootTransition = new Transition(() -> loadShootDuration <= getTick());
        loadToShootTransition.addAction(() -> {
            Vector direction = entity.getPosition().getDirectionTo(world.getPlayer().getPosition());
            PetriNetShoot.shoot(world, entity, direction, projectileName, projectileSpeed);
            int textureIndex = PetriNetShoot.getDirectedShootTexture(direction, leftShootTexture, rightShootTexture, topShootTexture, bottomShootTexture);
            entity.playAnimation(new ObjectAnimation(textureIndex, afterShootDuration, 3));
            entity.playSound(shootSound);
        });
        addEdge(1, 2, loadToShootTransition);

        TransitionBase shootToLoadTransition = new Transition(() -> afterShootDuration <= getTick());
        addEdge(2, 1, shootToLoadTransition);
    }
}
