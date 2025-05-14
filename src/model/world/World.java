package model.world;

import javax.imageio.ImageIO;

import graphics.Texture;
import graphics.shape.Cube;
import graphics.shape.Shape;
import objects.block.Block;
import objects.block.BlockType;
import objects.entity.Entity;
import objects.entity.EntityType;
import objects.entity.Player;
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

    public Block getBlock(int x, int y, int z) {
        if (0 <= x && x < blocks.length && 0 <= y && y < blocks[x].length && 0 <= z && z < blocks[x][y].length)
            return blocks[x][y][z];
        return null;
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
        int sx = 100, sy = 100, sz = 2;
        Block[][][] blocks = new Block[sx][sy][sz + 10];

        try {
            BufferedImage player0 = ImageIO.read(World.class.getResource("/resources/textures/outlined/purple-block.png"));
            // BufferedImage player1 = ImageIO.read(World.class.getResource("/resources/textures/outlined/blue-block.png"));

            Shape cube = new Cube();
            Texture player = new Texture(cube,  player0); // animé : 2 frames, 6 ticks

            for (int z = 0; z < sz; z++) {
                for (int y = 0; y < sy; y++) {
                    for (int x = 0; x < sx; x++) {
                        if(x >= 14 && x <= 16 && z== 1 && y == 9 ) {

                            blocks[x][y][z] = blockTypes.get(5).getInstance();
                        } else {

                            blocks[x][y][z] = blockTypes.get(z%2).getInstance();
                        }
                    }
                }
            }

            blocks[10][11][2] = blockTypes.get(0).getInstance();
            blocks[10][12][2] = blockTypes.get(0).getInstance();
            blocks[10][13][2] = blockTypes.get(0).getInstance();
            blocks[10][3][2] = blockTypes.get(0).getInstance();
            blocks[10][4][2] = blockTypes.get(0).getInstance();
            blocks[10][5][2] = blockTypes.get(0).getInstance();
            blocks[10][14][2] = blockTypes.get(4).getInstance();
            
            blocks[14][7][2] = blockTypes.get(4).getInstance();
            blocks[12][7][2] = blockTypes.get(4).getInstance();
            blocks[13][7][2] = blockTypes.get(4).getInstance();

            // blocks[15][12][2] = blockTypes.get(3).getInstance();

            blocks[13][19][2] = blockTypes.get(3).getInstance();
            
            blocks[18][11][2] = blockTypes.get(0).getInstance();
            blocks[18][12][2] = blockTypes.get(0).getInstance();
            blocks[18][13][2] = blockTypes.get(0).getInstance();

            ArrayList<Entity> entities = new ArrayList<>();

            // player
            EntityType playerType = new EntityType("player");
            playerType.addTexture(player);
            entities.add(new Player(playerType, 14.5, 8.5, 2.5));// chacune

            return new World(blocks, entities);


        } catch (IOException e) {
            throw new RuntimeException("Failed to load block textures", e);
        }
    }

    public void update() {
        // update blocks
        Vector positionBlock = new Vector();
        for (int z = 0; z < blocks[0][0].length; z++) {
            positionBlock.z = z + 0.5;
            for (int y = 0; y < blocks[0].length; y++) {
                positionBlock.y = y + 0.5;
                for (int x = 0; x < blocks.length; x++) {
                    Block block = blocks[x][y][z];
                    if (block != null) {
                        positionBlock.x = x + 0.5;
                        block.onUpdate(this,positionBlock);
                    }
                }
            }
        }

        // update entities
        for (Entity entity : entities)
            entity.onUpdate(this);
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
