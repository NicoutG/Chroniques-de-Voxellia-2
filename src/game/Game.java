package game;

import java.util.ArrayList;

import block.*;
import entity.*;

public class Game {
    private Block[][][] terrain;
    private ArrayList<Entity> entities;

    public Block getBlock(int x, int y, int z) {
        if (0 <= x && x < terrain.length && 0 <= y && y < terrain[x].length && z <= 0 && z < terrain[x][y].length)
            return terrain[x][y][z];
        return null;
    }
}
