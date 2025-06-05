package objects.entity.entityBehavior.pathFinding;

import java.util.ArrayList;
import java.util.HashSet;

import objects.entity.Entity;
import objects.entity.EntityAction;
import objects.entity.entityBehavior.EntityBehavior;
import tools.Vector;
import tools.PathFinding.Node;
import tools.PathFinding.PathFinding;
import tools.PathFinding.PathFindingType;
import world.World;

public class EntityBehaviorFollowPlayer extends EntityBehavior {
    private final static int MAX_SEARCH_PER_TICK = 100;
    private final static int MAX_SEARCH = 2000;

    private PathFindingType pathFindingType;
    private ArrayList<Node> openList = null;
    private HashSet<Vector> closedList = null;
    private ArrayList<Vector> path = null;
    private Vector destination = null;

    public EntityBehaviorFollowPlayer(PathFindingType pathFindingType) {
        this.pathFindingType = pathFindingType;
    }

    @Override
    public void onStart(World world,Entity entity) {
        initPathFinding(world, entity);
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        if (path != null && !path.isEmpty()) {
            Vector nextStep = path.get(0);
            EntityAction[] actions = pathFindingType.convertToAction(entity, nextStep, entity.getSpeed());
            entity.doActions(world, actions);
            if ((int)entity.getX() == (int)nextStep.x && (int)entity.getY() == (int)nextStep.y && (int)entity.getZ() == (int)nextStep.z)
                path.remove(0);
        }
        
        Boolean findPath = PathFinding.findPath(world, entity, destination, openList, closedList, MAX_SEARCH_PER_TICK, pathFindingType);
        if (findPath != null && findPath) {
            path = PathFinding.getPathAndLinkToEntity(world, entity, openList, pathFindingType);
            initPathFinding(world, entity);
        }
        else if ((findPath != null && !findPath) || MAX_SEARCH <= closedList.size()) {
            path = null;
            initPathFinding(world, entity);
        }
    }

    private void initPathFinding(World world, Entity entity) {
        openList = new ArrayList<>();
        openList = PathFinding.initOpenList(world, entity.getPosition());
        closedList = new HashSet<>();
        destination = PathFinding.adaptPosition(world, world.getPlayer().getPosition());
    }
}
