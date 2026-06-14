package objects.entity.entityBehavior.AI;

import audio.SoundType;
import objects.entity.Entity;
import objects.entity.entityBehavior.AI.utils.AIStaticFollower;
import tools.PathFinding.*;
import tools.PetriNet.*;
import world.World;

public class AIKnight extends AIStaticFollower {
    public AIKnight(World world, Entity entity) {
        super(world, entity, new PathFindingFalling(),
        8, 12, SoundType.THE_TOURNAMENT);
        addAction(() -> IPetriNetExecuteActions.decideTextureDefault(entity, ((IPetriNetExecuteActions)getCurrentState()).getCurrentActions(), 0, 1, 2, 3, 4, -1, -1, -1, -1));
    }
}
