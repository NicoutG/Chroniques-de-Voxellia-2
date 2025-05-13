package game;

import java.util.ArrayList;

import block.*;
import entity.*;
import tools.Vector;

public class Game {
    private Block[][][] terrain;
    private ArrayList<Entity> entities;

    public Block getBlock(int x, int y, int z) {
        if (0 <= x && x < terrain.length && 0 <= y && y < terrain[x].length && z <= 0 && z < terrain[x][y].length)
            return terrain[x][y][z];
        return null;
    }

    public void activateBlocks(int network) {
        for (int z = 0; z < terrain[0][0].length; z++)
            for (int y = 0; y < terrain[0].length; y++)
                for (int x = 0; x < terrain.length; x++) {
                    Block block = getBlock(x, y, z);
                    if (block != null)
                        block.onActivated(this, new Vector(x+0.5,y+0.5,z+0.5), network);
                }
    }

    public void desactivateBlocks(int network) {
        for (int z = 0; z < terrain[0][0].length; z++)
            for (int y = 0; y < terrain[0].length; y++)
                for (int x = 0; x < terrain.length; x++) {
                    Block block = getBlock(x, y, z);
                    if (block != null)
                        block.onDesactivated(this, new Vector(x+0.5,y+0.5,z+0.5), network);
                }
    }
}
