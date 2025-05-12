package game;

import javax.imageio.ImageIO;
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
        Block[][][] blocks = new Block[sx][sy][sz + 1];

        BufferedImage blue, red, playerImg;
        try {
            playerImg = ImageIO.read(World.class.getResource("/resource/texture/purple-block.png"));
            blue = ImageIO.read(World.class.getResource("/resource/texture/blue-block.png"));
            red = ImageIO.read(World.class.getResource("/resource/texture/red-block.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load block textures", e);
        }

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
        entities[0] = new Player(playerImg, 10.5, 10.5, 2);

        return new World(blocks, entities);
    }
}
