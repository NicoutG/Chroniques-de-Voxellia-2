package objects.entity.entityBehavior.AI;

import objects.entity.Entity;
import objects.entity.entityBehavior.AI.utils.AICoward;
import tools.PathFinding.*;
import tools.PetriNet.IPetriNetExecuteActions;
import world.World;

public class AIChicken extends AICoward {
    public AIChicken(World world, Entity entity) {
        super(world, entity, new PathFindingFalling(), 2.2, 3, 8, 60, 100, 40, 60);
        addAction(() -> IPetriNetExecuteActions.decideTextureDefault(entity, ((IPetriNetExecuteActions)getCurrentState()).getCurrentActions(), 0, 1, 2, 3, 4, -1, -1, -1, -1));
    }
}
