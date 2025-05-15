package model.world;

import javax.imageio.ImageIO;

import graphics.Texture;
import graphics.shape.Cube;
import graphics.shape.Shape;
import objects.block.Block;
import objects.block.BlockType;
import objects.entity.Entity;
import objects.entity.EntityType;
import objects.entity.Player;
import tools.Vector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

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

    /* keep the demo generator exactly as before ------------------------ */

    public static World createDemoWorld() {
        int sx = 25, sy = 25, sz = 2;
        Block[][][] blocks = new Block[sx][sy][sz + 10];

        try {
            BufferedImage player0 = ImageIO.read(World.class.getResource(
                    "/resources/textures/outlined/purple-block.png"));

            Shape cube = new Cube();
            Texture playerTex = new Texture(cube, player0);
            EntityType playerTp = new EntityType("player");
            playerTp.addTexture(playerTex);

            for (int z = 0; z < sz; z++)
                for (int y = 0; y < sy; y++)
                    for (int x = 0; x < sx; x++)
                        blocks[x][y][z] = BLOCK_TYPES.get((x >= 14 && x <= 16 && z == 1 && y == 9) ? 5 : z % 2)
                                .getInstance();

            ArrayList<Entity> entities = new ArrayList<>();
            entities.add(new Player(playerTp, 14.5, 8.5, 2.5));

            return new World(blocks, entities);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load demo textures", e);
        }
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
}
