package graphics.shape;

import javax.imageio.ImageIO;

import tools.PathManager;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Shape {

    private final BufferedImage leftMask;
    private final BufferedImage rightMask;
    private final BufferedImage topMask;
    private final boolean fullSpace;
    private final double opacity;
    private boolean[] allowLight = new boolean[] { false, false, false };

    public Shape(BufferedImage leftMask,
                    BufferedImage rightMask,
                    BufferedImage topMask,
                    boolean fullSpace, double opacity, boolean leftLight, boolean rightLight, boolean topLight) {

        this.leftMask  = Objects.requireNonNull(leftMask,  "leftMask");
        this.rightMask = Objects.requireNonNull(rightMask, "rightMask");
        this.topMask   = Objects.requireNonNull(topMask,   "topMask");
        this.fullSpace = fullSpace;
        this.opacity = opacity;
        this.allowLight[Face.LEFT.index] = leftLight;
        this.allowLight[Face.RIGHT.index] = rightLight;
        this.allowLight[Face.TOP.index] = topLight;
    }

    public Shape(String leftMask, String rightMask, String topMask, boolean fullSpace, double opacity, boolean leftLight, boolean rightLight, boolean topLight) {
        this(loadMask(leftMask), loadMask(rightMask), loadMask(topMask), fullSpace, opacity, leftLight, rightLight, topLight);
    }

    public Shape(String commonPath, String leftMask, String rightMask, String topMask, boolean fullSpace, double opacity, boolean leftLight, boolean rightLight, boolean topLight) {
        this(loadMask(commonPath+leftMask), loadMask(commonPath+rightMask), loadMask(commonPath+topMask), fullSpace, opacity, leftLight, rightLight, topLight);
    }



    /* ---------- Accessors ---------- */

    public final BufferedImage getLeftMask()  { return leftMask;  }
    public final BufferedImage getRightMask() { return rightMask; }
    public final BufferedImage getTopMask()   { return topMask;   }
    public final boolean takesFullSpace() {return fullSpace;}
    public final double getOpacity() {return opacity;}

    public boolean isLightAllowed(int index) {
        return allowLight[index];
    }

    public BufferedImage getGlobalMask() {
        int width = topMask.getWidth();
        int height = topMask.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = 0, g = 0, b = 0, a = 0;

                // Masque haut -> bleu
                int top = new Color(topMask.getRGB(x, y), true).getRed(); // mask en niveaux de gris
                if (top > 0) {
                    b = Math.min(255, b + top);
                    a = 255;
                }

                // Masque gauche -> rouge
                int left = new Color(leftMask.getRGB(x, y), true).getRed();
                if (left > 0) {
                    r = Math.min(255, r + left);
                    a = 255;
                }

                // Masque droite -> vert
                int right = new Color(rightMask.getRGB(x, y), true).getRed();
                if (right > 0) {
                    g = Math.min(255, g + right);
                    a = 255;
                }

                Color c = new Color(r, g, b, a);
                result.setRGB(x, y, c.getRGB());
            }
        }

        return result;
    }


    /* ---------- Util for subclasses ---------- */

    protected static BufferedImage loadMask(String resource) {
        try {
            var stream = Shape.class.getResourceAsStream(PathManager.MASK_PATH+resource);
            if (stream == null) {
                throw new IOException("Resource not found: " + resource);
            }
            return ImageIO.read(stream);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }
}
