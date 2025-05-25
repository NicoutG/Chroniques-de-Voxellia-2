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
            int xMin = (int)position.x - 1;
            int yMin = (int)position.y - 1;
            int zMin = (int)position.z - 1;
            Vector positionBlock = new Vector();
            for (int z = zMin; z < zMin + 3; z++) {
                positionBlock.z = z + 0.5;
                for (int y = yMin; y < yMin + 3; y++) {
                    positionBlock.y = y + 0.5;
                    for (int x = xMin; x < xMin + 3; x++) {
                        Block block = world.getBlock(x,y,z);
                        if (block != null) {
                            positionBlock.x = x + 0.5;
                            block.onInteraction(world, positionBlock, this);
                        }
                    }
                }
            }
            // interact with entities
            double radius = 1.5;
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

    public void onActivated(World world, int network) {
        executeEvent(e -> e.onActivated(world,this,network));
    }

    public void onDesactivated(World world, int network) {
        executeEvent(e -> e.onDesactivated(world,this,network));
    }

    public void onEntityCollision(World world, Entity entityCollision) {
        executeEvent(e -> e.onEntityCollision(world,this,entityCollision));
    }

    public void onDeath(World world) {
        executeEvent(e -> e.onDeath(world,this));
    }

    //#endregion
}
