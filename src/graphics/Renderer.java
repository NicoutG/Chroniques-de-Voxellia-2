/*  graphics/Renderer.java  */
package graphics;

import graphics.fog.FogManager;
import graphics.ligth.ColorRGB;
import graphics.ligth.FaceLighting;
import graphics.ligth.LightingEngine;
import graphics.shape.Face;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final List<TextLabel> textLabels = new ArrayList<>(128);

    private static final Font PIXEL_FONT;

    private Map<Vector, Block> fogMap = new HashMap();

    private final FogManager fogManager = new FogManager();

    static {
        Font f;
        try (InputStream is = Renderer.class.getResourceAsStream("/resources/fonts/basis33.ttf")) {
            f = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .registerFont(f);
        } catch (FontFormatException | IOException e) {
            f = new Font("Monospaced", Font.PLAIN, 24);
        }
        // derive at your default size:
        PIXEL_FONT = f.deriveFont(Font.PLAIN, 36f);
    }

    public Renderer(World world) {
        this.world = world;
        this.lighthinEngine = new LightingEngine();
    }

    /* =================================================================== */

    public void render(Graphics2D g2, int w, int h, long tick) {
        Block[][][] blocks = world.getBlocks();
        if (blocks == null)
            return;

        ArrayList<Entity> entities = world.getEntities();
        FaceLighting[][][] faceLightings = lighthinEngine.getLightings(world, tick);

        if (fogMap.size() == 0) {
            fogMap = fogManager.getFogMap(blocks);
        }

        /* ---------- compute camera offset ---------- */
        double camX = w / 2.0;
        double camY = h / 2.0;

        Entity player = world.getPlayer();
        if (player != null) {
            IsoMath.toScreen(player.getX(), player.getY(), player.getZ(), scratchPoint);
            camX -= scratchPoint.x;
            camY -= scratchPoint.y;
        } else {
            camY = h / 4.0;
        }

        /* ── snap the camera once ───────────────────────────────────── */
        int originXi = (int) Math.floor(camX);
        int originYi = (int) Math.floor(camY);

        /* ---------- clear & prepare canvas ---------- */
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        drawables.clear();
        textLabels.clear();

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

                    String lbl = (String) b.getState("text");
                    if (lbl != null && !lbl.isBlank())
                        textLabels.add(new TextLabel(lbl, x, y, z));

                    if (b.getTexture() == null)
                        continue;

                    boolean[] visibleFaces = getVisibleFaces(blocks, x, y, z, maxX, maxY, maxZ, originXi, originYi, w,
                            h);
                    if (!visibleFaces[Face.LEFT.index] &&
                            !visibleFaces[Face.RIGHT.index] &&
                            !visibleFaces[Face.TOP.index]) {
                        continue;
                    }

                    /* --- screen-space cull before allocating Drawable --- */
                    IsoMath.toScreen(x, y, z, scratchPoint);
                    double drawX = originXi + scratchPoint.x;
                    double drawY = originYi + scratchPoint.y;
                    if (drawX + IsoMath.DRAW_TILE_SIZE < 0 || drawX > w ||
                            drawY + IsoMath.DRAW_TILE_SIZE < 0 || drawY > h) {
                        continue;
                    }

                    if (b.getTexture() == null)
                        continue;

                    FaceLighting faceLighting = faceLightings[x][y][z];

                    boolean alwaysBehind = b.getProperty("floor") != null;

                    Texture text = b.getTexture();
                    if (visibleFaces[Face.LEFT.index]) {
                        drawables.add(new Drawable(text.shade(text.left(tick), faceLighting.left(),
                                faceLighting.right(), faceLighting.top()), x, y, z, false, alwaysBehind));
                    }
                    if (visibleFaces[Face.RIGHT.index]) {
                        drawables.add(new Drawable(text.shade(text.right(tick), faceLighting.left(),
                                faceLighting.right(), faceLighting.top()), x, y, z, false, alwaysBehind));
                    }
                    if (visibleFaces[Face.TOP.index]) {
                        drawables.add(new Drawable(text.shade(text.top(tick), faceLighting.left(), faceLighting.right(),
                                faceLighting.top()), x, y, z, false, alwaysBehind));
                    }

                }
            }
        }

        /*
         * =================================================================
         * 1-bis) FOG – border voxels above solid blocks
         * =================================================================
         */
        for (Map.Entry<Vector, Block> f : fogMap.entrySet()) {
            Vector v = f.getKey();
            Block b = f.getValue();
            if (b.getTexture() == null)
                continue;

            /* simple visibility cull */
            IsoMath.toScreen(v.x, v.y, v.z, scratchPoint);
            double drawX = originXi + scratchPoint.x;
            double drawY = originYi + scratchPoint.y;
            if (drawX + IsoMath.DRAW_TILE_SIZE < 0 || drawX > w ||
                    drawY + IsoMath.DRAW_TILE_SIZE < 0 || drawY > h)
                continue;

            /* one quad is enough: use the texture’s full sprite */
            drawables.add(new Drawable(b.getTexture().full(tick),
                    v.x, v.y, v.z,
                    true));
        }

        /*
         * =================================================================
         * 2) ENTITIES – usually sparse, so we just test screen bounds
         * ==================================================================
         */
        if (entities != null) {
            for (Entity e : entities) {
                if (e == null)
                    continue;

                String lbl = (String) e.getState("text");
                if (lbl != null && !lbl.isBlank())
                    textLabels.add(new TextLabel(lbl, e.getX(), e.getY(), e.getZ()));

                if (e.getTexture() == null)
                    continue;

                IsoMath.toScreen(e.getX(), e.getY(), e.getZ(), scratchPoint);
                double drawX = originXi + scratchPoint.x;
                double drawY = originYi + scratchPoint.y;
                if (drawX + IsoMath.DRAW_TILE_SIZE < 0 || drawX > w ||
                        drawY + IsoMath.DRAW_TILE_SIZE < 0 || drawY > h) {
                    continue;
                }

                drawables.add(new Drawable(
                        e.getTexture().full(tick), e.getX(), e.getY(), e.getZ(), true));

                FaceLighting faceLighting = sampleLighting(faceLightings, e.getX() - 0.5, e.getY() - 0.5,
                        e.getZ() - 0.5);

                Texture text = e.getTexture();
                drawables.add(new Drawable(text.shade(text.left(tick),
                        faceLighting.left(), faceLighting.right(), faceLighting.top()), e.getX(), e.getY(),
                        e.getZ(), true));
                drawables.add(new Drawable(text.shade(text.right(tick),
                        faceLighting.left(), faceLighting.right(), faceLighting.top()), e.getX(), e.getY(),
                        e.getZ(), true));
                drawables.add(new Drawable(text.shade(text.top(tick),
                        faceLighting.left(), faceLighting.right(), faceLighting.top()), e.getX(), e.getY(),
                        e.getZ(), true));

            }
        }

        /* ---------- depth sort & draw ---------- */
        drawables.sort(DEPTH);
        for (Drawable d : drawables) {
            IsoMath.toScreen(d.x, d.y, d.z, scratchPoint);
            int dx = originXi + (int) scratchPoint.x;
            int dy = originYi + (int) scratchPoint.y;

            g2.drawImage(d.texture,
                    dx, dy,
                    dx + IsoMath.DRAW_TILE_SIZE,
                    dy + IsoMath.DRAW_TILE_SIZE,
                    0, 0, IsoMath.TILE_SIZE, IsoMath.TILE_SIZE,
                    null);
        }

        /*
         * ──────────────────────────────────────────────────────────────
         * 3) draw all labels last
         * ----------------------------------------------------------------
         */
        g2.setFont(PIXEL_FONT);

        for (TextLabel tl : textLabels) {
            /* world → screen */
            IsoMath.toScreen(tl.x, tl.y, tl.z, scratchPoint);
            int sx = originXi + (int) scratchPoint.x + IsoMath.DRAW_TILE_SIZE / 2;
            int sy = originYi + (int) scratchPoint.y - 32; // anchor (bottom)

            /* word-wrap once per label */
            List<String> lines = wrapLines(tl.text, 50);

            FontMetrics fm = g2.getFontMetrics();
            int lineH = fm.getHeight();
            int ascent = fm.getAscent(); // baseline offset inside a line
            int blockTopY = sy - (lines.size() - 1) * lineH; // topmost line Y

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                int tx = sx - fm.stringWidth(line) / 2; // center this line
                int ty = blockTopY + i * lineH + ascent; // baseline of line i

                /* outline */
                g2.setColor(new Color(0, 0, 0, 150));
                g2.drawString(line, tx - 2, ty);
                g2.drawString(line, tx + 2, ty);
                g2.drawString(line, tx, ty - 2);
                g2.drawString(line, tx, ty + 2);

                /* interior */
                g2.setColor(new Color(255, 255, 255, 240));
                g2.drawString(line, tx, ty);
            }
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

    private boolean visibilityChecker(Block b, int direction) {
        if (b == null || b.getTexture() == null || b.getOpacity() < 1 || b.isLightAllowed(direction)
                || (b.getTexture() != null && !b.getTexture().takesFullSpace())) {
            return true;
        }
        return false;

    }

    private boolean[] getVisibleFaces(Block[][][] b, int x, int y, int z,
            int maxX, int maxY, int maxZ, double originX, double originY, int w, int h) {
        boolean[] faces = new boolean[] { false, false, false };
        if (!isInScreenRange(x, y, z, originX, originY, w, h))
            return faces;

        if (x == maxX || visibilityChecker(b[x + 1][y][z], Face.TOP.index)) {
            faces[Face.RIGHT.index] = true;
        }
        if (y == maxY || visibilityChecker(b[x][y + 1][z], Face.RIGHT.index)) {
            faces[Face.LEFT.index] = true;
        }
        if (z == maxZ || visibilityChecker(b[x][y][z + 1], Face.LEFT.index)) {
            faces[Face.TOP.index] = true;
        }

        return faces;
    }

    /**
     * Returns the lighting for an entity by rounding its position to the
     * nearest voxel and sampling the cube that sits next to it in the
     * direction of each rendered face.
     *
     * Right face → cube at (x , y+1 , z) → use its Face.RIGHT colour
     * Left face → cube at (x+1 , y , z) → use its Face.LEFT colour
     * Top face → cube at (x , y , z+1) → use its Face.TOP colour
     *
     * Out-of-bounds or air blocks contribute BLACK.
     */
    private static FaceLighting sampleLighting(FaceLighting[][][] grid,
            double x, double y, double z) {

        /* ----- round the entity position ----- */
        int xr = (int) Math.round(x);
        int yr = (int) Math.round(y);
        int zr = (int) Math.round(z);

        int maxX = grid.length - 1;
        int maxY = grid[0].length - 1;
        int maxZ = grid[0][0].length - 1;

        /* clamp to valid voxel indices */
        xr = Math.max(0, Math.min(xr, maxX));
        yr = Math.max(0, Math.min(yr, maxY));
        zr = Math.max(0, Math.min(zr, maxZ));

        /* ----- neighbour coordinates for each face ----- */
        int nxR = (yr < maxY) ? yr + 1 : yr; // +Y (Right face)
        int nxL = (xr < maxX) ? xr + 1 : xr; // +X (Left face)
        int nzT = (zr < maxZ) ? zr + 1 : zr; // +Z (Top face)

        /* ----- fetch colours, defaulting to black on air/out-of-range ----- */
        ColorRGB cRight = ColorRGB.BLACK;
        FaceLighting fl = grid[xr][nxR][zr];
        if (fl != null) {
            if (fl.right().isBlack()) {
                fl = grid[xr][yr][zr];
                if (fl != null) {
                    cRight = fl.right();
                }
            } else {
                cRight = fl.right();
            }
        }

        ColorRGB cLeft = ColorRGB.BLACK;
        fl = grid[nxL][yr][zr];
        if (fl != null) {
            if (fl.left().isBlack()) {
                fl = grid[xr][yr][zr];
                if (fl != null) {
                    cLeft = fl.left();
                }
            } else {
                cLeft = fl.left();
            }
        }

        ColorRGB cTop = ColorRGB.BLACK;
        fl = grid[xr][yr][nzT];
        if (fl != null)
            if (fl.top().isBlack()) {
                fl = grid[xr][yr][zr];
                if (fl != null) {
                    cTop = fl.top();
                }
            } else {
                cTop = fl.top();
            }

        return new FaceLighting(cRight, cLeft, cTop);
    }

    /**
     * Splits text into word-wrapped lines whose length never exceeds max.
     * Words longer than max are left on their own line.
     */
    public static List<String> wrapLines(String text, int max) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isBlank())
            return lines;

        StringBuilder current = new StringBuilder();
        for (String word : text.split("\\s+")) {
            if (current.length() == 0) {
                current.append(word);
            } else if (current.length() + 1 + word.length() <= max) {
                current.append(' ').append(word);
            } else { // overflow → start a new line
                lines.add(current.toString());
                current.setLength(0);
                current.append(word);
            }
        }
        if (current.length() > 0)
            lines.add(current.toString());

        return lines;
    }

}
