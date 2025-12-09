package world;

import objects.*;
import objects.block.*;
import objects.entity.*;
import tools.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.*;

/** Loads worlds from human-readable text files. */
public class WorldLoader {

    /* Small record that bundles both arrays for the World ctor */
    public record WorldData(Block[][][] blocks, ArrayList<Entity> entities, ArrayList<SpawnPoint> spawnPoints) {}

    public record SpawnPoint(int playerId, double x, double y, double z, Map<String, Object> parameters) {}


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

    public static WorldData loadWorld(String file, ArrayList<BlockType> blockTypes, ArrayList<EntityType> entityTypes) {

        Block[][][] blocks = null;
        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<SpawnPoint> spawnPoints = new ArrayList<>();

        try {
            String json = Files.readString(Paths.get(PathManager.WORLD_PATH + file));
            JSONObject root = new JSONObject(json);

            int dimX = root.getInt("dX");
            int dimY = root.getInt("dY");
            int dimZ = root.getInt("dZ");

            blocks = new Block[dimX][dimY][dimZ];

            JSONArray layers = root.getJSONArray("blocks");

            for (int z = 0; z < dimZ; z++) {
                JSONArray layer = layers.getJSONArray(z);

                for (int y = 0; y < dimY; y++) {
                    JSONArray row = layer.getJSONArray(y);

                    for (int x = 0; x < dimX; x++) {

                        Object cell = row.isNull(x) ? null : row.get(x);

                        if (cell == null) {
                            blocks[x][y][z] = null;
                            continue;
                        }

                        JSONObject obj = (JSONObject) cell;

                        String type = obj.getString("type");
                        int id = obj.getInt("id");
                        JSONArray params = obj.getJSONArray("parameters");

                        if (type.equals("block")) {
                            Block block = blockTypes.get(id).getInstance();
                            applyParameters(block, params);
                            blocks[x][y][z] = block;
                        }
                        else if (type.equals("entity")) {
                            Entity entity = entityTypes.get(id).getInstance(x + 0.5, y + 0.5, z + 0.5);
                            applyParameters(entity, params);
                            entities.add(entity);
                        }
                        else if (type.equals("player")) {
                            Map<String, Object> parameters = parseParameters(params);
                            spawnPoints.add(new SpawnPoint(id, x+0.5, y+0.5, z+0.5, parameters));
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading world '" + file + "'", e);
        }

        return new WorldData(blocks, entities, spawnPoints);
    }

    /* ------------------------------------------------------------------ */
    /*  Apply parameters from JSON                                         */
    /* ------------------------------------------------------------------ */

    private static void applyParameters(ObjectInstance instance, JSONArray params) {

        for (int i = 0; i < params.length(); i++) {
            JSONObject obj = params.getJSONObject(i);

            for (String key : obj.keySet()) {
                Object value = obj.get(key);

                if (value instanceof JSONArray)
                    instance.setState(key, jsonArrayToJavaArray((JSONArray) value));
                else
                    instance.setState(key, value);
            }
        }
    }

    private static Object jsonArrayToJavaArray(JSONArray arr) {
        if (arr.length() == 0) 
            return new Object[0];

        Object first = arr.get(0);

        if (first instanceof Integer) {
            int[] out = new int[arr.length()];
            for (int i = 0; i < arr.length(); i++) 
                out[i] = arr.getInt(i);
            return out;
        }
        if (first instanceof Double) {
            double[] out = new double[arr.length()];
            for (int i = 0; i < arr.length(); i++) 
                out[i] = arr.getDouble(i);
            return out;
        }
        if (first instanceof Boolean) {
            boolean[] out = new boolean[arr.length()];
            for (int i = 0; i < arr.length(); i++) 
                out[i] = arr.getBoolean(i);
            return out;
        }

        String[] out = new String[arr.length()];
        for (int i = 0; i < arr.length(); i++) 
            out[i] = arr.get(i).toString();

        return out;
    }

    private static Map<String, Object> parseParameters(JSONArray jsonParams) {
        Map<String, Object> params = new HashMap<>();

        for (int i = 0; i < jsonParams.length(); i++) {
            JSONObject obj = jsonParams.getJSONObject(i);

            for (String key : obj.keySet()) {
                Object value = obj.get(key);

                if (value instanceof JSONArray)
                    params.put(key, jsonArrayToJavaArray((JSONArray) value));
                else
                    params.put(key, value);
            }
        }
        return params;
    }


}
