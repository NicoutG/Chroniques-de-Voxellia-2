package graphics.shape;

import java.awt.image.BufferedImage;
import java.io.IOException;

public final class Rectangle extends Shape {

    private static final BufferedImage LEFT_MASK;
    private static final BufferedImage RIGHT_MASK;
    private static final BufferedImage TOP_MASK;

    static {
        try {
            LEFT_MASK  = loadMask("/resources/masks/rectangle/mask-left.png");
            RIGHT_MASK = loadMask("/resources/masks/rectangle/mask-right.png");
            TOP_MASK   = loadMask("/resources/masks/rectangle/mask-top.png");
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public Rectangle() {
        super(LEFT_MASK, RIGHT_MASK, TOP_MASK, true);
    }
}
