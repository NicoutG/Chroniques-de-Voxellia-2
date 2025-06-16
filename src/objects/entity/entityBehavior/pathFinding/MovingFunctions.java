package objects.entity.entityBehavior.pathFinding;

import java.util.ArrayList;
import java.util.HashSet;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.Vector;
import tools.PathFinding.Node;
import tools.PathFinding.PathFinding;
import tools.PathFinding.PathFindingType;
import world.World;

public class MovingFunctions {
    public final static String ENTITY_TO_FOLLOW = "entityToFollow";
    public final static String ENTITY_TO_FLEE = "entityToFlee";

    private final static int MAX_SEARCH_PER_TICK = 100;
    private final static int MAX_SEARCH = 2000;
    private final static int TICK_TO_UPDATE = 50;

    private PathFindingType pathFindingType;
    private ArrayList<Node> openList = null;
    private HashSet<Vector> closedList = null;
    private ArrayList<Vector> path = null;
    private Vector destination = null;

    private int tick = 0;
    private int lastUpdate = 0;

    public MovingFunctions(PathFindingType pathFindingType) {
        this.pathFindingType = pathFindingType;
    }

    public void onAttachTo(Entity entity) {
        entity.setState(ENTITY_TO_FOLLOW, null);
    }

    public void reinitiPath() {
        path = null;
    }

    public int getDistanceToDestination() {
        if (path == null)
            return -1;
        return path.size();
    }

    public void move(World world, Entity entity) {
        tick++;
        if (path != null && !path.isEmpty()) {
            Vector nextStep = path.get(0);
            EntityAction[] actions = pathFindingType.convertToAction(entity, nextStep, entity.getSpeed());
            entity.doActions(world, actions);
            if ((int)entity.getX() == (int)nextStep.x && (int)entity.getY() == (int)nextStep.y && (int)entity.getZ() == (int)nextStep.z)
                path.remove(0);
        }
    }

    public void chase(World world, Entity entity) {
        Entity entityToChase = getEntityToFollow(entity);
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
        if (path == null || path.isEmpty() || TICK_TO_UPDATE < tick - lastUpdate) {
            lastUpdate = tick;
            Vector goal = pathFindingType.getRandomDestination(world, entity, 10);
            initPathFinding(world, entity.getPosition(), goal);
            Boolean findPath = PathFinding.findPath(world, entity, destination, openList, closedList, MAX_SEARCH_PER_TICK, pathFindingType);
            if (findPath != null && findPath) {
                path = PathFinding.getPathAndLinkToEntity(world, entity, openList, pathFindingType);
            }
            else if ((findPath != null && !findPath) || MAX_SEARCH <= closedList.size())
                path = null;
        }
    }

    public void flee(World world, Entity entity) {
        if (path == null || path.isEmpty() || TICK_TO_UPDATE < tick - lastUpdate) {
            lastUpdate = tick;
            Entity entityToFlee = getEntityToFlee(entity);
            if (entityToFlee != null) {
                Vector fleePos = entityToFlee.getPosition();
                Vector furthestPos = null;
                double furthestDist = -1;
                for (int i = 0; i < 50; i++) {
                    Vector goal = pathFindingType.getRandomDestination(world, entity, 10);
                    double dist = goal.sub(fleePos).getNorm();
                    if (furthestDist < dist) {
                        furthestDist = dist;
                        furthestPos = goal;
                    }
                }
                
                initPathFinding(world, entity.getPosition(), furthestPos);
                Boolean findPath = PathFinding.findPath(world, entity, destination, openList, closedList, MAX_SEARCH_PER_TICK, pathFindingType);
                if (findPath != null && findPath) {
                    path = PathFinding.getPathAndLinkToEntity(world, entity, openList, pathFindingType);
                }
                else if ((findPath != null && !findPath) || MAX_SEARCH <= closedList.size())
                    path = null;
            }
        }
    }

    public void initPathFinding(World world, Vector position, Vector goal) {
        openList = new ArrayList<>();
        openList = PathFinding.initOpenList(world, position);
        closedList = new HashSet<>();
        destination = PathFinding.adaptPosition(world, goal);
    }

    public Entity getEntityToFollow(Entity entity) {
        Object state = entity.getState(ENTITY_TO_FOLLOW);
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
}
