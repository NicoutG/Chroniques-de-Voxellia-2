package world;

import objects.block.Block;
import objects.block.BlockType;
import objects.entity.Entity;
import objects.entity.EntityType;
import objects.entity.Player;
import tools.Vector;
import world.WorldLoader.WorldData;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import engine.GameControls;

public class World {

    /* ------------------------------------------------------------------ */
    /* STATIC FIELDS & CONSTRUCTORS */
    /* ------------------------------------------------------------------ */

    /** All block-types (kept once for the whole JVM). */
    private static final ArrayList<BlockType> BLOCK_TYPES = WorldLoader.loadBlockTypes();
    private static final ArrayList<EntityType> ENTITY_TYPES = WorldLoader.loadEntityTypes();

    /* ------------------------------------------------------------------ */
    /* INSTANCE STATE & API */
    /* ------------------------------------------------------------------ */

    private Block[][][] blocks;
    private ArrayList<Entity> entities;
    private ArrayList<Vector> spawnPoints;

    private HashMap<String,WorldData> worlds = new HashMap<>();
    private ArrayList<Runnable> afterUpdateTasks = new ArrayList<>();

    public World(String worldPath) {
        loadWorld(worldPath);
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

    public Block getBlock(String name) {
        for (BlockType blockType : BLOCK_TYPES)
            if (blockType.getName().equals(name))
                return blockType.getInstance();
        return null;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Entity getEntity(String name) {
        for (EntityType entityType : ENTITY_TYPES)
            if (entityType.getName().equals(name))
                return entityType.getInstance();
        return null;
    }

    public Player getPlayer() {
        for (Entity e : entities)
            if (e instanceof Player p)
                return p;
        return null;
    }

    public void clearWorlds() {
        worlds.clear();
    }

    public void loadWorld(String filename, int spawnPoint) {
        WorldData data;
        if (worlds.containsKey(filename)) {
            data = worlds.get(filename);
            blocks = data.blocks();
            entities = data.entities();
            spawnPoints = data.spawnPoints();
        }
        else {
            data = WorldLoader.loadWorld(filename, BLOCK_TYPES, ENTITY_TYPES);
            blocks = data.blocks();
            entities = data.entities();
            spawnPoints = data.spawnPoints();
            start();
            worlds.put(filename, data);
        }
        if (spawnPoints.isEmpty())
            throw new IllegalStateException("No spawn point for " + filename);
        afterUpdateTasks = new ArrayList<>();
        spawnPlayer(spawnPoint);
    }

    public void loadWorld(String filename) {
        loadWorld(filename, -1);
    }

    public void spawnPlayer(int spawnPoint) {
        Player player = getPlayer();
        Vector position;
        if (spawnPoint < 0 || spawnPoints.size() <= spawnPoint)
            position = spawnPoints.get((int) (Math.random() * spawnPoints.size()));
        else
            position = spawnPoints.get(spawnPoint);
        player.setPosition(position.x, position.y, position.z);
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

        executeTasks();
    }

    public void executeAfterUpdate(Runnable task) {
        afterUpdateTasks.add(task);
    }

    public void executeTasks() {
        for (Runnable task : afterUpdateTasks)
            task.run();
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
        double dx = 0, dy = 0;

        if (GameControls.isPressed(KeyEvent.VK_Z))
            dy -= 0.2;
        if (GameControls.isPressed(KeyEvent.VK_S))
            dy += 0.2;
        if (GameControls.isPressed(KeyEvent.VK_Q))
            dx -= 0.2;
        if (GameControls.isPressed(KeyEvent.VK_D))
            dx += 0.2;

        // Normalize movement if moving diagonally
        if (dx != 0 && dy != 0) {
            double norm = Math.sqrt(dx * dx + dy * dy);
            dx = dx / norm * 0.2;
            dy = dy / norm * 0.2;
        }

        if (dx != 0 || dy != 0)
            player.move(this, dx, dy, 0);

        if (GameControls.isPressed(KeyEvent.VK_A) || GameControls.isPressed(KeyEvent.VK_SPACE))
            player.jump(this);
        if (GameControls.isPressed(KeyEvent.VK_E))
            player.interact(this);
    }

    private void start() {
        Vector pos = new Vector();
        for (int z = 0; z < blocks[0][0].length; z++) {
            pos.z = z + 0.5;
            for (int y = 0; y < blocks[0].length; y++) {
                pos.y = y + 0.5;
                for (int x = 0; x < blocks.length; x++) {
                    Block b = blocks[x][y][z];
                    if (b != null) {
                        pos.x = x + 0.5;
                        b.onStart(this, new Vector(x + 0.5, y + 0.5, z + 0.5));
                    }
                }
            }
        }

        for (Entity entity : entities)
            entity.onStart(this);
    }
}
