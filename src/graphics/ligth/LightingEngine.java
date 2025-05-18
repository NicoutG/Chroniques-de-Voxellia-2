/*  graphics/light/LightingEngine.java  */
package graphics.ligth;

import java.util.ArrayDeque;
import java.util.List;
import objects.block.Block;
import objects.property.PropertyLight;
import graphics.shape.Face;

public final class LightingEngine {

    private static final double EPS = 0.01; // perceptible limit
    private static final int MAX_NEIGHBOURS = 6; // axis-aligned

    /* ---------- small helpers ---------- */

    /** Pre-built array of opposite directions, index-aligned with Voxel.DIR. */
    private static final byte[] OPPOSITE_IDX = { 1, 0, 3, 2, 5, 4 };

    /** Cheap test that avoids calling ColorRGB#equals on every voxel. */
    private static boolean isBlack(ColorRGB c) {
        return c == null || c.isBlack(); // ‘isBlack()’ should be a simple “== BLACK”
    }

    /* ===================================================================== */
    /* ENGINE */
    /* ===================================================================== */

    public FaceLighting[][][] compute(Block[][][] blocks, long tick) {

        final int X = blocks.length;
        final int Y = blocks[0].length;
        final int Z = blocks[0][0].length;

        /* ---------- output grids ---------- */
        FaceLighting[][][] out = new FaceLighting[X][Y][Z];
        ColorRGB[][][] outCol = new ColorRGB[X][Y][Z];

        for (int x = 0; x < X; ++x)
            for (int y = 0; y < Y; ++y)
                for (int z = 0; z < Z; ++z)
                    out[x][y][z] = new FaceLighting(); // keeps external API intact

        /* ---------- 1) extract every light source ---------- */
        ArrayDeque<Voxel> sources = new ArrayDeque<>(); // we only need FIFO
        for (int x = 0; x < X; ++x)
            for (int y = 0; y < Y; ++y)
                for (int z = 0; z < Z; ++z) {
                    Block b = blocks[x][y][z];
                    if (b == null)
                        continue;

                    PropertyLight lp = (PropertyLight) b.getProperty("light");
                    if (lp == null)
                        continue;

                    LightSource s = lp.getLight();
                    sources.addLast(new Voxel(
                            b, x, y, z,
                            s.color(tick), s.oscillatingIntensity(), s.falloff()));
                }

        /* ---------- scratch buffers reused across all BFS passes ---------- */
        final int[][][] visited = new int[X][Y][Z]; // 0 ⇒ never visited
        int visitTag = 1;

        final ArrayDeque<Voxel> q = new ArrayDeque<>();
        final byte[] newRule = new byte[MAX_NEIGHBOURS]; // indices of forbiddens
        final Voxel[] children = new Voxel[MAX_NEIGHBOURS];

        /* ================================================================= */
        /* 2) propagate **each** source (rules are per-source) */
        /* ================================================================= */
        for (Voxel src : sources) {

            /* 2-a: the source itself is fully lit */
            out[src.getX()][src.getY()][src.getZ()]
                    .inject(src.getColor().mul(src.getIntensity()));

            q.clear();
            q.addLast(src);

            final int myTag = visitTag++;

            while (!q.isEmpty()) {

                Voxel v = q.removeFirst();
                int vx = v.getX(), vy = v.getY(), vz = v.getZ();

                if (visited[vx][vy][vz] == myTag)
                    continue;
                visited[vx][vy][vz] = myTag;

                /* ---- accumulate local contribution ---- */
                ColorRGB add = v.getColor().mul(v.getIntensity());
                ColorRGB acc = outCol[vx][vy][vz];
                outCol[vx][vy][vz] = (acc == null) ? add : acc.add(add);

                /* ---- examine neighbours ---- */
                int ruleCnt = 0, childCnt = 0;

                List<Voxel> neigh = v.getNeighbors(blocks);
                for (Voxel n : neigh) {

                    Block nb = n.getBlock();

                    if (nb != null && nb.getOpacity() == 1  && !nb.isLightAllowed(Face.LEFT.index) && !nb.isLightAllowed(Face.RIGHT.index) && !nb.isLightAllowed(Face.TOP.index)) {
                        newRule[ruleCnt++] = OPPOSITE_IDX[n.getOriginDirIdx()];
                    }

                    else if (nb != null && nb.getOpacity() == 1 && !nb.isLightAllowed(n.getOriginAxisFace().index)) {
                        newRule[ruleCnt++] = OPPOSITE_IDX[n.getOriginDirIdx()];
                        n.kill(); 
                        children[childCnt++] = n;
                    }
                    else if (nb != null && nb.getOpacity() == 1) {
                        newRule[ruleCnt++] = OPPOSITE_IDX[n.getOriginDirIdx()];
                    }

                    else if (n.getIntensity() >= EPS) {
                        children[childCnt++] = n;
                    }
                }

                /* ---- propagate freshly found rules to every child ---- */
                for (int i = 0; i < childCnt; ++i) {
                    Voxel c = children[i];
                    for (int r = 0; r < ruleCnt; ++r)
                        c.addForbiddenDir(OPPOSITE_IDX[newRule[r]]);
                    q.addLast(c);
                }
            }
        }

        /* ================================================================= */
        /* 3) project accumulated light onto the three visible faces */
        /* ================================================================= */
        for (int x = 0; x < X; ++x)
            for (int y = 0; y < Y; ++y)
                for (int z = 0; z < Z; ++z) {

                    if (blocks[x][y][z] == null) {
                        ColorRGB c = outCol[x][y][z];
                        if (!isBlack(c))
                            out[x][y][z].accumulate(Face.RIGHT, c.clamp());
                        if (!isBlack(c))
                            out[x][y][z].accumulate(Face.LEFT, c.clamp());
                        if (!isBlack(c))
                            out[x][y][z].accumulate(Face.TOP, c.clamp());

                    } else {

                        /* RIGHT face (+X) */
                        if (x + 1 < X) {
                            ColorRGB c = outCol[x + 1][y][z];
                            if (!isBlack(c))
                                out[x][y][z].accumulate(Face.RIGHT, c.clamp());
                        }
                        /* LEFT face (+Y) */
                        if (y + 1 < Y) {
                            ColorRGB c = outCol[x][y + 1][z];
                            if (!isBlack(c))
                                out[x][y][z].accumulate(Face.LEFT, c.clamp());
                        }
                        /* TOP face (+Z) */
                        if (z + 1 < Z) {
                            ColorRGB c = outCol[x][y][z + 1];
                            if (!isBlack(c))
                                out[x][y][z].accumulate(Face.TOP, c.clamp());
                        }
                    }
                }

        return out; // === identical observable result ===
    }

}
