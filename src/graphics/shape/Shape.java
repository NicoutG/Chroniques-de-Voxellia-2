package graphics.shape;

import javax.imageio.ImageIO;

import tools.PathManager;

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
