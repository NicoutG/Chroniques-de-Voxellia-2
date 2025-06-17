package objects.block.blockBehavior;

import audio.SoundManager;
import audio.SoundType;
import objects.block.Block;
import objects.entity.Entity;
import objects.entity.Player;
import tools.Vector;
import world.World;

public class BlockBehaviorChangeWorld extends BlockBehaviorActivable {
    protected final static String NEW_WORLD = "world";
    protected final static String SPAWNPOINT = "spawnPoint";

     @Override
    protected void activate(World world, Block block, Vector position, int network) {
        block.setIndexTexture(1);
        SoundManager.playSoundFromCoordinates(SoundType.WORLD_LOADER_ACTIVATION, position.x, position.y, position.z);
    }
    
    @Override
    protected void desactivate(World world, Block block, Vector position, int network) {
        block.setIndexTexture(0);
        SoundManager.playSoundFromCoordinates(SoundType.WORLD_LOADER_ACTIVATION, position.x, position.y, position.z);
    }

    @Override
    public void onAttachTo(Block block) {
        block.setState(NEW_WORLD, "");
        block.setState(SPAWNPOINT, -1);
    }

    @Override
    public void onEntityClose(World world, Block block, Vector position, Entity entity) {
        if (entity instanceof Player && getActivationState(block)) {
            int xBlock = (int)position.x;
            int yBlock = (int)position.y;
            int zBlock = (int)position.z;
            int xEntity = (int)entity.getX();
            int yEntity = (int)entity.getY();
            int zEntity = (int)entity.getZ();
            if (xBlock == xEntity && yBlock == yEntity && zBlock == zEntity - 1) {
                String newWorld = getNewWorld(block);
                int spawnPoint = getSpawnPoint(block);
                loadWorld(world, newWorld, spawnPoint);
            }
        }
    }

    protected void loadWorld(World world, String newWorld, int spawnPoint) {
        world.executeAfterUpdate(() -> world.loadWorld(newWorld, spawnPoint));
    }

    protected String getNewWorld(Block block) {
        Object state = block.getState(NEW_WORLD);
        if (state != null && state instanceof String)
            return (String)state;
        return "";
    }

    protected int getSpawnPoint(Block block) {
        Object state = block.getState(SPAWNPOINT);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
