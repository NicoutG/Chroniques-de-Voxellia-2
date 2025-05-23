package world;

import objects.*;
import objects.block.*;
import objects.entity.*;
import tools.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Loads worlds from human-readable text files. */
public class WorldLoader {

    /* Small record that bundles both arrays for the World ctor */
    public record WorldData(Block[][][] blocks, ArrayList<Entity> entities, ArrayList<Vector> spawnPoints) {}

    /* ------------------------------------------------------------------ */
    /*  Public helpers                                                     */
    /* ------------------------------------------------------------------ */

    public static ArrayList<BlockType> loadBlockTypes() {
        ArrayList<BlockType> blockTypes = new ArrayList<>();
        for (BlockTemplate template : BlockTemplate.values())
            blockTypes.add(template.blockType);
        return blockTypes;
    }

    public static ArrayList<EntityType> loadEntityTypes() {
        ArrayList<EntityType> entityTypes = new ArrayList<>();
        for (EntityTemplate template : EntityTemplate.values())
            entityTypes.add(template.entityType);
        return entityTypes;
    }

    /** Parses <code>WORLD_PATH + file</code> and returns blocks + entities. */
    public static WorldData loadWorld(String file, ArrayList<BlockType> blockTypes, ArrayList<EntityType> entityTypes) {

        Block[][][]       blocks        = null;
        ArrayList<Entity> entities      = new ArrayList<>();
        ArrayList<Vector>  spawnPoints  = new ArrayList<>();

        entities.add(new Player(entityTypes.get(0),0,0,0));

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

                        if (tok.equals("."))                     // air
                            blocks[x][y][z] = null;
                        else if (tok.length() > 0 && (tok.charAt(0) == 'p' || tok.charAt(0) == 'P')) {   // spawn point
                            /* spawn point → remember coords, store AIR */
                            blocks[x][y][z] = null;
                            spawnPoints.add(new Vector(x+0.5,y+0.5,z+0.5));

                        } else if (tok.length() > 0 && (tok.charAt(0) == 'e' || tok.charAt(0) == 'E'))   // block
                            entities.add(loadEntity(tok.substring(1), x+0.5,y+0.5,z+0.5, entityTypes));
                        else
                            blocks[x][y][z] = loadBlock(tok, blockTypes);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading world '" + file + "'", e);
        }

        return new WorldData(blocks, entities, spawnPoints);
    }

    /* ------------------------------------------------------------------ */
    /*  Internal helpers                                                   */
    /* ------------------------------------------------------------------ */



    private static Block loadBlock(String token, ArrayList<BlockType> blockTypes) {

        String[] parts = token.split("/");
        Block block = blockTypes.get(Integer.parseInt(parts[0])).getInstance();

        for (int i = 1; i < parts.length; i++) {
            String[] nv = parts[i].split("=");
            setState(block, nv[0], nv[1]);
        }
        return block;
    }

    private static Entity loadEntity(String token, double x, double y, double z, ArrayList<EntityType> entityTypes) {

        String[] parts = token.split("/");
        Entity entity = entityTypes.get(Integer.parseInt(parts[0])).getInstance();
        entity.setPosition(x, y, z);

        for (int i = 1; i < parts.length; i++) {
            String[] nv = parts[i].split("=");
            setState(entity, nv[0], nv[1]);
        }
        return entity;
    }

    private static void setState(ObjectInstance instance, String name, String value) {
        Object cur = instance.getState(name);
        if (cur instanceof Double) instance.setState(name, Double.parseDouble(value));
        else if (cur instanceof Integer) instance.setState(name, Integer.parseInt(value));
        else if (cur instanceof Boolean) instance.setState(name, Boolean.parseBoolean(value));
        else if (cur instanceof double[]) instance.setState(name, parseDoubleArray(value));
        else if (cur instanceof int[]) instance.setState(name, parseIntArray(value));
        else if (cur instanceof boolean[]) instance.setState(name, parseBooleanArray(value));
        else if (cur instanceof String[]) instance.setState(name, parseStringArray(value));
        else instance.setState(name, value);
    }

    private static int[] parseIntArray(String input) {
        input = input.replaceAll("[{}\\s\"]", "");
        if (input.isEmpty()) return new int[0];

        String[] parts = input.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    private static double[] parseDoubleArray(String input) {
        input = input.replaceAll("[{}\\s\"]", "");
        if (input.isEmpty()) return new double[0];

        String[] parts = input.split(",");
        double[] result = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Double.parseDouble(parts[i]);
        }
        return result;
    }

    private static boolean[] parseBooleanArray(String input) {
        input = input.replaceAll("[{}\\s\"]", "");
        if (input.isEmpty()) return new boolean[0];

        String[] parts = input.split(",");
        boolean[] result = new boolean[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Boolean.parseBoolean(parts[i]);
        }
        return result;
    }

    private static String[] parseStringArray(String input) {
        // Supprime les accolades et les espaces inutiles
        input = input.trim();
        if (input.startsWith("{")) input = input.substring(1);
        if (input.endsWith("}")) input = input.substring(0, input.length() - 1);

        // Sépare les éléments entre guillemets
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // toggle quote status
                if (!inQuotes) {
                    result.add(current.toString());
                    current.setLength(0); // reset buffer
                }
            } else if (inQuotes) {
                if (c != '_')
                    current.append(c);
                else
                    current.append(' ');
            }
        }

        return result.toArray(new String[0]);
    }

}
