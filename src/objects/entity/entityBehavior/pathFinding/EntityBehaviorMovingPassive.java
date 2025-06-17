package objects.entity.entityBehavior.pathFinding;

import java.util.Random;

import objects.entity.Entity;
import tools.PathFinding.PathFindingType;
import world.World;

public class EntityBehaviorMovingPassive extends EntityBehaviorMoving {
    public static final String TICK_STOP = "tickStop";
    public static final String TICK_MOVE = "tickMove";

    private int tick = 0;
    private int nextUpadate = 0;
    private int tickMove = 80;
    private int tickStop = 100;

    public EntityBehaviorMovingPassive(PathFindingType pathFindingType) {
        super(pathFindingType);
    }

    public EntityBehaviorMovingPassive(PathFindingType pathFindingType, int tickMove, int tickStop) {
        this(pathFindingType);
        this.tickMove = tickMove;
        this.tickStop = tickStop;
    }

    @Override
    public void onAttachTo(Entity entity) {
        super.onAttachTo(entity);
        entity.setState(TICK_MOVE, tickMove);
        entity.setState(TICK_STOP, tickStop);
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        super.onUpdate(world,entity);

        // alternates between movement and pauses during random movements
        String movingState = getMovingState(entity);
        if (movingState.equals(WAIT) || movingState.equals(MOVE_RANDOMLY)) {
            tick++;
            if (nextUpadate <= tick) {
                tick = 0;
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
