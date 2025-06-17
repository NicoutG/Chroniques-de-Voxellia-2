package objects.entity.entityBehavior.pathFinding;

import objects.entity.Entity;
import tools.PathFinding.PathFindingType;
import world.World;

public class EntityBehaviorMovingAnimal extends EntityBehaviorMoving {


    public EntityBehaviorMovingAnimal(PathFindingType pathFindingType) {
        super(pathFindingType);
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        Entity player = getEntityToFlee(entity);
        double distance = entity.getDistance(player);
        String movingState = getMovingState(entity);
    }
}
