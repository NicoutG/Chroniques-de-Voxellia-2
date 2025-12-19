/*  graphics/IsoMath.java  */
package graphics;

import java.awt.geom.Point2D;

/** Lightweight utilities shared by the renderer(s). */
public final class IsoMath {

    private static double ZOOM = 1 ;
    public static int DRAW_TILE_SIZE;

    static {
        setTileSize(1);
    }

    /** Converts world grid coordinates to screen pixel coordinates. */
    public static void toScreen(double x, double y, double z, Point2D.Double out) {
        out.x = (x - y); 
        out.y = ((x + y) / 2.0 - z); 
    }

    private IsoMath() { }           // static only

    public static void setTileSize(double tileSize) {
        ZOOM      = Math.max(0.3, Math.min(tileSize, 2));
        DRAW_TILE_SIZE = (int)(116.0/ZOOM);
    }

    public static double getTileSize() {
        return ZOOM;
    }
}
