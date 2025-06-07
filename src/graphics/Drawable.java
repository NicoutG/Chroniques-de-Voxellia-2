package graphics;

import java.awt.image.BufferedImage;

public class Drawable {
    final BufferedImage texture;
    final double x, y, z;
    boolean requiresCorrection;
    boolean alwaysBehind;

    public Drawable(BufferedImage texture, double x, double y, double z) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.z = z;
        this.requiresCorrection = false;
        this.alwaysBehind = false;
    }

    public Drawable(BufferedImage texture, double x, double y, double z, boolean requiresCorrection) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.z = z;
        this.requiresCorrection = requiresCorrection;
    }

    public Drawable(BufferedImage texture, double x, double y, double z, boolean requiresCorrection,
            boolean alwaysBehind) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.z = z;
        this.requiresCorrection = requiresCorrection;
        this.alwaysBehind = alwaysBehind;
    }

    double getSortKey() {
        double newX = x;
        double newY = y;
        double newZ = z;
        if (requiresCorrection) {
            double threshold = 0.08;

            newX -= 0.5;
            double decimalPart = newX - Math.floor(newX);
            if (decimalPart < threshold || decimalPart > (1 - threshold)) {
                newX = Math.round(newX);
            }

            newY -= 0.5;
            decimalPart = newY - Math.floor(newY);
            if (decimalPart < threshold || decimalPart > (1 - threshold)) {
                newY = Math.round(newY);
            }

            newZ -= 0.5;
            decimalPart = newZ - Math.floor(newZ);
            if (decimalPart < threshold || decimalPart > (1 - threshold)) {
                newZ = Math.round(newZ);
            } else if (decimalPart > (0.5 - threshold) && decimalPart < (0.5 + threshold)) {
                newZ = Math.floor(newZ) + 0.5;
            }

        }

        if (this.alwaysBehind == true) {
            newX = newX - 0.45;
            newY = newY - 0.45;
        }
        return newX + newY + newZ * 2;
    }
}
