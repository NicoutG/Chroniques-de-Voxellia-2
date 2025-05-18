package graphics.shape;

import java.awt.image.BufferedImage;
import java.io.IOException;

import tools.PathManager;

public final class StairsLeft extends Shape {

    private static final BufferedImage LEFT_MASK;
    private static final BufferedImage RIGHT_MASK;
    private static final BufferedImage TOP_MASK;

    static {
        try {
            String maskPath = PathManager.MASK_PATH + "stairs/left/";
            LEFT_MASK  = loadMask(maskPath + "mask-left.png");
            RIGHT_MASK = loadMask(maskPath + "mask-right.png");
            TOP_MASK   = loadMask(maskPath + "mask-top.png");
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public StairsLeft() {
        super(LEFT_MASK, RIGHT_MASK, TOP_MASK, true);
    }
}
