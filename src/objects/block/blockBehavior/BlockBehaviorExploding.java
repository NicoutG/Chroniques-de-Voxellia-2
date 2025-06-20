package objects.block.blockBehavior;

import java.util.ArrayList;

import audio.SoundManager;
import audio.SoundType;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorExploding extends BlockBehaviorActivable {
    private final static String RADIUS = "radius";
    private final static String DESTRUCTIBLE_PROPERTY = "destructible";
    private final static String EXPLOSION_BLOCK = "explosionBlock";
    private final double DEFAULT_RADIUS;

    public BlockBehaviorExploding() {
        DEFAULT_RADIUS = 1.0;
    }

    public BlockBehaviorExploding(double defaultRadius) {
        DEFAULT_RADIUS = defaultRadius;
    }

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(RADIUS, DEFAULT_RADIUS);
        block.setState(EXPLOSION_BLOCK, "explosion");
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        SoundManager.playSoundFromCoordinates(SoundType.EXPLOSION, position.x, position.y, position.z);
        double radius = getRadius(block);
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
                    double distance = dif.x * dif.x + dif.y * dif.y + dif.z * dif.z;
                    if (distance <= radius * radius) {
                        Block blockWorld = world.getBlock(x,y,z);
                        if (blockWorld == null || (blockWorld != null && blockWorld.getProperty(DESTRUCTIBLE_PROPERTY) != null))
                            world.getBlocks()[x][y][z] = world.getBlock(getExplosionBlock(block));
                    }
                }
            }
        }

        ArrayList<Entity> entities = world.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            dif = position.sub(entity.getPosition());
            double distance = dif.x * dif.x + dif.y * dif.y + dif.z * dif.z;
            if (distance <= radius * radius) {
                entity.onDeath(world);
                entities.remove(i);
                i--;
            }
        }
    }

    public double getRadius(Block block) {
        Object state = block.getState(RADIUS);
        if (state != null && state instanceof Double)
            return (double)state;
        return -1;
    }

    public String getExplosionBlock(Block block) {
        Object state = block.getState(EXPLOSION_BLOCK);
        if (state != null && state instanceof String)
            return (String)state;
        return null;
    }
}
