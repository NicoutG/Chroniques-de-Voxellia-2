package graphics;

import java.awt.image.BufferedImage;

public class Drawable {
    final BufferedImage texture;
    final double x, y, z;
    boolean requiresCorrection;

    public Drawable(BufferedImage texture, double x, double y, double z) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.z = z;
        this.requiresCorrection = false;
    }

    public Drawable(BufferedImage texture, double x, double y, double z, boolean requiresCorrection) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.z = z;
        this.requiresCorrection = requiresCorrection;
    }

    // If correction is required it means we have to compensate the +0.5 given to
    // entities. thus we have to remove 0.5 for x and y and 0.25 for z (just to
    // compensate the factor 1.5) and thus we remove 1.25
    double getSortKey() {
        return requiresCorrection ? (x + y + z * 1.5) - 1.25 : x + y + z * 1.5;
    }
}
