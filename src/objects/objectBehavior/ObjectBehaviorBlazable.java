package objects.objectBehavior;

import java.util.ArrayList;
import java.util.Random;

import objects.ObjectInstance;
import objects.ObjectType;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class ObjectBehaviorBlazable<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, ?>,
    B extends ObjectBehavior<T, I, B>
    > extends ObjectBehavior<T,I,B> {

    private final static String BURNABLE_PROPERTY = "burnable";
    private final static String BURNING = "burning";
    private final static String FIRE_BLOCK_NAME = "fire";
    private final static double RADIUS_FIRE = 1.9;
    private final static double PROBA_FIRE = 0.01;

    @Override
    public void onAttachTo(I instance) {

    }

    public void onUpdate(World world, I instance, Vector position) {
        double radius = RADIUS_FIRE;
        int minX = Math.max(0,(int)(position.x - radius));
        int maxX = Math.min(world.getX() - 1, (int)(position.x + radius));
        int minY = Math.max(0,(int)(position.y - radius));
        int maxY = Math.min(world.getY() - 1, (int)(position.y + radius));
        int minZ = Math.max(0,(int)(position.z - radius));
        int maxZ = Math.min(world.getZ() - 1, (int)(position.z + radius));
        Vector dif = new Vector();
        for (int z = minZ; z <= maxZ; z++) {
            dif.z = z + 0.5 - position.z;
            for (int y = minY; y <= maxY; y++) {
                dif.y = y + 0.5 - position.y;
                for (int x = minX; x <= maxX; x++) {
                    dif.x = x + 0.5 - position.x;
                    double distance = Math.sqrt(dif.x * dif.x + dif.y * dif.y + dif.z * dif.z);
                    if (distance <= radius) {
                        Block blockWorld = world.getBlock(x,y,z);
                        if (blockWorld != null && blockWorld.getProperty(BURNABLE_PROPERTY) != null) {
                            Random random = new Random();
                            double proba = getProba(distance);
                            if (random.nextDouble() <= proba) {
                                world.getBlocks()[x][y][z] = null;
                                Entity fire = world.getEntity(FIRE_BLOCK_NAME);
                                fire.setPosition(x + 0.5, y + 0.5, z + 0.5);
                                world.executeAfterUpdate(() -> world.getEntities().add(fire));
                            }
                        }
                    }
                }
            }
        }

        ArrayList<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            dif = position.sub(entity.getPosition());
            double distance = Math.sqrt(dif.x * dif.x + dif.y * dif.y + dif.z * dif.z);
            if (distance <= radius) {
                if (entity.getProperty(BURNABLE_PROPERTY) != null && !getBurning(entity)) {
                    Random random = new Random();
                    double proba = getProba(distance);
                    if (random.nextDouble() <= proba) {
                        entity.setState(BURNING, true);
                        entity.onDeath(world);
                        world.executeAfterUpdate(() -> entities.remove(entity));
                        Entity fire = world.getEntity(FIRE_BLOCK_NAME);
                        fire.setPosition(entity.getX(), entity.getY(), entity.getZ());
                        world.executeAfterUpdate(() -> entities.add(fire));
                    }
                }
            }
        }
    }

    private double getProba(double distance) {
        double coef = (RADIUS_FIRE - distance) / RADIUS_FIRE;
        return PROBA_FIRE + (1 - PROBA_FIRE)  * coef * coef * coef;
    }

    public boolean getBurning(ObjectInstance objectInstance) {
        Object state = objectInstance.getState(BURNING);
        if (state != null && state instanceof Boolean)
            return (boolean)state;
        return false;
    }
    
}
