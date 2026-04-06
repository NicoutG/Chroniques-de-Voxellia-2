package tools.PathFinding;

import objects.entity.Entity;
import tools.Vector;
import world.World;

public class DestinationChooser {
    private Vector destination;
    private PathFindingType pathFindingType;

    private final static int NB_RANDOM_SEARCH = 10;

    public DestinationChooser(PathFindingType pathFindingType) {
        this.pathFindingType = pathFindingType;
    }

    public Vector getDestination() {
        return destination;
    }

    public double getDistanceToDestination(Entity entity) {
        if (destination != null)
            return destination.sub(entity.getPosition()).getNorm();
        return 0;
    }

    public Vector chooseEntity(Entity destinationEntity) {
        if (destinationEntity != null)
            destination = destinationEntity.getPosition();
        return destination;
    }

    public Vector chooseRandomPosition(World world, Entity entity, double minDistance, double maxDistance) {
        destination = pathFindingType.getRandomDestination(world, entity, minDistance, maxDistance);
        return destination;
    }

    public Vector chooseFurthestPosition(World world, Entity entity, Vector positionToFlee, double minDistance, double maxDistance) {
        Vector furthestPos = null;
        double furthestDist = -1;
        for (int i = 0; i < NB_RANDOM_SEARCH; i++) {
            Vector goal = chooseRandomPosition(world, entity, minDistance, maxDistance);
            double dist = goal.sub(positionToFlee).getNorm();
            if (furthestDist < dist) {
                furthestDist = dist;
                furthestPos = goal;
            }
        }
        destination = furthestPos;
        return destination;
    }

    public Vector chooseFurthestPosition(World world, Entity entity, Entity entityToFlee, double minDistance, double maxDistance) {
        return chooseFurthestPosition(world, entity, chooseEntity(entityToFlee), minDistance, maxDistance);
    }

    public Vector chooseClosestPosition(World world, Entity entity, Vector positionToFollow, double minDistance, double maxDistance) {
        Vector closestPos = null;
        double closestDist = Double.MAX_VALUE;
        for (int i = 0; i < NB_RANDOM_SEARCH; i++) {
            Vector goal = chooseRandomPosition(world, entity, minDistance, maxDistance);
            double dist = goal.sub(positionToFollow).getNorm();
            if (closestDist > dist) {
                closestDist = dist;
                closestPos = goal;
            }
        }
        destination = closestPos;
        return destination;
    }

    public Vector chooseClosestPosition(World world, Entity entity, Entity entityToFollow, double minDistance, double maxDistance) {
        return chooseClosestPosition(world, entity, chooseEntity(entityToFollow), minDistance, maxDistance);
    }
}
