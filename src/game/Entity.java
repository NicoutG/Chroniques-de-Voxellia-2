package game;

import java.awt.image.BufferedImage;

public class Entity {
    protected final BufferedImage texture;
    protected double x, y, z;

    public Entity(BufferedImage texture, double x, double y, double z) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BufferedImage getTexture() { return texture; }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    public void setPosition(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
    }
}
