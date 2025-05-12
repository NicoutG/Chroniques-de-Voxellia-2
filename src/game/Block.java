package game;

import java.awt.image.BufferedImage;

public class Block {
    public final BufferedImage texture;

    public Block(BufferedImage texture) {
        this.texture = texture;
    }

    public BufferedImage getTexture() {
        return texture;
    }
}
