package world;

import graphics.Texture;
import graphics.shape.Cube;
import graphics.shape.Shape;
import objects.block.*;
import objects.entity.Entity;
import objects.entity.EntityType;
import objects.entity.Player;
import tools.PathManager;
import tools.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/** Loads worlds from human-readable text files. */
public class WorldLoader {

    /* Small record that bundles both arrays for the World ctor */
    public record WorldData(Block[][][] blocks, ArrayList<Entity> entities, ArrayList<Vector> spawnPoints) {}

    /* ------------------------------------------------------------------ */
    /*  Public helpers                                                     */
    /* ------------------------------------------------------------------ */

    public static ArrayList<BlockType> loadBlockTypes() {
        return BlockTypeFactory.loadBlockTypes();
    }

    /** Parses <code>WORLD_PATH + file</code> and returns blocks + entities. */
    public static WorldData loadWorld(String file, ArrayList<BlockType> blockTypes) {

        Block[][][]       blocks        = null;
        ArrayList<Entity> entities      = new ArrayList<>();
        ArrayList<Vector>  spawnPoints  = new ArrayList<>();

        try {
            String[] lines = Files.readString(Paths.get(PathManager.WORLD_PATH + file))
                                   .split("\r?\n");

            /* ---- dimensions header ---- */
            String[] dims = lines[0].trim().split(" ");
            int dimX = Integer.parseInt(dims[0]);
            int dimY = Integer.parseInt(dims[1]);
            int dimZ = Integer.parseInt(dims[2]);
            blocks   = new Block[dimX][dimY][dimZ];

            /* ---- quick sanity check ---- */
            int expected = 1 + dimZ * (dimY + 1);
            if (lines.length != expected)
                throw new IllegalStateException("Malformed world file " + file);

            /* ---- walk through every layer ---- */
            int line = 1;
            for (int z = 0; z < dimZ; z++) {
                line++;                       // skip blank separator
                for (int y = 0; y < dimY; y++) {

                    String[] toks = lines[line++].trim().split(" ");
                    if (toks.length != dimX)
                        throw new IllegalStateException("Malformed row in " + file);

                    for (int x = 0; x < dimX; x++) {
                        String tok = toks[x];

                        if (tok.equals(".")) {                       // air
                            blocks[x][y][z] = null;

                        } else if (tok.length() > 0 &&
                                   (tok.charAt(0) == 'p' || tok.charAt(0) == 'P')) {
                            /* spawn point → remember coords, store AIR */
                            blocks[x][y][z] = null;
                            spawnPoints.add(new Vector(x+0.5,y+0.5,z+0.5));

                        } else {                                    // ordinary block
                            blocks[x][y][z] = loadBlock(tok, blockTypes);
                        }
                    }
                }
            }

            /* ---- create exactly ONE player at a random spawn ---- */
            if (!spawnPoints.isEmpty()) {
                BufferedImage skin = ImageIO.read(
                        WorldLoader.class.getResource(
                                "/resources/textures/outlined/blue-block.png"));
                Shape    cube = new Cube();
                Texture  skinTex = new Texture(cube, skin);
                EntityType playerType = new EntityType("player");
                playerType.addTexture(skinTex);

                entities.add(new Player(
                        playerType,
                        0,0,0));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading world '" + file + "'", e);
        }

        return new WorldData(blocks, entities, spawnPoints);
    }

    /* ------------------------------------------------------------------ */
    /*  Internal helpers                                                   */
    /* ------------------------------------------------------------------ */

    private static Block loadBlock(String token,
                                   ArrayList<BlockType> blockTypes) {

        String[] parts = token.split("/");
        Block block = blockTypes.get(Integer.parseInt(parts[0])).getInstance();

        for (int i = 1; i < parts.length; i++) {
            String[] nv = parts[i].split("=");
            setState(block, nv[0], nv[1]);
        }
        return block;
    }

    private static void setState(Block block, String name, String value) {
        Object cur = block.getState(name);
        if (cur instanceof Double) block.setState(name, Double.parseDouble(value));
        else if (cur instanceof Integer) block.setState(name, Integer.parseInt(value));
        else if (cur instanceof Boolean) block.setState(name, Boolean.parseBoolean(value));
        else block.setState(name, value);
    }
}
