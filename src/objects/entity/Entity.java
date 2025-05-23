package objects.entity;

import objects.ObjectInstanceMovable;
import objects.block.Block;
import objects.entity.entityBehavior.EntityBehavior;
import tools.*;
import world.World;

public class Entity extends ObjectInstanceMovable<EntityType, Entity, EntityBehavior>{
    private final static long WAITING_TIME_INTERACT = 500;
    private long lastInteraction = 0;

    public Entity(EntityType type,double x, double y, double z) {
        super(type,x,y,z);
    }

    public void notifyCloseBlocks(World world) {
        int x = (int)position.x;
        int y = (int)position.y;
        int z = (int)position.z;
        Vector pos = new Vector();
        for (int i = x - 1; i <= x + 1; i++) {
            pos.x = i + 0.5;
            for (int j = y - 1; j <= y + 1; j++) {
                pos.y = j + 0.5;
                for (int k = z - 1; k <= z + 1; k++) {
                    pos.z = k + 0.5;
                    Block block = world.getBlock(i, j, k);
                    if (block != null)
                        block.onEntityClose(world, pos, this);
                }
            }
        }
    }

    public void interact(World world) {
        long now = System.currentTimeMillis();
        if (lastInteraction + WAITING_TIME_INTERACT <= now) {
            lastInteraction = now;
            // interact with blocks
            int xMin = (int)(position.x - 0.5);
            int xMax = (int)(position.x + 0.5);
            int yMin = (int)(position.y - 0.5);
            int yMax = (int)(position.y + 0.5);
            int zMin = (int)(position.z - 0.5);
            int zMax = (int)(position.z + 0.5);
            int[][] posBlocks = new int[][] {{xMin,yMin,zMin},{xMin,yMin,zMax},{xMin,yMax,zMin},{xMin,yMax,zMax},{xMax,yMin,zMin},{xMax,yMin,zMax},{xMax,yMax,zMin},{xMax,yMax,zMax}};
            for (int[] posBlock : posBlocks) {
                Block block = world.getBlock(posBlock[0], posBlock[1], posBlock[2]);
                if (block != null) {
                    Vector position = new Vector(posBlock[0] + 0.5, posBlock[1] + 0.5, posBlock[2] + 0.5);
                    block.onInteraction(world, position, this);
                }
            }
            // interact with entities
            double radius = 2;
            for (Entity entity : world.getEntities())
                if (entity != this)
                    if (Math.abs(getX() - entity.getX()) < radius && Math.abs(getY() - entity.getY()) < radius && Math.abs(getZ() - entity.getZ()) < radius)
                        entity.onInteraction(world, this);
        }
    }

    public void jump(World world) {
        position.z -= 0.1;
        if (isCollidingBlock(world) || isCollidingEntity(world, 0, 0, 0))
            addVelocity(0, 0, 1.2);
        position.z += 0.1;
    }

    //#region behavior events

    public void onStart(World world) {
        executeEvent(e -> e.onStart(world,this));
    }

    public void onUpdate(World world) {
        double coef = 0.9;
        setVelocity(coef * velocity.x, coef * velocity.y, coef * velocity.z -0.4);
        addVelocity(0, 0, 0.1);
        move(world, velocity.x, velocity.y, velocity.z);
        notifyCloseBlocks(world);
        executeEvent(e -> e.onUpdate(world,this));
    }

    public void onInteraction(World world, Entity entityInteract) {
        executeEvent(e -> e.onInteraction(world,this,entityInteract));
    }

    public void onPush(World world, Vector move, Entity entityPush) {
        executeEvent(e -> e.onPush(world,this,move,entityPush));
    }

    //#endregion
}
