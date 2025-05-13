/*  graphics/ligth/LightingEngine.java  */
package graphics.ligth;

import java.util.ArrayDeque;

import block.Block;
import block.blockProperty.BlockPropertyLight;
import graphics.shape.Face;

/**
 * Simple flood-fill light mapper.
 * <ul>
 * <li>handles coloured lights that blend together (per-channel tracking);</li>
 * <li>lights are injected in the exact source cell, not an offset one;</li>
 * <li>faces +X, +Y, +Z are the only ones shaded because they are the only
 * faces the renderer ever draws in the current isometric view.</li>
 * </ul>
 */
public final class LightingEngine {

    /** 6 ortho-neighbours. */
    private static final int[][] DIR = { { +1, 0, 0 }, { -1, 0, 0 }, { 0, +1, 0 }, { 0, -1, 0 }, { 0, 0, +1 },
            { 0, 0, -1 } };

    /**
     * Computes per-face lighting for every non-null block in the chunk.
     *
     * @param blocks world chunk; axes are [x][y][z].
     * @return a parallel 3-D array (same shape) containing {@link FaceLighting}.
     */
    public FaceLighting[][][] compute(Block[][][] blocks) {

        final int X = blocks.length;
        final int Y = blocks[0].length;
        final int Z = blocks[0][0].length;

        /* ------------------------------------------------------------------ */
        /* result */
        /* ------------------------------------------------------------------ */
        FaceLighting[][][] out = new FaceLighting[X][Y][Z];
        for (int x = 0; x < X; ++x)
            for (int y = 0; y < Y; ++y)
                for (int z = 0; z < Z; ++z)
                    out[x][y][z] = new FaceLighting();

        /* ------------------------------------------------------------------ */
        /* “brightest reached so far” – one value per channel */
        /* ------------------------------------------------------------------ */
        double[][][] bestR = new double[X][Y][Z];
        double[][][] bestG = new double[X][Y][Z];
        double[][][] bestB = new double[X][Y][Z];

        /* ------------------------------------------------------------------ */
        /* BFS working queue */
        /* ------------------------------------------------------------------ */
        record Node(int x, int y, int z, ColorRGB col) {
        }
        ArrayDeque<Node> q = new ArrayDeque<>();

        /*
         * ==================================================================
         * 1) enqueue every emitting block
         * ==================================================================
         */
        for (int x = 0; x < X; ++x)
            for (int y = 0; y < Y; ++y)
                for (int z = 0; z < Z; ++z) {
                    Block b = blocks[x][y][z];
                    if (b == null)
                        continue;
                    BlockPropertyLight lp = (BlockPropertyLight) b.getBlockProperty("light");
                    if (lp == null)
                        continue;

                    LightSource l = lp.getLight();
                    double I = l.oscillatingIntensity();
                    ColorRGB col = l.color().mul(I);

                    /* light its own three visible faces at full power */
                    out[x][y][z].inject(col);

                    /* set “best so far” and enqueue */
                    bestR[x][y][z] = col.r();
                    bestG[x][y][z] = col.g();
                    bestB[x][y][z] = col.b();
                    q.addLast(new Node(x, y, z, col));
                }

        /*
         * ==================================================================
         * 2) flood-fill
         * ==================================================================
         */
        while (!q.isEmpty()) {

            Node n = q.removeFirst();
            Block from = blocks[n.x][n.y][n.z];

            /* falloff depends on the block the light is travelling through */
            double falloff = .9;
            BlockPropertyLight lp = (BlockPropertyLight) from.getBlockProperty("light");
            if (lp != null)
                falloff = lp.getLight().falloff();

            for (int[] d : DIR) {

                /*
                 * ----------------------------------------------------------
                 * 2.1 light the visible face of the *current* block
                 * ----------------------------------------------------------
                 */
                Face f = Face.fromDir(d[0], d[1], d[2]); // +X,+Y,+Z only
                if (f != null)
                    out[n.x][n.y][n.z].accumulate(f, n.col);

                /*
                 * ----------------------------------------------------------
                 * 2.2 propagate to neighbour
                 * ----------------------------------------------------------
                 */
                int nx = n.x + d[0], ny = n.y + d[1], nz = n.z + d[2];
                if (nx < 0 || ny < 0 || nz < 0 ||
                        nx >= X || ny >= Y || nz >= Z)
                    continue;

                Block to = blocks[nx][ny][nz];
                if (to == null)
                    continue;

                ColorRGB col = n.col.mul(falloff);
                if (col.r() < .01 && col.g() < .01 && col.b() < .01)
                    continue;

                /* stop if opaque */
                if (to.getOpacity() == 1)
                    continue;

                /*
                 * per-channel check: only queue if at least one channel
                 * is strictly brighter than what already reached that cell
                 */
                boolean propagate = col.r() > bestR[nx][ny][nz]
                        || col.g() > bestG[nx][ny][nz]
                        || col.b() > bestB[nx][ny][nz];

                if (propagate) {
                    bestR[nx][ny][nz] = Math.max(bestR[nx][ny][nz], col.r());
                    bestG[nx][ny][nz] = Math.max(bestG[nx][ny][nz], col.g());
                    bestB[nx][ny][nz] = Math.max(bestB[nx][ny][nz], col.b());
                    q.addLast(new Node(nx, ny, nz, col));
                }
            }
        }
        return out;
    }
}
