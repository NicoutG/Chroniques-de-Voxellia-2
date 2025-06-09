package world;

import objects.*;
import objects.block.*;
import objects.entity.*;
import tools.*;

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

        Player player = entityTypes.get(0).getInstancePlayer();
        entities.add(player);

        try {
            String[] lines = Files.readString(Paths.get(PathManager.WORLD_PATH + file))
                                   .split("\r?\n");

            /* ---- dimensions header ---- */
            String[] dims = split(lines[0].trim(),' ');
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

                    String[] toks = split(lines[line++].trim(),' ');
                    if (toks.length != dimX)
                        throw new IllegalStateException("Malformed row in " + file);

                    for (int x = 0; x < dimX; x++) {
                        String tok = toks[x];

                        if (tok.equals("."))                     // air
                            blocks[x][y][z] = null;
                        else if (tok.length() > 0 && (tok.charAt(0) == 'p' || tok.charAt(0) == 'P')) {   // spawn point
                            /* spawn point → remember coords, store AIR */
                            blocks[x][y][z] = null;
                            loadSpawnPoint(tok.substring(1), x, y, z, spawnPoints, player);

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


    private static void loadSpawnPoint(String token, double x, double y, double z, ArrayList<Vector> spawnPoints, Player player) {
        spawnPoints.add(new Vector(x+0.5,y+0.5,z+0.5));
        String[] parts = split(token,'/');
        setStates(player, parts);
    }

    private static Block loadBlock(String token, ArrayList<BlockType> blockTypes) {
        String[] parts = split(token,'/');
        Block block = blockTypes.get(Integer.parseInt(parts[0])).getInstance();
        setStates(block, parts);
        return block;
    }

    private static Entity loadEntity(String token, double x, double y, double z, ArrayList<EntityType> entityTypes) {

        String[] parts = split(token,'/');
        Entity entity = entityTypes.get(Integer.parseInt(parts[0])).getInstance(x,y,z);
        setStates(entity, parts);
        return entity;
    }

    private static void setStates(ObjectInstance instance, String ... states) {
        if (states != null) {
            for (int i = 1; i < states.length; i++) {
                String[] nv = split(states[i],'=');
                setState(instance, nv[0], nv[1]);
            }
        }
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
        else instance.setState(name, convertToString(value));
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
        input = input.replaceAll("[{}\"]", "");
        if (input.isEmpty()) return new String[0];
        String[] results = split(input,',');
        for (int i = 0; i < results.length; i++)
            results[i] = convertToString(results[i]);
        return results;
    }

    private static String convertToString(String text) {
        if (text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"')
            return text.substring(1, text.length() - 1);
        return text;
    }

    private static String[] split(String text, char separator) {
        ArrayList<String> texts = new ArrayList<>();
        boolean isInString = false;
        int begin = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == separator && !isInString) {
                texts.add(text.substring(begin, i));
                begin = i + 1;
            }
            if(text.charAt(i) == '"')
                isInString = !isInString;
        }
        if(begin < text.length())
            texts.add(text.substring(begin, text.length()));
        return texts.toArray(new String[0]);
    }

}
