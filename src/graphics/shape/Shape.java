package graphics.shape;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Shape {

    private final BufferedImage leftMask;
    private final BufferedImage rightMask;
    private final BufferedImage topMask;
    private final boolean fullSpace;

    protected Shape(BufferedImage leftMask,
                    BufferedImage rightMask,
                    BufferedImage topMask,
                    boolean fullSpace) {

        this.leftMask  = Objects.requireNonNull(leftMask,  "leftMask");
        this.rightMask = Objects.requireNonNull(rightMask, "rightMask");
        this.topMask   = Objects.requireNonNull(topMask,   "topMask");
        this.fullSpace = fullSpace;
    }

    /* ---------- Accessors ---------- */

    public final BufferedImage getLeftMask()  { return leftMask;  }
    public final BufferedImage getRightMask() { return rightMask; }
    public final BufferedImage getTopMask()   { return topMask;   }
    public final boolean takesFullSpace() {return fullSpace;}

    /* ---------- Util for subclasses ---------- */

    protected static BufferedImage loadMask(String resource)
            throws IOException {

        var stream = Shape.class.getResourceAsStream(resource);
        if (stream == null) {
            throw new IOException("Resource not found: " + resource);
        }
        return ImageIO.read(stream);
    }
}
