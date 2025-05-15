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
}
