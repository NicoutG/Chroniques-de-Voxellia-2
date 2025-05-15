/*  graphics/light/Voxel.java  */
package graphics.ligth; // same package name

import java.util.ArrayList;
import java.util.List;

import objects.block.Block;

public final class Voxel {

    /*
     * Six axial directions — do NOT change their order, the bit-mask relies on it.
     */
    private static final int[][] DIR = {
            { +1, 0, 0 }, { -1, 0, 0 },
            { 0, +1, 0 }, { 0, -1, 0 },
            { 0, 0, +1 }, { 0, 0, -1 }
    };

    /* ──────────────────── helpers ──────────────────── */

    /** Index (0-5) ↔ opposite index (XOR 1). */
    private static byte opposite(byte i) {
        return (byte) (i ^ 1);
    }

    /** Maps an (x,y,z) vector to its index in {@link #DIR}. */
    private static byte dirIndex(int[] d) {
        return (byte) ((d[0] == 1) ? 0
                : (d[0] == -1) ? 1
                        : (d[1] == 1) ? 2
                                : (d[1] == -1) ? 3
                                        : (d[2] == 1) ? 4 : 5 // the only remaining case
        );
    }

    /* ──────────────────── state ──────────────────── */

    private final int x, y, z;
    private final Block block;
    private final ColorRGB color;
    private double intensity;
    private final double fallOff;

    /** -1 ⇒ source voxel (no parent) ; 0-5 ⇒ index into {@link #DIR}. */
    private final byte originDirIdx;

    /** 6-bit mask, one bit per entry in {@link #DIR}. */
    private long forbiddenMask;

    /* ──────────────────── constructors ──────────────────── */

    /** Internal ctor used when we already have an encoded rule-set. */
    private Voxel(Block b, int x, int y, int z,
            ColorRGB col, double I, double f,
            byte originIdx, long inheritedMask) {

        block = b;
        this.x = x;
        this.y = y;
        this.z = z;
        color = col;
        intensity = I;
        fallOff = f;
        originDirIdx = originIdx;
        forbiddenMask = inheritedMask;

        /* First step after leaving the parent voxel: forbid an immediate U-turn. */
        if (originIdx != -1) {
            forbiddenMask |= 1L << opposite(originIdx);
            attenuateThroughBlock();
        }
    }

    /** Public ctor used by light-source voxels (no rules, no origin). */
    public Voxel(Block b, int x, int y, int z,
            ColorRGB col, double intensity, double fallOff) {
        this(b, x, y, z, col, intensity, fallOff, (byte) -1, 0L);
    }

    /* ──────────────────── core logic ──────────────────── */

    private void attenuateThroughBlock() {
        intensity *= fallOff;
        if (block != null) {
            double o = block.getOpacity();
            if (o > 0 && o < 1)
                intensity *= 1.0 - o;
        }
    }

    public List<Voxel> getNeighbors(Block[][][] grid) {

        final int maxX = grid.length,
                maxY = grid[0].length,
                maxZ = grid[0][0].length;

        List<Voxel> out = new ArrayList<>(6); // fixed upper bound

        /* Unrolled loop => no iterator object, predictable branching */
        for (byte idx = 0; idx < 6; ++idx) {

            int[] d = DIR[idx];
            int nx = x + d[0], ny = y + d[1], nz = z + d[2];

            if (nx < 0 || nx >= maxX ||
                    ny < 0 || ny >= maxY ||
                    nz < 0 || nz >= maxZ)
                continue;

            double factor = ((forbiddenMask & (1L << idx)) != 0) ? 0.90 : 1.0;

            Block nb = grid[nx][ny][nz];
            ColorRGB newColor = color;

            if (nb != null && nb.getOpacity() > 0 && nb.getOpacity() < 1 && nb.getColor() != null) {
                newColor = nb.getColor().add(color).mul(0.5);
            }

            out.add(new Voxel(
                    nb, nx, ny, nz,
                    newColor,
                    intensity * factor,
                    fallOff,
                    idx, // new voxel knows where it came from
                    forbiddenMask)); // …and inherits the whole mask
        }
        return out;
    }

    /* ──────────────────── API identical to original ──────────────────── */

    public Block getBlock() {
        return block;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public double getIntensity() {
        return intensity;
    }

    public ColorRGB getColor() {
        return color;
    }

    /** Null for sources, otherwise the shared static vector from {@link #DIR}. */
    public int[] getOriginDir() {
        return (originDirIdx == -1) ? null : DIR[originDirIdx];
    }

    /** Adds a new rule in O(1). */
    public void addForbiddenDir(int[] d) {
        forbiddenMask |= 1L << dirIndex(d);
    }

    /** Cheap O(1) version used by the new LightingEngine: takes a 0-5 index. */
    public void addForbiddenDir(byte dirIdx) {
        forbiddenMask |= 1L << dirIdx;
    }

    /** Convenience getter for the index form of the origin direction. */
    public byte getOriginDirIdx() {
        return originDirIdx;
    }

}
