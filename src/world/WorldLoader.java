package world;

import graphics.Texture;
import graphics.shape.Cube;
import graphics.shape.Shape;
import objects.block.*;
import objects.entity.Entity;
import objects.entity.EntityType;
import objects.entity.Player;
import tools.PathManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/** Loads worlds from human-readable text files. */
public class WorldLoader {

    /* Small record that bundles both arrays for the World ctor */
    public record WorldData(Block[][][] blocks, ArrayList<Entity> entities) {}

    /* ------------------------------------------------------------------ */
    /*  Public helpers                                                     */
    /* ------------------------------------------------------------------ */

    public static ArrayList<BlockType> loadBlockTypes() {
        return BlockTypeFactory.loadBlockTypes();
    }

    /** Parses <code>WORLD_PATH + file</code> and returns blocks + entities. */
    public static WorldData loadWorld(String file, ArrayList<BlockType> blockTypes, int spawnPoint) {

        Block[][][]       blocks        = null;
        ArrayList<Entity> entities      = new ArrayList<>();
        ArrayList<int[]>  playerSpawns  = new ArrayList<>();

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
                            playerSpawns.add(new int[]{x, y, z});

                        } else {                                    // ordinary block
                            blocks[x][y][z] = loadBlock(tok, blockTypes);
                        }
                    }
                }
            }

            /* ---- create exactly ONE player at a random spawn ---- */
            if (!playerSpawns.isEmpty()) {
                BufferedImage skin = ImageIO.read(
                        WorldLoader.class.getResource(
                                "/resources/textures/outlined/blue-block.png"));
                Shape    cube = new Cube();
                Texture  skinTex = new Texture(cube, skin);
                EntityType playerType = new EntityType("player");
                playerType.addTexture(skinTex);

                int[] spot;
                if (spawnPoint < 0)
                    spot = playerSpawns.get((int) (Math.random() * playerSpawns.size()));
                else
                    spot = playerSpawns.get(spawnPoint%playerSpawns.size());

                entities.add(new Player(
                        playerType,
                        spot[0] + 0.5,
                        spot[1] + 0.5,
                        spot[2] + 0.5));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading world '" + file + "'", e);
        }

        return new WorldData(blocks, entities);
    }

    public static WorldData loadWorld(String file, ArrayList<BlockType> blockTypes) {
        return loadWorld(file, blockTypes,-1);
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
