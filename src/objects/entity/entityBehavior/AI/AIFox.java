package objects.entity.entityBehavior.AI;

import objects.entity.Entity;
import objects.entity.entityBehavior.AI.utils.AICoward;
import tools.PathFinding.*;
import world.World;

public class AIFox extends AICoward {
    public AIFox(World world, Entity entity) {
        super(world, entity, new PathFindingFalling(), 1.2, 5, 10, 80, 100, 60, 80);
    }
}
