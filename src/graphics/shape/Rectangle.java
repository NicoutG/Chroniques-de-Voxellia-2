package graphics.shape;

import java.awt.image.BufferedImage;
import java.io.IOException;

import tools.PathManager;

public final class Rectangle extends Shape {

    private static final BufferedImage[] LEFT_MASK;
    private static final BufferedImage[] RIGHT_MASK;
    private static final BufferedImage[] TOP_MASK;

    static {
        try {
            final int NB_RECTANGLES = 3;
            LEFT_MASK = new BufferedImage[NB_RECTANGLES];
            RIGHT_MASK = new BufferedImage[NB_RECTANGLES];
            TOP_MASK = new BufferedImage[NB_RECTANGLES];
            for (int i = 0; i < NB_RECTANGLES; i++) {
                String maskPath = PathManager.MASK_PATH + "rectangle/rectangle" + (i+1) + "/";
                LEFT_MASK[i] = loadMask(maskPath + "mask-left.png");
                RIGHT_MASK[i] = loadMask(maskPath + "mask-right.png");
                TOP_MASK[i] = loadMask(maskPath + "mask-top.png");
            }
            
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public Rectangle(int nb) {
        super(LEFT_MASK[nb - 1], RIGHT_MASK[nb - 1], TOP_MASK[nb - 1], false);
    }
}
