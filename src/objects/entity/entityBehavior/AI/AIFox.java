package objects.entity.entityBehavior.AI;

import objects.entity.Entity;
import objects.entity.entityBehavior.AI.utils.AICoward;
import tools.PathFinding.*;
import tools.PetriNet.IPetriNetExecuteActions;
import world.World;

public class AIFox extends AICoward {
    public AIFox(World world, Entity entity) {
        super(world, entity, new PathFindingFalling(), 1.2, 5, 10, 80, 100, 60, 80);
        addAction(() -> IPetriNetExecuteActions.decideTextureDefault(entity, ((IPetriNetExecuteActions)getCurrentState()).getCurrentActions(), IPetriNetExecuteActions.getTickWithoutAction(getCurrentState()), 0, 1, 2, 3, 4, -1, -1, -1, -1));
    }
}
