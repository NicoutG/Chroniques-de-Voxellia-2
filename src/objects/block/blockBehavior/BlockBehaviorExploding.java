package objects.block.blockBehavior;

import objects.block.Block;
import tools.Vector;
import world.World;

public class BlockBehaviorExploding extends BlockBehaviorActivable {
    private final static String RADIUS = "radius";
    private final static String DESTRUCTIBLE_PROPERTY = "destructible";
    private final int DEFAULT_RADIUS;

    public BlockBehaviorExploding() {
        DEFAULT_RADIUS = 1;
    }

    public BlockBehaviorExploding(int defaultRadius) {
        DEFAULT_RADIUS = defaultRadius;
    }

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(RADIUS, DEFAULT_RADIUS);
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        int radius = getRadius(block);
        int minX = Math.max(0,(int)position.x - radius);
        int maxX = Math.min(world.getX() - 1, (int)position.x + radius);
        int minY = Math.max(0,(int)position.y - radius);
        int maxY = Math.min(world.getY() - 1, (int)position.y + radius);
        int minZ = Math.max(0,(int)position.z - radius);
        int maxZ = Math.min(world.getZ() - 1, (int)position.z + radius);
        for (int z = minZ; z <= maxZ; z++)
            for (int y = minY; y <= maxY; y++)
                for (int x = minX; x <= maxX; x++) {
                    Block blockWorld = world.getBlock(x,y,z);
                    if (blockWorld != null && blockWorld.getProperty(DESTRUCTIBLE_PROPERTY) != null)
                        world.getBlocks()[x][y][z] = null;
                }
    }

    public int getRadius(Block block) {
        Object state = block.getState(RADIUS);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
