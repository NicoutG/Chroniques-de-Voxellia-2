package world;

import objects.block.Block;
import objects.block.BlockType;
import objects.entity.Entity;
import objects.entity.Player;
import tools.Vector;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.GameControls;

public class World {

    /* ------------------------------------------------------------------ */
    /* STATIC FIELDS & CONSTRUCTORS */
    /* ------------------------------------------------------------------ */

    /** All block-types (kept once for the whole JVM). */
    private static final ArrayList<BlockType> BLOCK_TYPES = WorldLoader.loadBlockTypes();

    public static World loadWorld(String filename) {
        WorldLoader.WorldData data = WorldLoader.loadWorld(filename, BLOCK_TYPES);
        return new World(data.blocks(), data.entities());
    }

    /* ------------------------------------------------------------------ */
    /* INSTANCE STATE & API */
    /* ------------------------------------------------------------------ */

    private final Block[][][] blocks;
    private final ArrayList<Entity> entities;

    public World(Block[][][] blocks, ArrayList<Entity> entities) {
        this.blocks = blocks;
        this.entities = entities;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

    public Block getBlock(int x, int y, int z) {
        if (0 <= x && x < blocks.length &&
                0 <= y && y < blocks[x].length &&
                0 <= z && z < blocks[x][y].length)
            return blocks[x][y][z];
        return null;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        for (Entity e : entities)
            if (e instanceof Player p)
                return p;
        return null;
    }

    /* -------------------------- update loop -------------------------- */

    public void update() {
        updateControls();

        Vector pos = new Vector();
        for (int z = 0; z < blocks[0][0].length; z++) {
            pos.z = z + 0.5;
            for (int y = 0; y < blocks[0].length; y++) {
                pos.y = y + 0.5;
                for (int x = 0; x < blocks.length; x++) {
                    Block b = blocks[x][y][z];
                    if (b != null) {
                        pos.x = x + 0.5;
                        b.onUpdate(this, pos);
                    }
                }
            }
        }
        for (Entity e : entities)
            e.onUpdate(this);
    }

    public void activateBlocks(int network) {
        Vector pos = new Vector();
        for (int z = 0; z < blocks[0][0].length; z++) {
            pos.z = z + 0.5;
            for (int y = 0; y < blocks[0].length; y++) {
                pos.y = y + 0.5;
                for (int x = 0; x < blocks.length; x++) {
                    Block b = blocks[x][y][z];
                    if (b != null) {
                        pos.x = x + 0.5;
                        b.onActivated(this, new Vector(x + 0.5, y + 0.5, z + 0.5), network);
                    }
                }
            }
        }
    }

    public void desactivateBlocks(int network) {
        Vector pos = new Vector();
        for (int z = 0; z < blocks[0][0].length; z++) {
            pos.z = z + 0.5;
            for (int y = 0; y < blocks[0].length; y++) {
                pos.y = y + 0.5;
                for (int x = 0; x < blocks.length; x++) {
                    Block b = blocks[x][y][z];
                    if (b != null) {
                        pos.x = x + 0.5;
                        b.onDesactivated(this, new Vector(x + 0.5, y + 0.5, z + 0.5), network);
                    }
                }
            }
        }
    }

    private void updateControls() {
        Player player = getPlayer();
        if (GameControls.isPressed(KeyEvent.VK_UP))
            player.move(this, 0, -0.2, 0);
        if (GameControls.isPressed(KeyEvent.VK_DOWN))
            player.move(this, 0, 0.2, 0);
        if (GameControls.isPressed(KeyEvent.VK_LEFT))
            player.move(this, -0.2, 0, 0);
        if (GameControls.isPressed(KeyEvent.VK_RIGHT))
            player.move(this, 0.2, 0, 0);
        if (GameControls.isPressed(KeyEvent.VK_SPACE))
            player.addVelocity(0,0,1.5);
    }
}
