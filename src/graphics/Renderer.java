/*  graphics/Renderer.java  */
package graphics;

import graphics.ligth.ColorRGB;
import graphics.ligth.FaceLighting;
import graphics.ligth.LightingEngineAsync;
import graphics.shape.Face;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class Renderer {

    private static final Comparator<Drawable> DEPTH = Comparator.comparingDouble(Drawable::getSortKey);

    private final World world;
    private final LightingEngineAsync lighthingEngine;

    /* ---- reusable scratch objects to avoid GC thrash ---- */
    private final List<Drawable> drawables = new ArrayList<>(1024);
    private final Point2D.Double scratchPoint = new Point2D.Double();

    private final List<TextLabel> textLabels = new ArrayList<>(128);

    private static final Font PIXEL_FONT;

    private static BufferedImage frameBuffer;
    private static Graphics2D gBuffer;

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
        this.lighthingEngine = new LightingEngineAsync();
    }

    public void initRenderer(int w, int h) {
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        frameBuffer = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        gBuffer = frameBuffer.createGraphics();

        gBuffer.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        gBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    public void render(Graphics2D g2, int w, int h, long tick) {
        Block[][][] blocks = world.getBlocks();
        if (blocks == null)
            return;

        ArrayList<Entity> entities = world.getEntities();
        FaceLighting[][][] faceLightings = lighthingEngine.getLightings(world, tick);

        double originXi;
        double originYi;

        synchronized (world) {
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
            originXi = Math.floor(camX);
            originYi = Math.floor(camY);

            /* ---------- clear & prepare canvas ---------- */
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, w, h);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            /* ---------- depth sort & draw ---------- */
            getDrawablesLabels(w, h, tick, originXi, originYi, blocks, entities, faceLightings);
        }
        
        if (frameBuffer == null)
            initRenderer(w, h);
        else {
            gBuffer.setComposite(AlphaComposite.Clear);
            gBuffer.fillRect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
            gBuffer.setComposite(AlphaComposite.SrcOver);
        }

        // draw drawables (avec culling ici)
        for (Drawable d : drawables) {
            IsoMath.toScreen(d.x, d.y, d.z, scratchPoint);
            int dx = (int) (originXi + scratchPoint.x);
            int dy = (int) (originYi + scratchPoint.y);
            gBuffer.drawImage(d.texture,
                    dx, dy,
                    dx + IsoMath.DRAW_TILE_SIZE,
                    dy + IsoMath.DRAW_TILE_SIZE,
                    0, 0, IsoMath.TILE_SIZE, IsoMath.TILE_SIZE,
                    null);
        }
        g2.drawImage(frameBuffer, 0, 0, null);

        /*
        * ──────────────────────────────────────────────────────────────
        * 3) draw all labels last
        * ----------------------------------------------------------------
        */
        g2.setFont(PIXEL_FONT);

        for (TextLabel tl : textLabels) {
            /* world → screen */
            IsoMath.toScreen(tl.x, tl.y, tl.z, scratchPoint);
            int sx = (int)(originXi + scratchPoint.x + IsoMath.DRAW_TILE_SIZE / 2.0);
            int sy = (int)(originYi + scratchPoint.y - 32); // anchor (bottom)

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

    private void getDrawablesLabels(int w, int h, long tick, double originXi, double originYi, Block[][][] blocks, ArrayList<Entity> entities, FaceLighting[][][] faceLightings) {
        drawables.clear();
        textLabels.clear();

        final int maxX = blocks.length - 1;
        final int maxY = blocks[0].length - 1;
        final int maxZ = blocks[0][0].length - 1;

        boolean[][][] visibles = getVisibleBlocks(blocks, originXi, originYi, w, h);

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

                    if (!visibles[x][y][z])
                        continue;

                    if (!getVisibleFace(blocks, x, y, z, maxX, maxY, maxZ))
                        continue;

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
                    BufferedImage image = applyColor(text.full(tick - b.getTickFrame0()), b.getColor());
                    drawables.add(new Drawable(text.shade(image, faceLighting.left(),
                                faceLighting.right(), faceLighting.top()), x, y, z, false, alwaysBehind));
                }
            }
        }

        /*
         * =================================================================
         * 1-bis) FOG – border voxels above solid blocks
         * =================================================================
         */
        for (Map.Entry<Vector, Block> f : world.getFogMap().entrySet()) {
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
            drawables.add(new Drawable(b.getTexture().full(tick - b.getTickFrame0()),
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

                FaceLighting faceLighting = sampleLighting(faceLightings, e.getX() - 0.5, e.getY() - 0.5,
                        e.getZ() - 0.5);

                Texture text = e.getTexture();
                BufferedImage image = applyColor(text.full(tick - e.getTickFrame0()), e.getColor());
                drawables.add(new Drawable(text.shade(
                        image,faceLighting.left(), faceLighting.right(), faceLighting.top()), e.getX(), e.getY(), e.getZ(), true));
            }
        }

        // System.out.println(drawables.size());
        drawables.sort(DEPTH);
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

    private boolean[][][] getVisibleBlocks(Block[][][] b, double originX, double originY, int screenW, int screenH) {
        final int SIZE_X = b.length;
        final int SIZE_Y = b[0].length;
        final int SIZE_Z = b[0][0].length;
        boolean visibles[][][] = new boolean[SIZE_X][SIZE_Y][SIZE_Z];
        // top
        final int zS = SIZE_Z - 1;
        for (int x = 0; x < SIZE_X; x++)
            for (int y = 0; y < SIZE_Y; y++)
                if (isInScreenRange(x, y, zS, originX, originY, screenW, screenH))
                    getVisibleBlockLine(b, visibles, x, y, zS);
        // left
        final int yS = SIZE_Y - 1;
        for (int x = 0; x < SIZE_X; x++)
            for (int z = 0; z < SIZE_Z - 1; z++)
                if (isInScreenRange(x, yS, z, originX, originY, screenW, screenH))
                    getVisibleBlockLine(b, visibles, x, yS, z);
        // right
        final int xS = SIZE_X - 1;
        for (int y = 0; y < SIZE_Y - 1; y++)
            for (int z = 0; z < SIZE_Z - 1; z++)
                if (isInScreenRange(xS, y, z, originX, originY, screenW, screenH))
                    getVisibleBlockLine(b, visibles, xS, y, z);
        
        return visibles;
    }

    private void getVisibleBlockLine(Block[][][] b, boolean visibles[][][], int x, int y, int z) {
        visibles[x][y][z] = true;
        boolean hideBehind = false;
        Block block = b[x][y][z];
        if (block != null) {
            Texture texture = block.getTexture();
            if (texture != null)
                hideBehind = texture.getHideBehind();
        }
        if (!hideBehind && x > 0 && y > 0 && z > 0)
            getVisibleBlockLine(b, visibles, x - 1, y - 1, z - 1);
    }

    private boolean getVisibleFace(Block[][][] b, int x, int y, int z, int maxX, int maxY, int maxZ) {

        if (x == maxX || visibilityChecker(b[x + 1][y][z], Face.TOP.index)) {
            return true;
        }
        if (y == maxY || visibilityChecker(b[x][y + 1][z], Face.RIGHT.index)) {
            return true;
        }
        if (z == maxZ || visibilityChecker(b[x][y][z + 1], Face.LEFT.index)) {
            return true;
        }

        return false;
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

    public static BufferedImage applyColor(BufferedImage img, int[] color) {
        if (img == null || color == null || color.length != 3)
            return img;

        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int rTarget = color[0];
        int gTarget = color[1];
        int bTarget = color[2];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = img.getRGB(x, y);

                int alpha = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                // calcul de la luminosité du pixel (0-255)
                int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);

                // applique la couleur en conservant la luminosité
                int newR = Math.min(255, (rTarget * lum) / 255);
                int newG = Math.min(255, (gTarget * lum) / 255);
                int newB = Math.min(255, (bTarget * lum) / 255);

                int newArgb = (alpha << 24) | (newR << 16) | (newG << 8) | newB;
                result.setRGB(x, y, newArgb);
            }
        }

        return result;
    }
}
