/*  graphics/IsoMath.java  */
package graphics;

import java.awt.geom.Point2D;

/** Lightweight utilities shared by the renderer(s). */
public final class IsoMath {

    public static final int TILE_SIZE      = 16;
    public static final int DRAW_TILE_SIZE = 64;
    private static final double HALF_TILE  = DRAW_TILE_SIZE / 2.0;
    private static final double QUART_TILE = DRAW_TILE_SIZE / 4.0;

    /** Converts world grid coordinates to screen pixel coordinates. */
    public static Point2D.Double toScreen(double x, double y, double z,
                                          Point2D.Double dst) {
        double isoX = (x - y) * HALF_TILE;
        double isoY = (x + y) * QUART_TILE - z * HALF_TILE;
        if (dst == null) dst = new Point2D.Double();
        dst.setLocation(isoX, isoY);
        return dst;
    }

    private IsoMath() { }           // static only
}
