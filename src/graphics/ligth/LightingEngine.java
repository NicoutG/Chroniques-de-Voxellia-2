/*  graphics/light/LightingEngine.java  */
package graphics.ligth;

import java.util.ArrayDeque;
import java.util.ArrayList;

import objects.ObjectInstance;
import objects.block.Block;
import objects.entity.Entity;
import objects.property.PropertyLight;
import world.World;
import graphics.shape.Face;

public class LightingEngine {

    private static final double EPS = 0.01; // perceptible limit
    private static final int MAX_NEIGHBOURS = 6; // axis-aligned
    private static final double TRANSITION = 0.05; // color change between frames

    private static String previousWorld = "";
    private static FaceLighting[][][] previousLightings = null;

    /* ---------- small helpers ---------- */

    /** Pre-built array of opposite directions, index-aligned with Voxel.DIR. */
    private static final byte[] OPPOSITE_IDX = { 1, 0, 3, 2, 5, 4 };

    /** Cheap test that avoids calling ColorRGB#equals on every voxel. */
    private static boolean isBlack(ColorRGB c) {
        return c == null || c.isBlack(); // ‘isBlack()’ should be a simple “== BLACK”
    }

    public FaceLighting[][][] getLightings(World world, long tick) {
        FaceLighting[][][] newLightings = compute(world.getBlocks(), world.getEntities(), tick);
        if (world.getCurrentWorld().equals(previousWorld)) {
            final int X = newLightings.length;
            final int Y = newLightings[0].length;
            final int Z = newLightings[0][0].length;
            for (int x = 0; x < X; x++)
                for (int y = 0; y < Y; y++)
                    for (int z = 0; z < Z; z++) {
                        FaceLighting newLighting = newLightings[x][y][z];
                        FaceLighting previousLighting = previousLightings[x][y][z];
                        newLightings[x][y][z] = updateLighting(previousLighting, newLighting);
                    }
        }
        previousWorld = world.getCurrentWorld();
        previousLightings = newLightings;
        return newLightings;
    }

    private FaceLighting updateLighting(FaceLighting previousLighting, FaceLighting newLighting) {
        ColorRGB previousLeft = previousLighting.left();
        ColorRGB newLeft = newLighting.left();
        ColorRGB colorLeft = new ColorRGB(updateValue(previousLeft.r(), newLeft.r()), updateValue(previousLeft.g(), newLeft.g()), updateValue(previousLeft.b(), newLeft.b()));
        ColorRGB previousRight = previousLighting.right();
        ColorRGB newRight = newLighting.right();
        ColorRGB colorRight = new ColorRGB(updateValue(previousRight.r(), newRight.r()), updateValue(previousRight.g(), newRight.g()), updateValue(previousRight.b(), newRight.b()));
        ColorRGB previousTop = previousLighting.top();
        ColorRGB newTop = newLighting.top();
        ColorRGB colorTop = new ColorRGB(updateValue(previousTop.r(), newTop.r()), updateValue(previousTop.g(), newTop.g()), updateValue(previousTop.b(), newTop.b()));
        return new FaceLighting(colorLeft, colorRight, colorTop);
    }

    private double updateValue(double previousValue, double newValue) {
        double dif = newValue - previousValue;
        if (0 < dif)
            return Math.min(newValue, previousValue + TRANSITION);
        return Math.max(newValue, previousValue - TRANSITION);
    }

