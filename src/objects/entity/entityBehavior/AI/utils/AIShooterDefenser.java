package objects.entity.entityBehavior.AI.utils;

import objects.ObjectAnimation;
import objects.entity.Entity;
import objects.entity.entityBehavior.EntityBehaviorConstantVelocity;
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
        int tickBeetweenShoot, int shootAnimationDuration, int chargeShootDuration, 
        String projectileName, double projectileSpeed) {

        DestinationChooser destinationChooser = new DestinationChooser(pathFindingType);

        // State 0: move randomly
        PathChooser randomPathChooser = new PathChooser(pathFindingType);
        PetriNetBase state0 = new PetriNetMoveRandomly(world, entity, randomPathChooser, () -> destinationChooser.chooseRandomPosition(world, entity, 5, 10), minTickWait, maxTickWait, minTickMove, maxTickMove);
        addState(state0);

        // State 1: Wait
        PetriNetBase state1 = new PetriNetState();
        addState(state1);

        // State 1: Fire
        PetriNetBase state2 = new PetriNetState(() -> {
            Vector direction = entity.getPosition().getDirectionTo(world.getPlayer().getPosition());
            int textureIndex = getDirectedShootTexture(direction);
            if (entity.getIndexTexture() != textureIndex)
                entity.playEffectAnimation(new ObjectAnimation(textureIndex, shootAnimationDuration, 1), getTick() - 1);
            if (getTick() == chargeShootDuration)
                shoot(world, entity, direction, projectileName, projectileSpeed);
        });
        addState(state2);

        // Transitions
        TransitionBase toWaitTransition = new Transition(() -> detectionDistance >= (world.getPlayer().getPosition().sub(entity.getPosition()).getNorm()));
        addEdge(0, 1, toWaitTransition);

        TransitionBase toRandomTransition = new Transition(() -> safeDistance < (world.getPlayer().getPosition().sub(entity.getPosition()).getNorm()));
        addEdge(1, 0, toRandomTransition);
        TransitionBase toFireTransition = new Transition(() -> tickBeetweenShoot <= getTick());
        addEdge(1, 2, toFireTransition);

        TransitionBase toWaitTransition2 = new Transition(() -> shootAnimationDuration <= getTick());
        addEdge(2, 1, toWaitTransition2);
    }

    private int getDirectedShootTexture(Vector direction) {
        int start = 5;
        if (Math.abs(direction.y) < Math.abs(direction.x)) {
            if (direction.x > 0)
                return start + 1;
            else
                return start;
        }
        else {
            if (direction.y > 0)
                return start + 3;
            else
                return start + 2;
        }
    }

    private void shoot(World world, Entity entity, Vector direction, String projectileName, double projectileSpeed) {
        Entity projectile = world.getEntity(projectileName);
        projectile.setPosition(entity.getPosition().x + direction.x, entity.getPosition().y + direction.y, entity.getPosition().z + direction.z);
        projectile.setState(EntityBehaviorConstantVelocity.VELOCITY, new double[] {direction.x * projectileSpeed, direction.y * projectileSpeed, direction.z * projectileSpeed});
        world.addEntity(projectile);
    }
}
