package graphics.shape;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Shape {

    private final BufferedImage leftMask;
    private final BufferedImage rightMask;
    private final BufferedImage topMask;

    protected Shape(BufferedImage leftMask,
                    BufferedImage rightMask,
                    BufferedImage topMask) {

        this.leftMask  = Objects.requireNonNull(leftMask,  "leftMask");
        this.rightMask = Objects.requireNonNull(rightMask, "rightMask");
        this.topMask   = Objects.requireNonNull(topMask,   "topMask");
    }

    /* ---------- Accessors ---------- */

    public final BufferedImage getLeftMask()  { return leftMask;  }
    public final BufferedImage getRightMask() { return rightMask; }
    public final BufferedImage getTopMask()   { return topMask;   }

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
