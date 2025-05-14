/*  graphics/Renderer.java  */
package graphics;

import graphics.ligth.ColorRGB;
import graphics.ligth.FaceLighting;
import graphics.ligth.LightingEngine;
import graphics.shape.Face;
import model.world.World;
import objects.block.Block;
import objects.entity.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Renders an isometric world while …
 * • skipping interior blocks (every neighbour present);
 * • culling anything whose quad never reaches the viewport;
 * • re-using the same Point2D & ArrayList between frames.
 */
public final class Renderer {

    private static final Comparator<Drawable> DEPTH = Comparator.comparingDouble(Drawable::getSortKey);

    private final World world;
    private final LightingEngine lighthinEngine;

    /* ---- reusable scratch objects to avoid GC thrash ---- */
    private final List<Drawable> drawables = new ArrayList<>(1024);
    private final Point2D.Double scratchPoint = new Point2D.Double();

    public Renderer(World world) {
        this.world = world;
        this.lighthinEngine = new LightingEngine();
    }

    /* =================================================================== */

    public void render(Graphics2D g2, int w, int h, long tick) {
        Block[][][] blocks = world.getBlocks();
        FaceLighting[][][] faceLightings = lighthinEngine.compute(blocks);

        ArrayList<Entity> entities = world.getEntities();
        if (blocks == null)
            return;

        /* ---------- compute camera offset ---------- */
        double originX = w / 2.0;
        double originY = h / 2.0;

        Entity player = world.getPlayer();
        if (player != null) {
            IsoMath.toScreen(player.getX(), player.getY(), player.getZ(), scratchPoint);
            originX -= scratchPoint.x;
            originY -= scratchPoint.y;
        } else {
            originY = h / 4.0;
        }

        /* ---------- clear & prepare canvas ---------- */
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        drawables.clear(); // reuse the same list each frame

        /*
         * =================================================================
         * 1) BLOCKS – add only when visible
         * ==================================================================
         */
        final int maxX = blocks.length - 1;
        final int maxY = blocks[0].length - 1;
        final int maxZ = blocks[0][0].length - 1;

        for (int z = 0; z <= maxZ; z++) {
            for (int y = 0; y <= maxY; y++) {
                for (int x = 0; x <= maxX; x++) {
                    Block b = blocks[x][y][z];
                    if (b == null)
                        continue;

                    boolean[] visibleFaces = getVisibleFaces(blocks, x, y, z, maxX, maxY, maxZ, originX, originY, w, h);
                    if (!visibleFaces[Face.LEFT.index] &&
                            !visibleFaces[Face.RIGHT.index] &&
                            !visibleFaces[Face.TOP.index]) {
                        continue;
                    }

                    /* --- screen-space cull before allocating Drawable --- */
                    IsoMath.toScreen(x, y, z, scratchPoint);
                    double drawX = originX + scratchPoint.x;
                    double drawY = originY + scratchPoint.y;
                    if (drawX + IsoMath.DRAW_TILE_SIZE < 0 || drawX > w ||
                            drawY + IsoMath.DRAW_TILE_SIZE < 0 || drawY > h) {
                        continue; // quad never reaches the viewport
                    }

                    FaceLighting faceLighting = faceLightings[x][y][z];

                    if (visibleFaces[Face.LEFT.index]) {
                        drawables.add(new Drawable(shade(b.getTexture().left(tick), faceLighting.left()), x, y, z));
                    }
                    if (visibleFaces[Face.RIGHT.index]) {
                        drawables.add(new Drawable(shade(b.getTexture().right(tick), faceLighting.right()), x, y, z));
                    }
                    if (visibleFaces[Face.TOP.index]) {
                        drawables.add(new Drawable(shade(b.getTexture().top(tick), faceLighting.top()), x, y, z));
                    }

                }
            }
        }

        /*
         * =================================================================
         * 2) ENTITIES – usually sparse, so we just test screen bounds
         * ==================================================================
         */
        if (entities != null) {
            for (Entity e : entities) {
                if (e == null || e.getTexture() == null)
                    continue;

                IsoMath.toScreen(e.getX(), e.getY(), e.getZ(), scratchPoint);
                double drawX = originX + scratchPoint.x;
                double drawY = originY + scratchPoint.y;
                if (drawX + IsoMath.DRAW_TILE_SIZE < 0 || drawX > w ||
                        drawY + IsoMath.DRAW_TILE_SIZE < 0 || drawY > h) {
                    continue;
                }

                drawables.add(new Drawable(
                        e.getTexture().full(tick), e.getX(), e.getY(), e.getZ()));

                // FaceLighting faceLighting = sampleLighting(faceLightings, e.getX(), e.getY(),
                // e.getZ());

                // drawables.add(new Drawable(shade(e.getTexture().left(tick),
                // faceLighting.left()), e.getX(), e.getY(),
                // e.getZ()));
                // drawables.add(new Drawable(shade(e.getTexture().right(tick),
                // faceLighting.right()), e.getX(), e.getY(),
                // e.getZ()));
                // drawables.add(new Drawable(shade(e.getTexture().top(tick),
                // faceLighting.top()), e.getX(), e.getY(),
                // e.getZ()));

            }
        }

        /* ---------- depth sort & draw ---------- */
        drawables.sort(DEPTH);
        for (Drawable d : drawables) {
            IsoMath.toScreen(d.x, d.y, d.z, scratchPoint);
            int dx = (int) Math.round(originX + scratchPoint.x);
            int dy = (int) Math.round(originY + scratchPoint.y);

            g2.drawImage(d.texture,
                    dx, dy,
                    dx + IsoMath.DRAW_TILE_SIZE,
                    dy + IsoMath.DRAW_TILE_SIZE,
                    0, 0, IsoMath.TILE_SIZE, IsoMath.TILE_SIZE,
                    null);
        }
    }

