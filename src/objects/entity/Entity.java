package objects.entity;

import objects.ObjectInstanceMovable;
import objects.block.Block;
import objects.entity.entityBehavior.EntityBehavior;
import tools.*;
import world.World;

public class Entity extends ObjectInstanceMovable<EntityType, Entity, EntityBehavior>{

    public Entity(EntityType type,double x, double y, double z) {
        super(type,x,y,z);
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
    }
}
