package objects.entity.entityBehavior.AI;

import objects.entity.Entity;
import objects.entity.entityBehavior.AI.utils.AIShooterDefenser;
import tools.PathFinding.*;
import tools.PetriNet.IPetriNetExecuteActions;
import world.World;

public class AIEarthDefenser extends AIShooterDefenser {
    public AIEarthDefenser(World world, Entity entity) {
        super(world, entity, new PathFindingFalling(), 
        6, 10, 
        80, 100, 60, 80, 
        50, 20,10,
        "ballProjectile", 0.8);
        addAction(() -> IPetriNetExecuteActions.decideTextureDefault(entity, ((IPetriNetExecuteActions)getCurrentState()).getCurrentActions(), 0, 1, 2, 3, 4, -1, -1, -1, -1));
    }
}
