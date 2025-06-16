package objects.entity.entityBehavior.pathFinding;

import objects.entity.Entity;
import objects.entity.entityBehavior.EntityBehavior;
import tools.PathFinding.PathFindingType;
import world.World;

public class EntityBehaviorMoving extends EntityBehavior {
    private final static String MOVING_STATE = "movingState";

    private final static String CHASE = "chase";
    private final static String FLEE = "flee";
    private final static String MOVE_RANDOMLY = "moveRandomly";

    private MovingFunctions movingFunctions;
    private String previousMovingState = null;

    public EntityBehaviorMoving(PathFindingType pathFindingType) {
        movingFunctions = new MovingFunctions(pathFindingType);
    }

    @Override
    public void onAttachTo(Entity entity) {
        movingFunctions.onAttachTo(entity);
        entity.setState(MOVING_STATE, "moveRandomly");
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        entity.setState(MovingFunctions.ENTITY_TO_FOLLOW, world.getPlayer());
        entity.setState(MovingFunctions.ENTITY_TO_FLEE, world.getPlayer());
        movingFunctions.move(world, entity);
        Entity entityFlee = movingFunctions.getEntityToFlee(entity);
        double distance = entity.getDistance(entityFlee);
        if (distance <= 10)
            entity.setState(MOVING_STATE, MOVE_RANDOMLY);
        if (20 <= distance)
            entity.setState(MOVING_STATE, CHASE);

        String movingState = getMovingState(entity);
        if ((movingState == null && previousMovingState != null) || !movingState.equals(previousMovingState)) {
            movingFunctions.reinitiPath();
            previousMovingState = movingState;
            System.out.println(movingState);
        }
        switch (movingState) {
            case CHASE: movingFunctions.chase(world, entity);break;
            case FLEE: movingFunctions.flee(world, entity);break;
            case MOVE_RANDOMLY: movingFunctions.moveRandomly(world, entityFlee);break;
        }
    }

    public String getMovingState(Entity entity) {
        Object state = entity.getState(MOVING_STATE);
        if (state != null && state instanceof String)
            return (String)state;
        return null;
    }
}
