/*  graphics/Renderer.java  */
package graphics;

import engine.Block;
import entity.Entity;
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
        Entity[] entities = world.getEntities();
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
         * 1) BLOCKS – add only when “exposed” AND visible on screen
         * ==================================================================
         */
        final int maxX = blocks.length - 1;
        final int maxY = blocks[0].length - 1;
        final int maxZ = blocks[0][0].length - 1;

        int c = 0;
        int total = 0;

        for (int z = 0; z <= maxZ; z++) {
            for (int y = 0; y <= maxY; y++) {
                for (int x = 0; x <= maxX; x++) {

                    Block b = blocks[x][y][z];
                    if (b == null)
                        continue;

                    total++;
                    if (!isExposed(blocks, x, y, z, maxX, maxY, maxZ))
                        continue;
                    c++;

                    /* --- screen-space cull before allocating Drawable --- */
                    IsoMath.toScreen(x, y, z, scratchPoint);
                    double drawX = originX + scratchPoint.x;
                    double drawY = originY + scratchPoint.y;
                    if (drawX + IsoMath.DRAW_TILE_SIZE < 0 || drawX > w ||
                            drawY + IsoMath.DRAW_TILE_SIZE < 0 || drawY > h) {
                        continue; // quad never reaches the viewport
                    }

                    drawables.add(new Drawable(
                            b.getTexture().full(tick), x, y, z));
                }
            }
        }

        System.out.println("Blocks renderer : " + c + " - Total : " + total);

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

    /** “Interior” test: true if at least one of the 6 neighbours is empty. */
    private static boolean isExposed(Block[][][] b, int x, int y, int z,
            int maxX, int maxY, int maxZ) {

        return (x == maxX || (b[x + 1][y][z] == null || !b[x + 1][y][z].getTexture().takesFullSpace())) ||
                (y == maxY || b[x][y + 1][z] == null || !b[x + 1][y][z].getTexture().takesFullSpace()) ||
                (z == maxZ || b[x][y][z + 1] == null || !b[x + 1][y][z].getTexture().takesFullSpace());
    }
}
