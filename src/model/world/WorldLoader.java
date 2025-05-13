package model.world;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import block.*;

public class WorldLoader {
    public static ArrayList<BlockType> loadBlockTypes() {
        return BlockTypeFactory.loadBlockTypes();
    }

    public static Block[][][] loadBlocks(String filePath) {
        Block[][][] blocks;
        try {
            String exp = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch(Exception e) {
            e.printStackTrace();
        }
        blocks = new Block[1][1][1];
        return blocks;
    }
}
