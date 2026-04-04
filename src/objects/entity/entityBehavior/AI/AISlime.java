package objects.entity.entityBehavior.AI;

import objects.entity.Entity;
import objects.entity.entityBehavior.AI.utils.AIPassive;
import tools.PathFinding.*;
import world.World;

public class AISlime extends AIPassive {
    public AISlime(World world, Entity entity) {
        super(world, entity, new PathFindingFly(), 80, 100, 60, 80);
    }
}
