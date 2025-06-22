package world;

import objects.block.Block;
import objects.block.BlockType;
import objects.entity.Entity;
import objects.entity.EntityAction;
import objects.entity.EntityType;
import objects.entity.Player;
import tools.Vector;
import world.WorldLoader.WorldData;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import engine.GameControls;
import graphics.fog.FogManager;

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

    private Map<Vector, Block> fogMap = new HashMap();

    private HashMap<String,WorldData> worlds = new HashMap<>();
    private ArrayList<Runnable> afterUpdateTasks = new ArrayList<>();

    private String currentWorld;
    private String startingWorld;
    private int startingSpawnPoint;

    public World(String worldPath) {
        loadWorld0(worldPath);
    }

    public String getCurrentWorld() {
        return currentWorld;
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

    public Map<Vector, Block> getFogMap() {
        return fogMap;
    }

    public void loadWorld(String filename, int spawnPoint) {
        currentWorld = filename;
        WorldData data;
        if (worlds.containsKey(filename)) {
            data = worlds.get(filename);
            blocks = data.blocks();
            entities = data.entities();
            spawnPoints = data.spawnPoints();
            if (spawnPoints.isEmpty())
                throw new IllegalStateException("No spawn point for " + filename);
            afterUpdateTasks = new ArrayList<>();
            spawnPlayer(spawnPoint);
        }
        else {
            data = WorldLoader.loadWorld(filename, BLOCK_TYPES, ENTITY_TYPES);
            blocks = data.blocks();
            entities = data.entities();
            spawnPoints = data.spawnPoints();
            if (spawnPoints.isEmpty())
                throw new IllegalStateException("No spawn point for " + filename);
            afterUpdateTasks = new ArrayList<>();
            spawnPlayer(spawnPoint);
            start();
            worlds.put(filename, data);
        }

        FogManager fogManager = new FogManager();
        fogMap = fogManager.getFogMap(blocks);
    }

    public void loadWorld(String filename) {
        loadWorld(filename, -1);
    }

    public void loadWorld0(String filename, int spawnPoint) {
        worlds.clear();
        startingWorld = filename;
        startingSpawnPoint = spawnPoint;
        loadWorld(filename, spawnPoint);
    }

    public void loadWorld0(String filename) {
        loadWorld0(filename,-1);
    }

    public void reloadWorld() {
        loadWorld0(startingWorld, startingSpawnPoint);
    }

    private void spawnPlayer(int spawnPoint) {
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

        updateControls();

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
        afterUpdateTasks = new ArrayList<>();
    }

    public void activate(int network) {
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
        for (Entity entity : entities)
            entity.onActivated(this, network);
    }

    public void desactivate(int network) {
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
        for (Entity entity : entities)
            entity.onDesactivated(this, network);
    }

    private void updateControls() {
        Player player = getPlayer();
        ArrayList<EntityAction> actions = new ArrayList<>();
        if (GameControls.isPressed(KeyEvent.VK_Z))
            actions.add(EntityAction.TOP);
        if (GameControls.isPressed(KeyEvent.VK_S))
            actions.add(EntityAction.BOTTOM);
        if (GameControls.isPressed(KeyEvent.VK_Q))
            actions.add(EntityAction.LEFT);
        if (GameControls.isPressed(KeyEvent.VK_D))
            actions.add(EntityAction.RIGHT);
        if (GameControls.isPressed(KeyEvent.VK_A) || GameControls.isPressed(KeyEvent.VK_SPACE))
            actions.add(EntityAction.JUMP);
        if (GameControls.isPressed(KeyEvent.VK_E))
            actions.add(EntityAction.INTERACT);
        if (GameControls.isPressed(KeyEvent.VK_R))
            reloadWorld();
        
        if (!actions.isEmpty())
            player.doActions(this, actions.toArray(new EntityAction[0]));
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
                        b.onStart(this, pos);
                    }
                }
            }
        }

        for (Entity entity : entities)
            entity.onStart(this);
    }

    public int getX() {
        if (blocks == null)
            return 0;
        return blocks.length;
    }

    public int getY() {
        if (blocks == null)
            return 0;
        return blocks[0].length;
    }

    public int getZ() {
        if (blocks == null)
            return 0;
        return blocks[0][0].length;
    }
}
