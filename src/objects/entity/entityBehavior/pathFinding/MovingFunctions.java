package objects.entity.entityBehavior.pathFinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.Vector;
import tools.PathFinding.Node;
import tools.PathFinding.PathFinding;
import tools.PathFinding.PathFindingType;
import world.World;

public class MovingFunctions {
    public final static String ENTITY_TO_CHASE = "entityToFollow";
    public final static String ENTITY_TO_FLEE = "entityToFlee";

    private final static int MAX_SEARCH_PER_TICK = 100;
    private final static int MAX_SEARCH = 2000;
    private final static int DISTANCE_TO_MOVE = 10;
    private final static int MAX_TICK_TO_UPDATE = 50;

    private PathFindingType pathFindingType;
    private ArrayList<Node> openList = null;
    private HashSet<Vector> closedList = null;
    private ArrayList<Vector> path = null;
    private Vector destination = null;

    private int tick = 0;
    private int nextUpdate = 0;

    public MovingFunctions(PathFindingType pathFindingType) {
        this.pathFindingType = pathFindingType;
    }

    public void onAttachTo(Entity entity) {
        entity.setState(ENTITY_TO_CHASE, null);
        entity.setState(ENTITY_TO_FLEE, null);
    }

    public int getDistanceToDestination() {
        if (path == null)
            return -1;
        return path.size();
    }

    public void move(World world, Entity entity) {
        if (path != null && !path.isEmpty()) {
            Vector nextStep = path.get(0);
            EntityAction[] actions = pathFindingType.convertToAction(entity, nextStep, entity.getSpeed());
            entity.doActions(world, actions);
            if ((int)entity.getX() == (int)nextStep.x && (int)entity.getY() == (int)nextStep.y && (int)entity.getZ() == (int)nextStep.z)
                path.remove(0);
        }
    }

    public void chase(World world, Entity entity) {
        Entity entityToChase = getEntityToChase(entity);
        if (entityToChase != null) {
            if (openList == null)
                initPathFinding(world, entity.getPosition(), entityToChase.getPosition());
            Boolean findPath = PathFinding.findPath(world, entity, destination, openList, closedList, MAX_SEARCH_PER_TICK, pathFindingType);
            if (findPath != null && findPath) {
                path = PathFinding.getPathAndLinkToEntity(world, entity, openList, pathFindingType);
                initPathFinding(world, entity.getPosition(), entityToChase.getPosition());
            }
            else if ((findPath != null && !findPath) || MAX_SEARCH <= closedList.size()) {
                path = null;
                initPathFinding(world, entity.getPosition(), entityToChase.getPosition());
            }
        }
    }

    public void moveRandomly(World world, Entity entity) {
        tick++;
        if (nextUpdate <= tick) {
            openList = null;
            tick = 0;
            Random random = new Random();
            nextUpdate = 1 + MAX_TICK_TO_UPDATE/2 + random.nextInt(MAX_TICK_TO_UPDATE/2);
        }
        if (openList == null || path == null || path.isEmpty()) {
            initPathFinding(world, entity.getPosition(), pathFindingType.getRandomDestination(world, entity, DISTANCE_TO_MOVE));
        }
        Boolean findPath = PathFinding.findPath(world, entity, destination, openList, closedList, MAX_SEARCH_PER_TICK, pathFindingType);
        if (findPath != null && findPath) {
            path = PathFinding.getPathAndLinkToEntity(world, entity, openList, pathFindingType);
            initPathFinding(world, entity.getPosition(), destination);
        }
        else if ((findPath != null && !findPath) || MAX_SEARCH <= closedList.size()) {
            path = null;
            initPathFinding(world, entity.getPosition(), pathFindingType.getRandomDestination(world, entity, DISTANCE_TO_MOVE));
        }
    }

    public void flee(World world, Entity entity) {
        Entity entityToFlee = getEntityToFlee(entity);
        if (entityToFlee != null) {
            tick++;
            if (nextUpdate <= tick) {
                openList = null;
                tick = 0;
                Random random = new Random();
                nextUpdate = 1 + MAX_TICK_TO_UPDATE/2 + random.nextInt(MAX_TICK_TO_UPDATE/2);
            }
            if (openList == null || path == null || path.isEmpty())
                initPathFinding(world, entity.getPosition(), findFurthestPosition(world, entity, entityToFlee.getPosition()));
            Boolean findPath = PathFinding.findPath(world, entity, destination, openList, closedList, MAX_SEARCH_PER_TICK, pathFindingType);
            if (findPath != null && findPath) {
                path = PathFinding.getPathAndLinkToEntity(world, entity, openList, pathFindingType);
                initPathFinding(world, entity.getPosition(), destination);
            }
            else if ((findPath != null && !findPath) || MAX_SEARCH <= closedList.size()) {
                path = null;
                initPathFinding(world, entity.getPosition(), findFurthestPosition(world, entity, entityToFlee.getPosition()));
            }
        }
    }

    private Vector findFurthestPosition(World world, Entity entity, Vector fleePos) {
        Vector furthestPos = null;
        double furthestDist = -1;
        for (int i = 0; i < 50; i++) {
            Vector goal = pathFindingType.getRandomDestination(world, entity, DISTANCE_TO_MOVE);
            double dist = goal.sub(fleePos).getNorm();
            if (furthestDist < dist) {
                furthestDist = dist;
                furthestPos = goal;
            }
        }
        return furthestPos;
    }

    public void initPathFinding(World world, Vector position, Vector goal) {
        openList = new ArrayList<>();
        openList = PathFinding.initOpenList(world, position);
        closedList = new HashSet<>();
        destination = PathFinding.adaptPosition(world, goal);
    }

    public Entity getEntityToChase(Entity entity) {
        Object state = entity.getState(ENTITY_TO_CHASE);
        if (state != null && state instanceof Entity)
            return (Entity)state;
        return null;
    }

    public Entity getEntityToFlee(Entity entity) {
        Object state = entity.getState(ENTITY_TO_FLEE);
        if (state != null && state instanceof Entity)
            return (Entity)state;
        return null;
    }

    public MovingFunctions clone() {
        return new MovingFunctions(pathFindingType);
    }
}
