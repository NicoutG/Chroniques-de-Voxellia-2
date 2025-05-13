package model.world;

import javax.imageio.ImageIO;

import block.Block;
import block.BlockType;
import entity.Entity;
import entity.Player;
import graphics.Texture;
import graphics.shape.Cube;
import graphics.shape.Shape;
import tools.Vector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class World {
    private static ArrayList<BlockType> blockTypes = WorldLoader.loadBlockTypes();
    private Block[][][] blocks;
    private ArrayList<Entity> entities;

    public World(Block[][][] blocks, ArrayList<Entity> entities) {
        this.blocks = blocks;
        this.entities = entities;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        for (Entity e : entities) {
            if (e instanceof Player)
                return (Player) e;
        }
        return null;
    }

    public static World createDemoWorld() {
        int sx = 20, sy = 20, sz = 2;
        Block[][][] blocks = new Block[sx][sy][sz + 2];

        try {
            BufferedImage player0 = ImageIO.read(World.class.getResource("/resources/textures/purple-block.png"));
            BufferedImage player1 = ImageIO.read(World.class.getResource("/resources/textures/blue-block.png"));

            Shape cube = new Cube();
            Texture player = new Texture(cube, new BufferedImage[] { player0, player1 }, 6); // animé : 2 frames, 6 ticks

            for (int z = 0; z < sz; z++) {
                for (int y = 0; y < sy; y++) {
                    for (int x = 0; x < sx; x++) {
                        blocks[x][y][z] = blockTypes.get(z%2).createBlock();
                    }
                }
            }

            blocks[10][11][2] = blockTypes.get(0).createBlock();
            blocks[10][12][2] = blockTypes.get(0).createBlock();
            blocks[10][13][2] = blockTypes.get(0).createBlock();

            ArrayList<Entity> entities = new ArrayList<>();

            // player
            entities.add(new Player(player, 12.5, 12.5, 2));// chacune

            return new World(blocks, entities);


        } catch (IOException e) {
            throw new RuntimeException("Failed to load block textures", e);
        }
    }

    public void activateBlocks(int network) {
        for (int z = 0; z < blocks[0][0].length; z++)
            for (int y = 0; y < blocks[0].length; y++)
                for (int x = 0; x < blocks.length; x++) {
                    Block block = blocks[x][y][z];
                    if (block != null)
                        block.onActivated(this, new Vector(x+0.5,y+0.5,z+0.5), network);
                }
    }

    public void desactivateBlocks(int network) {
        for (int z = 0; z < blocks[0][0].length; z++)
            for (int y = 0; y < blocks[0].length; y++)
                for (int x = 0; x < blocks.length; x++) {
                    Block block = blocks[x][y][z];
                    if (block != null)
                        block.onDesactivated(this, new Vector(x+0.5,y+0.5,z+0.5), network);
                }
    }
}
