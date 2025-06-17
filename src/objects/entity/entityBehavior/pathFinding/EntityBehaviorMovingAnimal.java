package objects.entity.entityBehavior.pathFinding;

import java.util.Random;

import objects.entity.Entity;
import tools.PathFinding.PathFindingType;
import world.World;

public class EntityBehaviorMovingAnimal extends EntityBehaviorMoving {
    public static final String DISTANCE_TO_FLEE = "distanceToFlee";
    public static final String SPEED_CHANGE = "speedChange";
    public static final String TICK_STOP = "tickStop";
    public static final String TICK_MOVE = "tickMove";

    private int tick = 0;
    private int nextUpadate = 0;

    public EntityBehaviorMovingAnimal(PathFindingType pathFindingType) {
        super(pathFindingType);
    }

    @Override
    public void onAttachTo(Entity entity) {
        super.onAttachTo(entity);
        entity.setState(DISTANCE_TO_FLEE, 5.0);
        entity.setState(SPEED_CHANGE, 1.2);
        entity.setState(TICK_MOVE, 80);
        entity.setState(TICK_STOP, 100);
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        super.onUpdate(world,entity);
        Entity player = getEntityToFlee(entity);
        double distance = entity.getDistance(player);
        String movingState = getMovingState(entity);
        double distanceToFlee = getDistanceToFlee(entity);

        // flee when player is close, move randomly otherwise
        if (distance <= distanceToFlee) {
            if (!movingState.equals(FLEE))
                entity.setSpeed(entity.getSpeed() * getSpeedChange(entity));
            entity.setState(MOVING_STATE, FLEE);
        }
        else if (distanceToFlee + 10 <= distance) {
            if (movingState.equals(FLEE))
                entity.setSpeed(entity.getSpeed()/getSpeedChange(entity));
            entity.setState(MOVING_STATE, MOVE_RANDOMLY);
        }

        // alternates between movement and pauses during random movements
        tick++;
        if (nextUpadate <= tick) {
            tick = 0;
            movingState = getMovingState(entity);
            Random random = new Random();
            if (movingState.equals(MOVE_RANDOMLY)) {
                int tickStop = getTickStop(entity);
                nextUpadate = Math.max(1,tickStop/2 + random.nextInt(Math.max(1,tickStop/2)));
                entity.setState(MOVING_STATE, WAIT);
            }
            else if (movingState.equals(WAIT)) {
                int tickMove = getTickMove(entity);
                nextUpadate = Math.max(1,tickMove/2 + random.nextInt(Math.max(1,tickMove/2)));
                entity.setState(MOVING_STATE, MOVE_RANDOMLY);
            }
        }
    }

    public double getDistanceToFlee(Entity entity) {
        Object state = entity.getState(DISTANCE_TO_FLEE);
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

    public int getTickStop(Entity entity) {
        Object state = entity.getState(TICK_STOP);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }

    public int getTickMove(Entity entity) {
        Object state = entity.getState(TICK_MOVE);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
