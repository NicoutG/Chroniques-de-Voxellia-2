package graphics.shape;

import java.awt.image.BufferedImage;
import java.io.IOException;

import tools.PathManager;

public final class Lever extends Shape {

    private static final BufferedImage LEFT_MASK_F;
    private static final BufferedImage RIGHT_MASK_F;
    private static final BufferedImage TOP_MASK_F;
    private static final BufferedImage LEFT_MASK_T;
    private static final BufferedImage RIGHT_MASK_T;
    private static final BufferedImage TOP_MASK_T;

    static {
        try {
            String maskPath = PathManager.MASK_PATH + "lever/false/";
            LEFT_MASK_F  = loadMask(maskPath + "mask-left.png");
            RIGHT_MASK_F = loadMask(maskPath + "mask-right.png");
            TOP_MASK_F   = loadMask(maskPath + "mask-top.png");
            maskPath = PathManager.MASK_PATH + "lever/true/";
            LEFT_MASK_T  = loadMask(maskPath + "mask-left.png");
            RIGHT_MASK_T = loadMask(maskPath + "mask-right.png");
            TOP_MASK_T   = loadMask(maskPath + "mask-top.png");
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public Lever(boolean activation) {
        this(
            activation ? LEFT_MASK_T : LEFT_MASK_F,
            activation ? RIGHT_MASK_T : RIGHT_MASK_F,
            activation ? TOP_MASK_T : TOP_MASK_F
        );
    }

    private Lever(BufferedImage left, BufferedImage right, BufferedImage top) {
        super(left, right, top, true);
    }
}
