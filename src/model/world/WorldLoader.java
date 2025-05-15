package model.world;

import graphics.Texture;
import graphics.shape.Cube;
import graphics.shape.Shape;
import objects.block.*;
import objects.entity.Entity;
import objects.entity.EntityType;
import objects.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/** Loads worlds from human-readable text files. */
public class WorldLoader {

    private static final String WORLD_PATH = "src/resources/worlds/";

    /* ---------- tiny record used as a return-value ---------- */

    public record WorldData(Block[][][] blocks, ArrayList<Entity> entities) {
    }

    /* ---------- still needed by other code ---------- */
    public static ArrayList<BlockType> loadBlockTypes() {
        return BlockTypeFactory.loadBlockTypes();
    }

    /* ------------------------------------------------------------------ */
    /* PUBLIC API */
    /* ------------------------------------------------------------------ */

    public static WorldData loadWorld(String file,
            ArrayList<BlockType> blockTypes) {

        Block[][][] blocks = null;
        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<int[]> playerSpawns = new ArrayList<>();

        try {
            String[] lines = Files.readString(Paths.get(WORLD_PATH + file))
                    .split("\r?\n");

            /* ----- parse header dims ----- */
            String[] dims = lines[0].trim().split(" ");
            int dimX = Integer.parseInt(dims[0]);
            int dimY = Integer.parseInt(dims[1]);
            int dimZ = Integer.parseInt(dims[2]);
            blocks = new Block[dimX][dimY][dimZ];

            int expectedLines = 1 + dimZ * (dimY + 1);
            if (lines.length != expectedLines)
                throw new IllegalStateException("Malformed world file: " + file);

            int line = 1;
            for (int z = 0; z < dimZ; z++) {
                line++; // skip blank separator
                for (int y = 0; y < dimY; y++) {

                    String[] tokens = lines[line++].trim().split(" ");
                    if (tokens.length != dimX)
                        throw new IllegalStateException("Malformed row in " + file);

                    for (int x = 0; x < dimX; x++) {
                        String tok = tokens[x];
                        if (tok.equals(".")) {
                            blocks[x][y][z] = null; // empty air
                        } else if (tok.equalsIgnoreCase("p")) {
                            blocks[x][y][z] = null; // spawn spot
                            playerSpawns.add(new int[] { x, y, z });
                        } else {
                            blocks[x][y][z] = loadBlock(tok, blockTypes);
                        }
                    }
                }
            }

            if (!playerSpawns.isEmpty()) {

                // TODO : REWORK

                BufferedImage skin = ImageIO.read(
                        WorldLoader.class.getResource(
                                "/resources/textures/outlined/dirt-block.png"));
                Shape cube = new Cube();
                Texture playerTex = new Texture(cube, skin);
                EntityType playerType = new EntityType("player");
                playerType.addTexture(playerTex);

                // END REWORK

                int n = (int) (Math.random() * playerSpawns.size());
                double px = playerSpawns.get(n)[0], py =  playerSpawns.get(n)[1], pz =  playerSpawns.get(n)[2];
                entities.add(new Player(playerType, px, py, pz));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading world " + file, e);
        }

        return new WorldData(blocks, entities);
    }

    /* ------------------------------------------------------------------ */
    /* internals */
    /* ------------------------------------------------------------------ */

    private static Block loadBlock(String exp,
            ArrayList<BlockType> blockTypes) {

        String[] parts = exp.split("/");
        Block block = blockTypes.get(Integer.parseInt(parts[0])).getInstance();

        for (int i = 1; i < parts.length; i++) {
            String[] nameVal = parts[i].split("=");
            setState(block, nameVal[0], nameVal[1]);
        }
        return block;
    }

    private static void setState(Block block, String name, String value) {
        Object cur = block.getState(name);
        if (cur instanceof Double) {
            block.setState(name, Double.parseDouble(value));
        } else if (cur instanceof Integer) {
            block.setState(name, Integer.parseInt(value));
        } else if (cur instanceof Boolean) {
            block.setState(name, Boolean.parseBoolean(value));
        } else {
            block.setState(name, value);
        }
    }
}
