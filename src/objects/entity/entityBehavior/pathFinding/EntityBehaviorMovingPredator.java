package objects.entity.entityBehavior.pathFinding;

import objects.entity.Entity;
import tools.PathFinding.PathFindingType;
import world.World;

public class EntityBehaviorMovingPredator extends EntityBehaviorMovingPassive {
    public static final String DISTANCE_TO_CHANGE = "distanceToChange";
    public static final String SPEED_CHANGE = "speedChange";

    private double distanceToChange = 5;
    private double speedChange = 1.2;

    public EntityBehaviorMovingPredator(PathFindingType pathFindingType) {
        super(pathFindingType);
    }

    public EntityBehaviorMovingPredator(PathFindingType pathFindingType, double distanceToChange) {
        this(pathFindingType);
        this.distanceToChange = distanceToChange;
    }

    public EntityBehaviorMovingPredator(PathFindingType pathFindingType, double distanceToChange, double speedChange) {
        this(pathFindingType, distanceToChange);
        this.speedChange = speedChange;
    }

    public EntityBehaviorMovingPredator(PathFindingType pathFindingType, double distanceToChange, double speedChange, int tickMove, int tickStop) {
        super(pathFindingType, tickMove, tickStop);
        this.distanceToChange = distanceToChange;
        this.speedChange = speedChange;
    }

    @Override
    public void onAttachTo(Entity entity) {
        super.onAttachTo(entity);
        entity.setState(DISTANCE_TO_CHANGE, distanceToChange);
        entity.setState(SPEED_CHANGE, speedChange);
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        super.onUpdate(world,entity);
        Entity player = getEntityToFlee(entity);
        double distance = entity.getDistance(player);
        String movingState = getMovingState(entity);
        double distanceToChange = getDistanceToChange(entity);

        // chase when player is close, move randomly otherwise
        if (distance <= distanceToChange) {
            if (!movingState.equals(CHASE))
                entity.setSpeed(entity.getSpeed() * getSpeedChange(entity));
            entity.setState(MOVING_STATE, CHASE);
        }
        else if (distanceToChange + 10 <= distance) {
            if (movingState.equals(CHASE))
                entity.setSpeed(entity.getSpeed()/getSpeedChange(entity));
            entity.setState(MOVING_STATE, MOVE_RANDOMLY);
        }
    }

    public double getDistanceToChange(Entity entity) {
        Object state = entity.getState(DISTANCE_TO_CHANGE);
        if (state != null && state instanceof Double)
            return (double)state;
        return -1;
    }

    public double getSpeedChange(Entity entity) {
        Object state = entity.getState(SPEED_CHANGE);
        if (state != null && state instanceof Double)
            return (double)state;
        return -1;
    }
}
