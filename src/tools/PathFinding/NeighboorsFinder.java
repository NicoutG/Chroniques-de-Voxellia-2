package tools.PathFinding;

import java.util.ArrayList;

import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public abstract class NeighboorsFinder {

    public abstract ArrayList<Vector> getNeighboors(World world, Entity entity, Vector position, Vector destination);

    protected boolean isValidNeighboor(World world, Entity entity, Vector pos) {
        if (0 <= pos.x && pos.x < world.getX() && 0 <= pos.y && pos.y < world.getY() && 0 <= pos.z && pos.z < world.getZ()) {
            Block block = world.getBlock((int)pos.x, (int)pos.y, (int)pos.z);
            if (block == null || !entity.possibleCollisionWith(block) || Entity.noCollisionBlock(entity) || Entity.noCollision(entity)) {
                if (!Entity.noCollisionEntity(entity) && !Entity.noCollision(entity)) {
                    ArrayList<Entity> entities = world.getEntities();
                    for (Entity otherEntity : entities) 
                        if (entity.possibleCollisionWith(otherEntity))
                            if (entity.getCollision().collision(pos, otherEntity.getCollision(), otherEntity.getPosition()))
                                return false;
                }
                return true;
            }
        }
        return false;
    }
    
}
