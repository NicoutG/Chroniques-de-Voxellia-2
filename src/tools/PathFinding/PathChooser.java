package tools.PathFinding;

import java.util.ArrayList;
import java.util.HashSet;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.Vector;
import world.World;

public class PathChooser {
    private final static int MAX_SEARCH_PER_TICK = 100;
    private final static int MAX_SEARCH = 2000;
    
    private PathFindingType pathFindingType;
    private ArrayList<Node> openList = null;
    private HashSet<Vector> closedList = null;
    private ArrayList<Vector> path = null;
    private Vector destination = null;

    private PathState state = null;

    public PathChooser(PathFindingType pathFindingType) {
        this.pathFindingType = pathFindingType;
    }

    public void resetSearch() {
        openList = null;
    }

    public void resetPath() {
        path = null;
    }

    public int getDistanceToDestination() {
        if (path == null)
            return -1;
        return path.size();
    }

    public PathState getState() {
        if (state == null)
            return PathState.NO_PATH;
        return state;
    }

    public void setDestination(World world, Entity entity, Vector destination) {
        this.destination = PathFinding.adaptPosition(world, destination);
        initPathFinding(world, entity.getPosition());
    }

    public PathState updatePath(World world, Entity entity) {
        if (openList == null || path == null || path.isEmpty())
            initPathFinding(world, entity.getPosition());

        Boolean findPath = PathFinding.findPath(world, entity, destination, openList, closedList, MAX_SEARCH_PER_TICK, pathFindingType);
        if (findPath != null && findPath) {
            path = PathFinding.getPathAndLinkToEntity(world, entity, openList, pathFindingType);
            state = PathState.FOUND;
            initPathFinding(world, entity.getPosition());
            return state;
        }
        else if ((findPath != null && !findPath) || MAX_SEARCH <= closedList.size()) {
            path = null;
            initPathFinding(world, entity.getPosition());
            if (findPath != null && !findPath)
                state = PathState.NO_PATH;
            else
                state = PathState.FAILED;
            return state;
        }
        state = PathState.SEARCHING;
        return state;
    }

    public void moveToDestination(World world, Entity entity) {
        if (path != null && !path.isEmpty()) {
            Vector nextStep = path.get(0);
            EntityAction[] actions = pathFindingType.convertToAction(entity, nextStep, entity.getSpeed());
            entity.doActions(world, actions);
            if ((int)entity.getX() == (int)nextStep.x && (int)entity.getY() == (int)nextStep.y && (int)entity.getZ() == (int)nextStep.z)
                path.remove(0);
            if (getDistanceToDestination() <= 0)
                state = PathState.REACHED;
        }
    }

    private void initPathFinding(World world, Vector position) {
        openList = new ArrayList<>();
        openList = PathFinding.initOpenList(world, position);
        closedList = new HashSet<>();
    }
}