    /* =================================================================== */

    private boolean isInScreenRange(int x, int y, int z,
            double originX, double originY,
            int screenW, int screenH) {

        // project the tile’s centre to 2-D screen space
        IsoMath.toScreen(x, y, z, scratchPoint);
        double drawX = originX + scratchPoint.x;
        double drawY = originY + scratchPoint.y;

        // the tile is a fixed DRAW_TILE_SIZE × DRAW_TILE_SIZE quad; it is visible
        // iff that quad overlaps the viewport bounds in either axis
        return drawX + IsoMath.DRAW_TILE_SIZE >= 0 && // not completely left
                drawX <= screenW && // not completely right
                drawY + IsoMath.DRAW_TILE_SIZE >= 0 && // not completely above
                drawY <= screenH; // not completely below
    }

    private boolean[] getVisibleFaces(Block[][][] b, int x, int y, int z,
            int maxX, int maxY, int maxZ, double originX, double originY, int w, int h) {
        boolean[] faces = new boolean[] { false, false, false };
        if (!isInScreenRange(x, y, z, originX, originY, w, h))
            return faces;
        if (x == maxX || (b[x + 1][y][z] == null || !b[x + 1][y][z].getTexture().takesFullSpace()))
            faces[Face.RIGHT.index] = true;
        if (y == maxY || b[x][y + 1][z] == null || !b[x][y + 1][z].getTexture().takesFullSpace())
            faces[Face.LEFT.index] = true;
        if (z == maxZ || b[x][y][z + 1] == null || !b[x][y][z + 1].getTexture().takesFullSpace())
            faces[Face.TOP.index] = true;
        return faces;
    }

    /* ---------- modulation couleur (logiciel) ----------------------- */
    private BufferedImage shade(BufferedImage src, ColorRGB c) {
        BufferedImage dst = new BufferedImage(src.getWidth(),
                src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < src.getHeight(); ++y)
            for (int x = 0; x < src.getWidth(); ++x) {
                int argb = src.getRGB(x, y);
                int a = argb >>> 24;
                int r = (int) (((argb >> 16) & 0xFF) * c.r());
                int g = (int) (((argb >> 8) & 0xFF) * c.g());
                int b = (int) (((argb) & 0xFF) * c.b());
                dst.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        return dst;
    }

    /**
     * Returns the lighting at an arbitrary (x,y,z) position by trilinear
     * interpolation of the surrounding 8 grid cells.
     */
    private static FaceLighting sampleLighting(FaceLighting[][][] grid,
            double x, double y, double z) {
        /* --- integer cell corners --- */
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);
        int z0 = (int) Math.floor(z);

        int x1 = Math.min(x0 + 1, grid.length - 1);
        int y1 = Math.min(y0 + 1, grid[0].length - 1);
        int z1 = Math.min(z0 + 1, grid[0][0].length - 1);

        /* --- fractional part inside the cell --- */
        double fx = x - x0, ux = 1.0 - fx; // weights along X
        double fy = y - y0, uy = 1.0 - fy; // weights along Y
        double fz = z - z0, uz = 1.0 - fz; // weights along Z

        /* --- accumulators for the three faces we shade --- */
        double lR = 0, lG = 0, lB = 0; // left face
        double rR = 0, rG = 0, rB = 0; // right face
        double tR = 0, tG = 0, tB = 0; // top face

        /* --- walk through the eight corners --- */
        for (int xi = 0; xi <= 1; xi++) {
            double wx = (xi == 0) ? ux : fx;
            int gx = (xi == 0) ? x0 : x1;

            for (int yi = 0; yi <= 1; yi++) {
                double wy = (yi == 0) ? uy : fy;
                int gy = (yi == 0) ? y0 : y1;

                for (int zi = 0; zi <= 1; zi++) {
                    double wz = (zi == 0) ? uz : fz;
                    int gz = (zi == 0) ? z0 : z1;

                    double w = wx * wy * wz; // full weight
                    FaceLighting fl = grid[gx][gy][gz];
                    if (fl == null) // air -> black contribution
                        continue;

                    ColorRGB cL = fl.left();
                    ColorRGB cR = fl.right();
                    ColorRGB cT = fl.top();

                    lR += cL.r() * w;
                    lG += cL.g() * w;
                    lB += cL.b() * w;
                    rR += cR.r() * w;
                    rG += cR.g() * w;
                    rB += cR.b() * w;
                    tR += cT.r() * w;
                    tG += cT.g() * w;
                    tB += cT.b() * w;
                }
            }
        }

        return new FaceLighting(
                new ColorRGB(lR, lG, lB),
                new ColorRGB(rR, rG, rB),
                new ColorRGB(tR, tG, tB));
    }

}
