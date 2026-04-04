package objects.entity.entityBehavior.AI.utils;

import objects.entity.Entity;
import tools.PathFinding.*;
import tools.PetriNet.*;
import tools.PetriNet.States.*;
import world.World;

public abstract class AIPassive extends AI {
    public AIPassive(World world, Entity entity, PathFindingType pathFindingType, int minTickWait, int maxTickWait, int minTickMove, int maxTickMove) {
        DestinationChooser destinationChooser = new DestinationChooser(pathFindingType);

        // State 0: move randomly
        PathChooser randomPathChooser = new PathChooser(pathFindingType);
        PetriNetBase state0 = new PetriNetMoveRandomly(world, entity, randomPathChooser, () -> destinationChooser.chooseRandomPosition(world, entity, 5, 10), minTickWait, maxTickWait, minTickMove, maxTickMove);
        addState(state0);
    }
}
