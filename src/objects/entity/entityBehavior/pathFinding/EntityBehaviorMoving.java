package objects.entity.entityBehavior.pathFinding;

import objects.entity.Entity;
import objects.entity.entityBehavior.EntityBehavior;
import tools.PathFinding.PathFindingType;
import world.World;

public abstract class EntityBehaviorMoving extends EntityBehavior {
    public final static String MOVING_STATE = "movingState";

    public final static String CHASE = "chase";
    public final static String FLEE = "flee";
    public final static String MOVE_RANDOMLY = "moveRandomly";
    public final static String WAIT = "wait";

    private MovingFunctions movingFunctions;

    public EntityBehaviorMoving(PathFindingType pathFindingType) {
        movingFunctions = new MovingFunctions(pathFindingType);
    }

    @Override
    public void onAttachTo(Entity entity) {
        movingFunctions.onAttachTo(entity);
        entity.setState(MOVING_STATE, "moveRandomly");
    }

    @Override
    public void onStart(World world, Entity entity) {
        entity.setState(MovingFunctions.ENTITY_TO_CHASE, world.getPlayer());
        entity.setState(MovingFunctions.ENTITY_TO_FLEE, world.getPlayer());
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        movingFunctions.move(world, entity);

        String movingState = getMovingState(entity);
        switch (movingState) {
            case CHASE: movingFunctions.chase(world, entity);break;
            case FLEE: movingFunctions.flee(world, entity);break;
            case MOVE_RANDOMLY: movingFunctions.moveRandomly(world, entity);break;
        }
    }

    public String getMovingState(Entity entity) {
        Object state = entity.getState(MOVING_STATE);
        if (state != null && state instanceof String)
            return (String)state;
        return null;
    }

    public Entity getEntityToChase(Entity entity) {
        return movingFunctions.getEntityToChase(entity);
    }

    public Entity getEntityToFlee(Entity entity) {
        return movingFunctions.getEntityToFlee(entity);
    }

    @Override
    public EntityBehavior clone() {
        EntityBehaviorMoving clone = (EntityBehaviorMoving)super.clone();
        clone.movingFunctions = (MovingFunctions)movingFunctions.clone();
        return clone;
    }
}
