package objects;

import objects.block.Block;
import objects.collision.CollisionList;
import objects.entity.Entity;
import objects.objectBehavior.ObjectBehavior;
import tools.Vector;
import world.World;

public class ObjectInstanceMovable <
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, B>,
    B extends ObjectBehavior<T, I, B>
> extends ObjectInstance<T,I,B> {

    public static final String NO_COLLISION = "noCollision";
    public static final String NO_COLLISION_BLOCK = "noCollisionBlock";
    public static final String NO_COLLISION_ENTITY = "noCollisionEntity";
    public static final String NO_COLLISION_SAME = "noCollisionSame";
    protected static final double MAX_VELOCITY = 2.5;

    protected Vector position;
    protected Vector velocity = new Vector();

    public ObjectInstanceMovable(T type, double x, double y, double z) {
        super(type);
        position = new Vector(x,y,z);
    }

    public double getX() {
        return position.x;
    }

    public double getY() {
        return position.y;
    }

    public double getZ() {
        return position.z;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(double x, double y, double z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(double x, double y, double z) {
        velocity.x = x;
        if (x < -MAX_VELOCITY)
            velocity.x = -MAX_VELOCITY;
        else
            if (MAX_VELOCITY < x)
                velocity.x = MAX_VELOCITY;
        velocity.y = y;
        if (y < -MAX_VELOCITY)
            velocity.y = -MAX_VELOCITY;
        else
            if (MAX_VELOCITY < y)
                velocity.y = MAX_VELOCITY;
        velocity.z = z;
        if (z < -MAX_VELOCITY)
            velocity.z = -MAX_VELOCITY;
        else
            if (MAX_VELOCITY < z)
                velocity.z = MAX_VELOCITY;
    }

    public void addVelocity(double x, double y, double z) {
        setVelocity(velocity.x + x, velocity.y + y, velocity.z + z);
    }

    public Vector move(World world, double moveX, double moveY, double moveZ) {
        if (noCollision(this)) {
            position.x += moveX;
            position.y += moveY;
            position.z += moveZ;
            return new Vector(moveX, moveY, moveZ);
        }

        Vector realMove = new Vector();
        realMove.x = moveAxis(world, moveX, 0, 0);
        realMove.y = moveAxis(world, 0, moveY, 0);
        realMove.z = moveAxis(world, 0, 0, moveZ);

        if (!noCollisionEntity(this)) {
            boolean noCollisionSame = noCollisionSame(this);
            for (Entity entity : world.getEntities()) {
                if (entity != (Entity)this && !noCollision(entity) && 
                        !noCollisionEntity(entity) && 
                        !(noCollisionSame && entity.areSameType((Entity)this)) && 
                        CollisionList.ON_TOP_ENTITY.collision(position, entity.getCollision(), entity.getPosition()))
                    entity.move(world, realMove.x, realMove.y, realMove.z);
            }
        }

        return realMove;
    }

    private double moveAxis(World world, double dx, double dy, double dz) {
        double stepSize = 0.1;
        double distance = Math.abs(dx + dy + dz);
        int steps = (int)(distance / stepSize) + 1;
        double stepX = dx / steps;
        double stepY = dy / steps;
        double stepZ = dz / steps;

        double moveAxis = 0;

        for (int i = 0; i < steps; i++) {
            position.x += stepX;
            position.y += stepY;
            position.z += stepZ;

            boolean colliding = isCollidingBlock(world);
            if (!colliding)
                colliding = isCollidingEntity(world, stepX, stepY, stepZ);
            if (colliding) {
                position.x -= stepX;
                position.y -= stepY;
                position.z -= stepZ;
                if (dx != 0)
                    velocity.x = 0;
                if (dy != 0)
                    velocity.y = 0;
                if (dz != 0)
                    velocity.z = 0;
                break;
            }

            moveAxis += stepX + stepY + stepZ;
        }
        return moveAxis;
    }

    protected boolean isCollidingBlock(World world) {
        if (!noCollision(this) && !noCollisionBlock(this)) {
            int minX = (int)Math.floor(position.x - 0.5);
            int maxX = (int)Math.ceil(position.x + 0.5);
            int minY = (int)Math.floor(position.y - 0.5);
            int maxY = (int)Math.ceil(position.y + 0.5);
            int minZ = (int)Math.floor(position.z - 0.5);
            int maxZ = (int)Math.ceil(position.z + 0.5);

            Vector blockPos = new Vector();
            for (int z = minZ; z <= maxZ; z++) {
                blockPos.z = z + 0.5;
                for (int y = minY; y <= maxY; y++) {
                    blockPos.y = y + 0.5;
                    for (int x = minX; x <= maxX; x++) {
                        Block block = world.getBlock(x, y, z);
                        if (block != null  && !noCollision(block)) {
                            blockPos.x = x + 0.5;
                            if (collision(position, block, blockPos))
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean isCollidingEntity(World world, double moveX, double moveY, double moveZ) {
        Vector move = new Vector(moveX, moveY, moveZ);
        boolean noCollision = noCollision(this);
        boolean noCollisionEntity = noCollisionEntity(this);
        boolean noCollisionSame = noCollisionSame(this);
        for (Entity entity : world.getEntities())
            if (entity != this)
                if (collision(position, entity, entity.getPosition())) {
                    entity.onEntityCollision(world, (Entity)this);
                    ((Entity)this).onEntityCollision(world, entity);
                    if (!noCollision && !noCollisionEntity && !noCollision(entity) && !noCollisionEntity(entity) && 
                            !(noCollisionSame && entity.areSameType((Entity)this))) {
                        if (moveX != 0 || moveY != 0 || moveZ != 0)
                            entity.onPush(world, move, (Entity)this);
                        if (collision(position, entity, entity.getPosition()))
                            return true;
                    }
                }
        return false;
    }

    private boolean noCollision(ObjectInstance objectInstance) {
        return (objectInstance.getProperty(NO_COLLISION) != null);
    }

    private boolean noCollisionBlock(ObjectInstance objectInstance) {
        return (objectInstance.getProperty(NO_COLLISION_BLOCK) != null);
    }

    private boolean noCollisionEntity(ObjectInstance objectInstance) {
        return (objectInstance.getProperty(NO_COLLISION_ENTITY) != null);
    }

    private boolean noCollisionSame(ObjectInstance objectInstance) {
        return (objectInstance.getProperty(NO_COLLISION_SAME) != null);
    }
}
