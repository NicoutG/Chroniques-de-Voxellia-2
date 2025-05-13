/*  graphics/Renderer.java  */
package graphics;

import block.Block;
import entity.Entity;
import graphics.shape.Face;
import model.world.World;

import java.awt.*;
import java.awt.geom.Point2D;
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

    /* ---- reusable scratch objects to avoid GC thrash ---- */
    private final List<Drawable> drawables = new ArrayList<>(1024);
    private final Point2D.Double scratchPoint = new Point2D.Double();

    public Renderer(World world) {
        this.world = world;
    }

    /* =================================================================== */

    public void render(Graphics2D g2, int w, int h, long tick) {
        Block[][][] blocks = world.getBlocks();
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

                    if (visibleFaces[Face.LEFT.index]) {
                        drawables.add(new Drawable(
                                b.getTexture().left(tick), x, y, z));
                    }
                    if (visibleFaces[Face.RIGHT.index]) {
                        drawables.add(new Drawable(
                                b.getTexture().right(tick), x, y, z));
                    }
                    if (visibleFaces[Face.TOP.index]) {
                        drawables.add(new Drawable(
                                b.getTexture().top(tick), x, y, z));
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
}
