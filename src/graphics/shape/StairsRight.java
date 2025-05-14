package graphics.shape;

import java.awt.image.BufferedImage;
import java.io.IOException;

public final class StairsRight extends Shape {

    private static final BufferedImage LEFT_MASK;
    private static final BufferedImage RIGHT_MASK;
    private static final BufferedImage TOP_MASK;

    static {
        try {
            LEFT_MASK  = loadMask("/resources/masks/stairs/right/mask-left.png");
            RIGHT_MASK = loadMask("/resources/masks/stairs/right/mask-right.png");
            TOP_MASK   = loadMask("/resources/masks/stairs/right/mask-top.png");
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public StairsRight() {
        super(LEFT_MASK, RIGHT_MASK, TOP_MASK, true);
    }
}
