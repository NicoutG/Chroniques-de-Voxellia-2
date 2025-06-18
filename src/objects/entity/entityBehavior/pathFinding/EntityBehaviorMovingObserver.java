package objects.entity.entityBehavior.pathFinding;

import objects.entity.Entity;
import tools.PathFinding.PathFindingType;
import world.World;

public class EntityBehaviorMovingObserver extends EntityBehaviorMovingPassive {
    public static final String DISTANCE_TO_CHANGE = "distanceToChange";
    public static final String DISTANCE_TO_STAY = "distanceToStay";
    public static final String SPEED_CHANGE = "speedChange";

    private double distanceToChange = 10;
    private double distanceToStay = 5;
    private double speedChange = 1.2;

    public EntityBehaviorMovingObserver(PathFindingType pathFindingType) {
        super(pathFindingType);
    }

    public EntityBehaviorMovingObserver(PathFindingType pathFindingType, double distanceToChange, double distanceToStay) {
        this(pathFindingType);
        this.distanceToChange = distanceToChange;
        this.distanceToStay = distanceToStay;
    }

    public EntityBehaviorMovingObserver(PathFindingType pathFindingType, double distanceToChange, double distanceToStay, double speedChange) {
        this(pathFindingType, distanceToChange, distanceToStay);
        this.speedChange = speedChange;
    }

    public EntityBehaviorMovingObserver(PathFindingType pathFindingType, double distanceToChange, double distanceToStay, double speedChange, int tickMove, int tickStop) {
        super(pathFindingType, tickMove, tickStop);
        this.distanceToChange = distanceToChange;
        this.distanceToStay = distanceToStay;
        this.speedChange = speedChange;
    }

    @Override
    public void onAttachTo(Entity entity) {
        super.onAttachTo(entity);
        entity.setState(DISTANCE_TO_CHANGE, distanceToChange);
        entity.setState(DISTANCE_TO_STAY, distanceToStay);
        entity.setState(SPEED_CHANGE, speedChange);
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        super.onUpdate(world,entity);
        Entity player = getEntityToFlee(entity);
        double distance = entity.getDistance(player);
        String movingState = getMovingState(entity);
        double distanceToChange = getDistanceToChange(entity);

        // flee when player is close, move randomly otherwise
        if (distance <= distanceToStay) {
            if (movingState.equals(MOVE_RANDOMLY) || movingState.equals(WAIT))
                entity.setSpeed(entity.getSpeed() * getSpeedChange(entity));
            entity.setState(MOVING_STATE, FLEE);
        }
        else if (distance <= distanceToStay + 2) {
            if (movingState.equals(CHASE) || movingState.equals(FLEE))
                entity.setSpeed(entity.getSpeed() / getSpeedChange(entity));
            entity.setState(MOVING_STATE, WAIT);
        }
        else if (distance <= distanceToChange) {
            if (movingState.equals(MOVE_RANDOMLY) || movingState.equals(WAIT))
                entity.setSpeed(entity.getSpeed() * getSpeedChange(entity));
            entity.setState(MOVING_STATE, CHASE);
        }
        else {
            if (!movingState.equals(WAIT) && !movingState.equals(MOVE_RANDOMLY)) {
                entity.setSpeed(entity.getSpeed()/getSpeedChange(entity));
                entity.setState(MOVING_STATE, MOVE_RANDOMLY);
            }
        }
    }

    public double getDistanceToChange(Entity entity) {
        Object state = entity.getState(DISTANCE_TO_CHANGE);
        if (state != null && state instanceof Double)
            return (double)state;
        return -1;
    }

    public double getDistanceToSTay(Entity entity) {
        Object state = entity.getState(DISTANCE_TO_STAY);
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
