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

    public Shape(BufferedImage leftMask,
                    BufferedImage rightMask,
                    BufferedImage topMask,
                    boolean fullSpace) {

        this.leftMask  = Objects.requireNonNull(leftMask,  "leftMask");
        this.rightMask = Objects.requireNonNull(rightMask, "rightMask");
        this.topMask   = Objects.requireNonNull(topMask,   "topMask");
        this.fullSpace = fullSpace;
    }

    public Shape(String leftMask, String rightMask, String topMask, boolean fullSpace) {
        this(loadMask(leftMask), loadMask(rightMask), loadMask(topMask), fullSpace);
    }

    public Shape(String commonPath, String leftMask, String rightMask, String topMask, boolean fullSpace) {
        this(loadMask(commonPath+leftMask), loadMask(commonPath+rightMask), loadMask(commonPath+topMask), fullSpace);
    }



    /* ---------- Accessors ---------- */

    public final BufferedImage getLeftMask()  { return leftMask;  }
    public final BufferedImage getRightMask() { return rightMask; }
    public final BufferedImage getTopMask()   { return topMask;   }
    public final boolean takesFullSpace() {return fullSpace;}

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
