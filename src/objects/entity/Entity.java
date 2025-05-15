package objects.entity;

import objects.ObjectInstance;
import objects.block.Block;
import tools.*;
import world.World;

public class Entity extends ObjectInstance<EntityType>{
    protected static final double MAX_VELOCITY = 2.5;

    protected Vector position;
    protected Vector velocity = new Vector();

    public Entity(EntityType type,double x, double y, double z) {
        super(type);
        position = new Vector();
        setPosition(x,y,z);
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

    public void onUpdate(World world) {
        double coef = 0.9;
        setVelocity(coef * velocity.x, coef * velocity.y, coef * velocity.z -0.4);
        addVelocity(0, 0, 0.1);
        move(world, velocity.x, velocity.y, velocity.z);
        notifyCloseBlocks(world);
    }

    public void notifyCloseBlocks(World world) {
        int x = (int)position.x;
        int y = (int)position.y;
        int z = (int)position.z;
        for (int i = x - 1; i <= x + 1; i++)
            for (int j = y - 1; j <= y + 1; j++)
                for (int k = z - 1; k <= z + 1; k++) {
                    Block block = world.getBlock(i, j, k);
                    if (block != null)
                        block.onEntityClose(world, new Vector(i + 0.5, j + 0.5, k + 0.5), this);
                }
    }

    public void move(World world, double moveX, double moveY, double moveZ) {
        if (getProperty("noCollision") != null) {
            position.x += moveX;
            position.y += moveY;
            position.z += moveZ;
            return;
        }

        moveAxis(world, moveX, 0, 0);
        moveAxis(world, 0, moveY, 0);
        moveAxis(world, 0, 0, moveZ);
    }

    private void moveAxis(World world, double dx, double dy, double dz) {
        double stepSize = 0.1;
        double distance = Math.abs(dx + dy + dz);
        int steps = (int)(distance / stepSize) + 1;
        double stepX = dx / steps;
        double stepY = dy / steps;
        double stepZ = dz / steps;

        for (int i = 0; i < steps; i++) {
            position.x += stepX;
            position.y += stepY;
            position.z += stepZ;

            if (isCollidingBlock(world)) {
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
        }
    }

    private boolean isCollidingBlock(World world) {
        int minX = (int)Math.floor(position.x - 0.5);
        int maxX = (int)Math.ceil(position.x + 0.5);
        int minY = (int)Math.floor(position.y - 0.5);
        int maxY = (int)Math.ceil(position.y + 0.5);
        int minZ = (int)Math.floor(position.z - 0.5);
        int maxZ = (int)Math.ceil(position.z + 0.5);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlock(x, y, z);
                    if (block != null  && block.getProperty("noCollision") == null) {
                        Vector blockPos = new Vector(x + 0.5, y + 0.5, z + 0.5);
                        if (getCollision().collision(position, block.getCollision(), blockPos))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
