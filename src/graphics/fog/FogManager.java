/*  graphics/fog/FogManager.java  */
package graphics.fog;

import graphics.Texture;
import objects.block.Block;
import objects.block.BlockType;
import tools.Vector;

import java.util.HashMap;
import java.util.Map;

public final class FogManager {

    /* ------------------------------------------------------------------ */
    /* texture variants */
    /* ------------------------------------------------------------------ */
    private static final BlockType[] FOG_TYPES = {
            /* 0 */ new BlockType("fog100", new Texture[] {
                    Texture.createBasicTexture(
                            new String[] { "fog/100/fog-0.png", "fog/100/fog-1.png",
                                    "fog/100/fog-2.png", "fog/100/fog-3.png" },
                            3) },
                    0, null, null, null),
            /* 1 */ new BlockType("fog75", new Texture[] {
                    Texture.createBasicTexture(
                            new String[] { "fog/75/fog-1.png", "fog/75/fog-2.png",
                                    "fog/75/fog-3.png", "fog/75/fog-0.png" },
                            3) },
                    0, null, null, null),
            /* 2 */ new BlockType("fog50", new Texture[] {
                    Texture.createBasicTexture(
                            new String[] { "fog/50/fog-2.png", "fog/50/fog-1.png",
                                    "fog/50/fog-0.png", "fog/50/fog-3.png" },
                            3) },
                    0, null, null, null),
            /* 3 */ new BlockType("fog25", new Texture[] {
                    Texture.createBasicTexture(
                            new String[] { "fog/25/fog-0.png", "fog/25/fog-1.png",
                                    "fog/25/fog-2.png", "fog/25/fog-3.png" },
                            3) },
                    0, null, null, null),
            /* 4 */ new BlockType("fog10", new Texture[] {
                    Texture.createBasicTexture(
                            new String[] { "fog/10/fog-3.png", "fog/10/fog-2.png",
                                    "fog/10/fog-1.png", "fog/10/fog-0.png" },
                            3) },
                    0, null, null, null)
    };

    /* convenient handles */
    private static final Block FOG100 = FOG_TYPES[0].getInstance();
    private static final Block FOG75 = FOG_TYPES[1].getInstance();
    private static final Block FOG50 = FOG_TYPES[2].getInstance();
    private static final Block FOG25 = FOG_TYPES[3].getInstance();
    private static final Block FOG10 = FOG_TYPES[4].getInstance();

    /* ------------------------------------------------------------------ */
    public Map<Vector, Block> getFogMap(Block[][][] blocks) {

        Map<Vector, Block> fog = new HashMap<>();
        if (blocks == null || blocks.length == 0)
            return fog;

        int dimX = blocks.length;
        int dimY = blocks[0].length;
        int dimZ = blocks[0][0].length;

        for (int z = 0; z < dimZ; z++) {

            int aboveZ = z + 1;
            int topZ = z + 2;
            double aboveZc = aboveZ + 0.5;
            double topZc = topZ + 0.5;

            /* west, north, east, south borders */
            for (int y = 0; y < dimY; y++)
                maybeAddGroup(blocks, fog, 0, y, z, -1, 0,
                        aboveZc, topZc, dimZ, dimX, dimY);

            for (int x = 0; x < dimX; x++)
                maybeAddGroup(blocks, fog, x, 0, z, 0, -1,
                        aboveZc, topZc, dimZ, dimX, dimY);

            for (int y = 0; y < dimY; y++)
                maybeAddGroup(blocks, fog, dimX - 1, y, z, 1, 0,
                        aboveZc, topZc, dimZ, dimX, dimY);

            for (int x = 0; x < dimX; x++)
                maybeAddGroup(blocks, fog, x, dimY - 1, z, 0, 1,
                        aboveZc, topZc, dimZ, dimX, dimY);
        }
        return fog;
    }

    /* ------------------------------------------------------------------ */
    private static void maybeAddGroup(Block[][][] blocks,
            Map<Vector, Block> fog,
            int x, int y, int z,
            int ox, int oy, // outward dir
            double aboveZc, double topZc,
            int dimZ, int dimX, int dimY) {

        /* anchor must be solid */
        if (blocks[x][y][z] == null)
            return;

        int aboveZ = z + 1;
        if (aboveZ >= dimZ)
            return;

        if (blocks[x][y][aboveZ] != null)
            return; // space already occupied

        /* jitter only outward ------------------------------------------------ */
        double rand = Math.random();
        double jx = (ox != 0) ? rand : 0.0;
        double jy = (oy != 0) ? rand : 0.0;
        double jz = rand / 5 - 0.1;

        fog.put(new Vector(x + 0.5 + ox + jx,
                y + 0.5 + oy + jy,
                aboveZc), FOG100);
        fog.put(new Vector(x + 0.5 + jx,
                y + 0.5 + jy,
                aboveZc), FOG75);
        fog.put(new Vector(x + 0.5 - ox * 0.5 + jx,
                y + 0.5 - oy * 0.5 + jy,
                aboveZc), FOG50);
        fog.put(new Vector(x + 0.5 - ox + jx,
                y + 0.5 - oy + jy,
                aboveZc), FOG25);
        fog.put(new Vector(x + 0.5 - ox * 1.25 + jx,
                y + 0.5 - oy * 1.25 + jy,
                aboveZc), FOG10);

        fog.put(new Vector(x + 0.5 + jx,
                y + 0.5 + jy,
                aboveZc + 0.5 + jz), FOG25);
        fog.put(new Vector(x + 0.5 + ox + jx,
                y + 0.5 + oy + jy,
                aboveZc + 0.5 + jz), FOG50);
    }
}
