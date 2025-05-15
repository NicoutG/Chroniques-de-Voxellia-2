package graphics;

import java.awt.image.BufferedImage;

public class Drawable {
    final BufferedImage texture;
    final double x, y, z;

    public Drawable(BufferedImage texture, double x, double y, double z) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double getSortKey() {
        return Math.floor(x) + Math.floor(y) + Math.floor(z) * 2;
    }
}
