package model.world;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import block.*;

public class WorldLoader {
    private static final String WORLD_PATH = "src/resources/worlds/"; 

    public static ArrayList<BlockType> loadBlockTypes() {
        return BlockTypeFactory.loadBlockTypes();
    }

    public static Block[][][] loadBlocks(String filePath, ArrayList<BlockType> blockTypes) {
        Block[][][] blocks = null;
        final String errorMessage = "Error loading "+filePath;
        try {
            String exp = new String(Files.readAllBytes(Paths.get(WORLD_PATH + filePath)));
            String [] lines=exp.split("\r\n");
            if (lines == null || lines.length<=1) {
                System.out.println(errorMessage);
                System.exit(0);
            }
            String [] dims=lines[0].split(" ");
            if (dims==null || dims.length!=3) {
                System.out.println(errorMessage);
                System.exit(0);
            }
            int dimX=Integer.parseInt(dims[0]);
            int dimY=Integer.parseInt(dims[1]);
            int dimZ=Integer.parseInt(dims[2]);
            blocks = new Block[dimX][dimY][dimZ];
            int nbLines=1+dimZ*(dimY+1);
            if (lines.length!=nbLines) {
                System.out.println(errorMessage);
                System.exit(0);
            }
            int line=1;
            for (int i=0;i<dimZ;i++) {
                line++;
                for (int j=0;j<dimY;j++) {
                    String [] blocksY=lines[line++].split(" ");
                    if (blocksY.length!=dimX) {
                        System.out.println(errorMessage);
                        System.exit(0);
                    }
                    for (int k=0;k<dimX;k++)
                        blocks[k][j][i]= loadBlock(blocksY[k], blockTypes);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return blocks;
    }

    private static Block loadBlock(String exp, ArrayList<BlockType> blockTypes) {
        if (exp.equals("."))
            return null;
        String[] blockAndStates = exp.split("/");
        Block block = blockTypes.get(Integer.parseInt(blockAndStates[0])).createBlock();
        for (int i = 1; i < blockAndStates.length; i++) {
            String[] nameAndValue = blockAndStates[i].split("=");
            String name = nameAndValue[0];
            String value = nameAndValue[1];
            setState(block, name, value);
        }
        return block;
    }

    private static void setState(Block block, String name, String value) {
        Object currentValue = block.getState(name);
        switch (currentValue) {
            case Double _ -> block.setState(name, Double.parseDouble(value));
            case Integer _ -> block.setState(name, Integer.parseInt(value));
            case Boolean _ -> block.setState(name, Boolean.parseBoolean(value));
            default -> block.setState(name, value);
        }
    }
}
