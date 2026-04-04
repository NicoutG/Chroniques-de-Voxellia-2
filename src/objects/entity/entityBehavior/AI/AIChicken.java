package objects.entity.entityBehavior.AI;

import objects.entity.Entity;
import objects.entity.entityBehavior.AI.utils.AICoward;
import tools.PathFinding.*;
import world.World;

public class AIChicken extends AICoward {
    public AIChicken(World world, Entity entity) {
        super(world, entity, new PathFindingFalling(), 2.2, 3, 8, 60, 100, 40, 60);
    }
}