    /* ===================================================================== */
    /* ENGINE */
    /* ===================================================================== */
    /* ===================================================================== */
    /* ENGINE */
    /* ===================================================================== */
    private FaceLighting[][][] compute(Block[][][] blocks, ArrayList<Entity> entities, long tick) {

        final int X = blocks.length;
        final int Y = blocks[0].length;
        final int Z = blocks[0][0].length;

        /* ---------- output buffers ---------- */
        FaceLighting[][][] out = new FaceLighting[X][Y][Z];
        ColorRGB[][][] outC = new ColorRGB[X][Y][Z];

        for (int x = 0; x < X; ++x)
            for (int y = 0; y < Y; ++y)
                for (int z = 0; z < Z; ++z)
                    out[x][y][z] = new FaceLighting();

        /* ---------- 1) collect light sources ---------- */
        ColorRGB ambient = ColorRGB.BLACK; // sum of every “master” light
        ArrayDeque<Voxel> sources = new ArrayDeque<>(); // only falloff < 1
        // collect block light sources
        for (int x = 0; x < X; ++x)
            for (int y = 0; y < Y; ++y)
                for (int z = 0; z < Z; ++z) {
                    Block b = blocks[x][y][z];
                    ambient = addIfLightSource(ambient, sources, tick, b, x, y, z);
                }

        // collect entity light sources
        for (Entity e : entities) {
            int x = (int)e.getX();
            int y = (int)e.getY();
            int z = (int)e.getZ();
            if (0 <= x && x < X && 0 <= y && y < Y && 0 <= z && z < Z)
                ambient = addIfLightSource(ambient, sources, tick, e, x, y, z);
        }

        /* ---------- 1-bis) fill every voxel with ambient light ---------- */
        if (!ambient.isBlack()) {
            for (int x = 0; x < X; ++x)
                for (int y = 0; y < Y; ++y)
                    for (int z = 0; z < Z; ++z)
                        outC[x][y][z] = ambient; // same object is fine (immutable)
        }

        /* ---------- reusable scratch buffers ---------- */
        final int[][][] visited = new int[X][Y][Z]; // 0 ⇒ never visited
        int visitTag = 1;

        final ArrayDeque<Voxel> q = new ArrayDeque<>();
        final byte[] newRule = new byte[MAX_NEIGHBOURS];
        final Voxel[] children = new Voxel[MAX_NEIGHBOURS];

        /* ================================================================= */
        /* 2) propagate every **non-master** source */
        /* ================================================================= */
        for (Voxel src : sources) {

            /* inject full power on the source voxel */
            out[src.getX()][src.getY()][src.getZ()]
                    .inject(src.getColor().mul(src.getIntensity()));

            q.clear();
            q.addLast(src);
            final int tag = visitTag++;

            while (!q.isEmpty()) {
                Voxel v = q.removeFirst();
                int vx = v.getX(), vy = v.getY(), vz = v.getZ();
                if (visited[vx][vy][vz] == tag)
                    continue;
                visited[vx][vy][vz] = tag;

                /* accumulate contribution on this voxel */
                ColorRGB add = v.getColor().mul(v.getIntensity());
                ColorRGB acc = outC[vx][vy][vz];
                outC[vx][vy][vz] = (acc == null) ? add : acc.add(add);

                /* explore neighbours */
                int ruleCnt = 0, childCnt = 0;
                for (Voxel n : v.getNeighbors(blocks)) {

                    ObjectInstance nb = n.getObjectInstance();

                    if (nb != null && nb.getOpacity() == 1 &&
                            !nb.isLightAllowed(Face.LEFT.index) &&
                            !nb.isLightAllowed(Face.RIGHT.index) &&
                            !nb.isLightAllowed(Face.TOP.index)) {

                        newRule[ruleCnt++] = OPPOSITE_IDX[n.getOriginDirIdx()];

                    } else if (nb != null && nb.getOpacity() == 1 &&
                            !nb.isLightAllowed(n.getOriginAxisFace().index)) {

                        newRule[ruleCnt++] = OPPOSITE_IDX[n.getOriginDirIdx()];
                        n.kill();
                        children[childCnt++] = n;

                    } else if (nb != null && nb.getOpacity() == 1) {

                        newRule[ruleCnt++] = OPPOSITE_IDX[n.getOriginDirIdx()];

                    } else if (n.getIntensity() >= EPS) {

                        children[childCnt++] = n;
                    }
                }

                /* propagate rules to children */
                for (int i = 0; i < childCnt; ++i) {
                    Voxel c = children[i];
                    for (int r = 0; r < ruleCnt; ++r)
                        c.addForbiddenDir(OPPOSITE_IDX[newRule[r]]);
                    q.addLast(c);
                }
            }
        }

        /* ================================================================= */
        /* 3) project accumulated light onto visible faces */
        /* ================================================================= */
        for (int x = 0; x < X; ++x)
            for (int y = 0; y < Y; ++y)
                for (int z = 0; z < Z; ++z) {

                    if (blocks[x][y][z] == null) {
                        ColorRGB c = outC[x][y][z];
                        if (!isBlack(c)) {
                            out[x][y][z].accumulate(Face.RIGHT, c.clamp());
                            out[x][y][z].accumulate(Face.LEFT, c.clamp());
                            out[x][y][z].accumulate(Face.TOP, c.clamp());
                        }
                    } else {
                        if (x + 1 < X && !isBlack(outC[x + 1][y][z]))
                            out[x][y][z].accumulate(Face.RIGHT, outC[x + 1][y][z].clamp());

                        if (y + 1 < Y && !isBlack(outC[x][y + 1][z]))
                            out[x][y][z].accumulate(Face.LEFT, outC[x][y + 1][z].clamp());

                        if (z + 1 < Z && !isBlack(outC[x][y][z + 1]))
                            out[x][y][z].accumulate(Face.TOP, outC[x][y][z + 1].clamp());
                    }
                }

        return out;
    }

    private ColorRGB addIfLightSource(ColorRGB ambient, ArrayDeque<Voxel> sources, long tick, ObjectInstance objectInstance, int x, int y, int z) {
        if (objectInstance == null)
            return ambient;

        PropertyLight lp = (PropertyLight) objectInstance.getProperty(PropertyLight.NAME);
        if (lp == null)
            return ambient;

        LightSource ls = lp.getLight();
        ColorRGB col = ls.color(tick);
        double I = ls.oscillatingIntensity();
        double f = ls.falloff();

        if (f >= 0.999) { // MASTER-LIGHT
            ambient = ambient.add(col.mul(I)); // accumulate once
        } else { // normal point light
            sources.addLast(new Voxel(
                    objectInstance, x, y, z,
                    col, I, f));
        }
        return ambient;
    }

}
