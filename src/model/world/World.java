package model.world;

import javax.imageio.ImageIO;

import engine.Block;
import entity.Entity;
import entity.Player;
import graphics.Texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    private final Block[][][] blocks;
    private final Entity[] entities;

    public World(Block[][][] blocks, Entity[] entities) {
        this.blocks = blocks;
        this.entities = entities;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

    public Entity[] getEntities() {
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
            BufferedImage blueImg = ImageIO.read(World.class.getResource("/resource/texture/blue-block.png"));
            BufferedImage redImg = ImageIO.read(World.class.getResource("/resource/texture/red-block.png"));
            BufferedImage player0 = ImageIO.read(World.class.getResource("/resource/texture/purple-block.png"));
            BufferedImage player1 = ImageIO.read(World.class.getResource("/resource/texture/blue-block.png"));

            Texture blue = new Texture(blueImg); // bloc statique
            Texture red = new Texture(redImg);
            Texture player = new Texture(new BufferedImage[] { player0, player1 }, 6); // animé : 2 frames, 6 ticks
                   
             for (int z = 0; z < sz; z++) {
            for (int y = 0; y < sy; y++) {
                for (int x = 0; x < sx; x++) {
                    blocks[x][y][z] = new Block((z % 2 == 0) ? blue : red);
                }
            }
        }

        blocks[10][11][2] = new Block(blue);
        blocks[10][12][2] = new Block(blue);
        blocks[10][13][2] = new Block(blue);

        Entity[] entities = new Entity[3];

        // player
        entities[0] = new Player(player, 12.5, 12.5, 2);// chacune

        return new World(blocks, entities);


        } catch (IOException e) {
            throw new RuntimeException("Failed to load block textures", e);
        }
    }
}
