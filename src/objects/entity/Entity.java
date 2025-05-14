package objects.entity;

import java.util.ArrayList;
import tools.Vector;

import model.world.World;
import objects.ObjectInstance;
import objects.block.Block;
import tools.*;

public class Entity extends ObjectInstance<EntityType>{
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

    public void setPosition(double x, double y, double z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
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
                    if (block != null) {
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
