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

    double getSortKey() {
        if (requiresCorrection) {
            double threshold = 0.1;
            
            double newX = x - 0.5;
            double decimalPart = newX - Math.floor(newX);
            if (decimalPart < threshold || decimalPart > (1 - threshold)) {
                newX = Math.round(newX);
            }

            double newY = y - 0.5;
            decimalPart = newY - Math.floor(newY);
            if (decimalPart < threshold || decimalPart > (1 - threshold)) {
                newY = Math.round(newY);
            }
            
            double newZ = z - 0.5;
            decimalPart = newY - Math.floor(newZ);
            if (decimalPart < threshold || decimalPart > (1 - threshold)) {
                newZ = Math.round(newZ);
            }

            return newX + newY + newZ * 2;
        }

        return x + y + z * 2;
    }
}
