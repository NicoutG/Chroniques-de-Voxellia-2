package graphics.GPURenderer;

import java.awt.image.BufferedImage;

class Sprite {
    float x, y;
    float width, height;
    float brightness;
    BufferedImage image;
    BufferedImage maskLeft;
    BufferedImage maskRight;
    BufferedImage maskTop;

    float[] lightLeft  = new float[3];
    float[] lightRight = new float[3];
    float[] lightTop   = new float[3];
    int[] color;

    double x3D, y3D, z3D;
    boolean alwaysBehind = false;
    boolean requiresCorrection = false;

    public Sprite(double x, double y, BufferedImage img) {
        this.x = (float)x;
        this.y = (float)y;
        this.image = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.brightness = 1;
        this.color = null;
    }

    public void setBrightness(double brightness) {
        this.brightness = (float)brightness;
    }

    public void setSize(double size) {
        this.width = (float)size;
        this.height = (float)size;
    }

    public void set3D(double x, double y, double z) {
        x3D = x;
        y3D = y;
        z3D = z;
    }

    double getSortKey() {
        double newX = x3D;
        double newY = y3D;
        double newZ = z3D;
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
