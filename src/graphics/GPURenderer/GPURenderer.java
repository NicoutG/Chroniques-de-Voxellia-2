package graphics.GPURenderer;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import graphics.IsoMath;
import graphics.Texture;
import graphics.ligth.ColorRGB;
import graphics.ligth.FaceLighting;
import graphics.ligth.LightingEngineAsync;
import graphics.shape.Face;
import graphics.shape.Shape;
import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class GPURenderer {
    private static final Comparator<Sprite> DEPTH = Comparator.comparingDouble(Sprite::getSortKey);

    private final World world;
    private final LightingEngineAsync lighthingEngine;

    private static long lastRenderer = 0;
    private static long difLastRenderer = 0;

    private final Point2D.Double scratchPoint = new Point2D.Double();

    private double lastOriginX = 0;
    private double lastOriginY = 0;
    private final static double TRANSITION = 0.004;

    public GPURenderer(World world) {
        this.world = world;
        this.lighthingEngine = new LightingEngineAsync();
        TextToImage.init();
    }

    public void render(int w, int h, long tick) {
        long now = System.currentTimeMillis();
        difLastRenderer = now - lastRenderer;
        Block[][][] blocks = world.getBlocks();
        if (blocks == null)
            return;

        ArrayList<Entity> entities = world.getEntities();


        double originXi;
        double originYi;

        List<Sprite> sprites = null;

        synchronized (world) {
            FaceLighting[][][] faceLightings = lighthingEngine.getLightings(world, tick);

            /* ---------- compute camera offset ---------- */
            double camX = w / 2.0;
            double camY = h / 2.0;

            Entity player = world.getPlayer();
            if (player != null) {
                // convert player coords to screen
                IsoMath.toScreen(player.getX(), player.getY(), player.getZ(), scratchPoint);

                // centrer caméra sur le joueur
                camX -= scratchPoint.x;
                camY -= scratchPoint.y;
            } else {
                camY = h / 4.0;
            }


            /* ── snap the camera once ───────────────────────────────────── */
            originXi = Math.floor(camX);
            originYi = Math.floor(camY);
            updateOrigin(originXi, originYi);

            /* ---------- depth sort & draw ---------- */
           sprites = getSprites(w, h, tick, blocks, entities, faceLightings);
        }
        
        GLImageRenderer.renderer(sprites);
        
        lastRenderer = now;
    }

    private List<Sprite> getSprites(int w, int h, long tick, Block[][][] blocks, ArrayList<Entity> entities, FaceLighting[][][] faceLightings) {
        List<Sprite> sprites = new ArrayList<>(500);

        List<Sprite> labels = new ArrayList<>();
        
        final int maxX = blocks.length - 1;
        final int maxY = blocks[0].length - 1;
        final int maxZ = blocks[0][0].length - 1;

        boolean[][][] visibles = getVisibleBlocks(blocks, lastOriginX, lastOriginY, w, h);

        /*
         * =================================================================
         * 0) BLOCKS
         * ==================================================================
         */
        for (int z = 0; z <= maxZ; z++) {
            for (int y = 0; y <= maxY; y++) {
                for (int x = 0; x <= maxX; x++) {
                    Block b = blocks[x][y][z];
                    if (b == null)
                        continue;

                    String lbl = (String) b.getState("text");
                    if (lbl != null && !lbl.isBlank())
                        labels.add(createSpriteLabel(lbl, x, y, z, w, h));

                    if (b.getTexture() == null)
                        continue;

                    if (!visibles[x][y][z])
                        continue;

                    if (!getVisibleFace(blocks, x, y, z, maxX, maxY, maxZ))
                        continue;

                    FaceLighting faceLighting = getFaceLighting(faceLightings, x, y, z);

                    Texture text = b.getTexture();
                    int[] color = b.getColor();
                    boolean alwaysBehind = (b.getProperty("floor") != null);
                    double[] xy = transformation3DTo2D(x, y, z, w, h);
                    BufferedImage image = text.full(tick - b.getTickFrame0());
                    Sprite sprite = new Sprite((int)xy[0], (int)xy[1], image);
                    sprite.set3D(x, y, z);
                    sprite.alwaysBehind = alwaysBehind;
                    sprite.color = color;
                    sprite = addMask(sprite, text.getShape(), faceLighting);
                    sprite.setSize(IsoMath.DRAW_TILE_SIZE);
                    sprites.add(sprite);
                    if (sprite.maskLeft == null || sprite.maskRight == null || sprite.maskTop == null)
                        System.out.println(b.getName());
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

            double x = v.x;
            double y = v.y;
            double z = v.z;

            double[] xy = transformation3DTo2D(x, y, z, w, h);
            double drawX = xy[0];
            double drawY = xy[1];
            if (drawX + IsoMath.DRAW_TILE_SIZE < 0 || drawX > w ||
                    drawY + IsoMath.DRAW_TILE_SIZE < 0 || drawY > h)
                continue;

            Texture text = b.getTexture();
            Sprite sprite = new Sprite(drawX, drawY, text.full(tick - b.getTickFrame0()));
            sprite.set3D(x, y, z);
            sprite.requiresCorrection = true;
            sprite.setSize(IsoMath.DRAW_TILE_SIZE);
            sprites.add(sprite);
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

                double x = e.getX() - 0.5;
                double y = e.getY() - 0.5;
                double z = e.getZ() - 0.5;

                String lbl = (String) e.getState("text");
                if (lbl != null && !lbl.isBlank())
                    labels.add(createSpriteLabel(lbl, x, y, z, w, h));

                if (e.getTexture() == null)
                    continue;


                FaceLighting faceLighting = sampleLighting(faceLightings, x, y, z);
                Texture text = e.getTexture();
                int[] color = e.getColor();
                double[] xy = transformation3DTo2D(x, y, z, w, h);
                BufferedImage image = text.full(tick - e.getTickFrame0());
                Sprite sprite = new Sprite((int)xy[0], (int)xy[1], image);
                sprite.set3D(x + 0.5, y + 0.5, z + 0.5);
                sprite.color = color;
                sprite.requiresCorrection = true;
                sprite = addMask(sprite, text.getShape(), faceLighting);
                sprite.setSize(IsoMath.DRAW_TILE_SIZE);
                sprites.add(sprite);
            }
        }

        sprites.sort(DEPTH);
        labels.sort(DEPTH);
        sprites.addAll(labels);

        return sprites;
    }

    private FaceLighting getFaceLighting(FaceLighting[][][] faceLightings,int x, int y, int z) {
        if ((0 <= x && x < faceLightings.length) && (0 <= y && y < faceLightings[0].length) && (0 <= z && z < faceLightings[0][0].length))
            return faceLightings[x][y][z];
        return new FaceLighting();
    }

    private Sprite createSpriteLabel(String label, double x, double y, double z, int w, int h) {
        double[] xy = transformation3DTo2D(x, y, z, w, h);
        BufferedImage img = TextToImage.createTextImage(label);
        Sprite sprite = new Sprite(xy[0] - img.getWidth() / 2 + 50, xy[1] -10, img);
        sprite.set3D(x, y, z);
        return sprite;
    }

    private Sprite addMask(Sprite sprite, Shape shape, FaceLighting faceLighting) {
        sprite.maskLeft = shape.getLeftMask();
        ColorRGB colorLight = faceLighting.left();
        sprite.lightLeft = new float[] {(float)colorLight.r(), (float)colorLight.g(), (float)colorLight.b()};
        sprite.maskRight = shape.getRightMask();
        colorLight = faceLighting.right();
        sprite.lightRight = new float[] {(float)colorLight.r(), (float)colorLight.g(), (float)colorLight.b()};
        sprite.maskTop = shape.getTopMask();
        colorLight = faceLighting.top();
        sprite.lightTop = new float[] {(float)colorLight.r(), (float)colorLight.g(), (float)colorLight.b()};
        return sprite;
    }

    private double[] transformation3DTo2D(double x, double y, double z, int w, int h) { 
        IsoMath.toScreen(x, y, z, scratchPoint); 
        int dx = (int) (lastOriginX + scratchPoint.x); 
        int dy = (int) (lastOriginY + scratchPoint.y); 
        return new double[] { dx, dy }; 
    }

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
        if (b == null || b.getTexture() == null || b.isLightAllowed(direction)
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
                hideBehind = texture.takesFullSpace();
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
    
    private void updateOrigin(double newOriginX, double newOriginY) {
        for (int i = 0; i < difLastRenderer; i++) {
            double difX = newOriginX - lastOriginX;
            if (Math.abs(difX) < 2) {
                lastOriginX = newOriginX;
                break;
            }
            else
                lastOriginX += TRANSITION * difX ;
        }
        for (int i = 0; i < difLastRenderer; i++) {
            double difY = newOriginY - lastOriginY;
            if (Math.abs(difY) < 2) {
                lastOriginY = newOriginY;
                break;
            }
            else
                lastOriginY += TRANSITION * difY ;
        }
    }
}
