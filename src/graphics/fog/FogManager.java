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
                                        null, null, null) {{
                                                setOpacity(0);
                                        }},
                        /* 1 */ new BlockType("fog75", new Texture[] {
                                        Texture.createBasicTexture(
                                                        new String[] { "fog/75/fog-2.png", "fog/75/fog-1.png",
                                                                        "fog/75/fog-3.png", "fog/75/fog-0.png" },
                                                        3) },
                                        null, null, null) {{
                                                setOpacity(0);
                                        }},
                        /* 2 */ new BlockType("fog50", new Texture[] {
                                        Texture.createBasicTexture(
                                                        new String[] { "fog/50/fog-0.png", "fog/50/fog-1.png",
                                                                        "fog/50/fog-2.png", "fog/50/fog-3.png" },
                                                        3) },
                                        null, null, null) {{
                                                setOpacity(0);
                                        }},
                        /* 3 */ new BlockType("fog25", new Texture[] {
                                        Texture.createBasicTexture(
                                                        new String[] { "fog/25/fog-1.png", "fog/25/fog-2.png",
                                                                        "fog/25/fog-3.png", "fog/25/fog-0.png" },
                                                        3) },
                                        null, null, null) {{
                                                setOpacity(0);
                                        }},
                        /* 4 */ new BlockType("fog10", new Texture[] {
                                        Texture.createBasicTexture(
                                                        new String[] { "fog/10/fog-0.png", "fog/10/fog-1.png",
                                                                        "fog/10/fog-2.png", "fog/10/fog-3.png" },
                                                        3) },
                                        null, null, null) {{
                                                setOpacity(0);
                                        }},
        };

        /* convenient handles */
        private static final Block FOG100 = FOG_TYPES[0].getInstance();
        private static final Block FOG75 = FOG_TYPES[1].getInstance();
        private static final Block FOG50 = FOG_TYPES[2].getInstance();
        private static final Block FOG25 = FOG_TYPES[3].getInstance();
        private static final Block FOG10 = FOG_TYPES[4].getInstance();

        /* -------------------------------------------------------------- */
        /* Utilitaires internes */
        /* -------------------------------------------------------------- */

        private static boolean isSolid(Block b) {
                return b != null;
        }

        /** Indique si la cellule a au moins un voisin horizontal solide (même z) */
        private static boolean hasHorizontalSolidNeighbor(Block[][][] blocks,
                        int x, int y, int z,
                        int dimX, int dimY, boolean checkCorners) {

                int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
                if (checkCorners) {
                        dirs = new int[][] {
                                        { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
                                        { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 }
                        };
                }
                for (int[] d : dirs) {
                        int nx = x + d[0];
                        int ny = y + d[1];
                        if (nx < 0 || nx >= dimX || ny < 0 || ny >= dimY)
                                continue;
                        if (isSolid(blocks[nx][ny][z]))
                                return true;
                }
                return false;
        }

        private static boolean hasHorizontalNeighborsX(Block[][][] blocks,
                        int x, int y, int z,
                        int dimX, int dimY) {
                int[][] dirs = { { 1, 0 }, { -1, 0 } };
                for (int[] d : dirs) {
                        int nx = x + d[0];
                        int ny = y + d[1];
                        if (nx < 0 || nx >= dimX || ny < 0 || ny >= dimY)
                                continue;
                        if (isSolid(blocks[nx][ny][z]))
                                return true;
                }
                return false;
        }

        private static boolean hasHorizontalNeighborsY(Block[][][] blocks,
                        int x, int y, int z,
                        int dimX, int dimY) {
                int[][] dirs = { { 0, 1 }, { 0, -1 } };
                for (int[] d : dirs) {
                        int nx = x + d[0];
                        int ny = y + d[1];
                        if (nx < 0 || nx >= dimX || ny < 0 || ny >= dimY)
                                continue;
                        if (isSolid(blocks[nx][ny][z]))
                                return true;
                }
                return false;
        }

        /* -------------------------------------------------------------- */
        /* Méthode publique */
        /* -------------------------------------------------------------- */

        public Map<Vector, Block> getFogMap(Block[][][] blocks) {
                Map<Vector, Block> fog = new HashMap<>();
                if (blocks == null || blocks.length == 0)
                        return fog;

                final int dimX = blocks.length;
                final int dimY = blocks[0].length;
                final int dimZ = blocks[0][0].length;

                /* ---------------------------------------------------------- */
                /* Parcours de TOUTES les cellules nulles */
                /* ---------------------------------------------------------- */
                for (int x = 0; x < dimX; x++) {
                        for (int y = 0; y < dimY; y++) {
                                for (int z = 0; z < dimZ; z++) {

                                        if(isSolid(blocks[x][y][z])) continue;

                                        final boolean isBottomFace = (z == 0);
                                        final boolean isTopFace = (z == dimZ - 1);

                                        final boolean hasSolidAbove = (z + 1 < dimZ) && isSolid(blocks[x][y][z + 1]);
                                        final boolean hasSolidBelow = (z - 1 >= 0) && isSolid(blocks[x][y][z - 1]);

                                        final boolean borderX = (x == 0 || x == dimX - 1);
                                        final boolean borderY = (y == 0 || y == dimY - 1);

                                        final boolean isBorder = (
                                                (borderX || borderY) && 
                                                (
                                                        (hasSolidAbove || hasSolidBelow) ||
                                                        (
                                                                (hasHorizontalNeighborsX(blocks, x, y, z, dimX, dimY) && borderY) ||
                                                                (hasHorizontalNeighborsY(blocks, x, y, z, dimX, dimY) && borderX)
                                                        )
                                                )
                                        );

                                        final boolean isFace = (isBottomFace || isTopFace) &&
                                                        hasHorizontalSolidNeighbor(blocks, x, y, z, dimX, dimY, true);

                                        if (!(isBorder || isFace))
                                                continue; // pas de brume

                                        if (isBorder) {
                                                /* -------------------------------------------------- */
                                                /* Traitement des bords */
                                                /* -------------------------------------------------- */
                                                int ox = 0, oy = 0;
                                                if (x == 0)
                                                        ox = 1;
                                                else if (x == dimX - 1)
                                                        ox = -1;
                                                if (y == 0)
                                                        oy = 1;
                                                else if (y == dimY - 1)
                                                        oy = -1;

                                                boolean spill = false;
                                                int ix = x + ox;
                                                int iy = y + oy;
                                                if (ix >= 0 && ix < dimX && iy >= 0 && iy < dimY) {
                                                        spill = !isSolid(blocks[ix][iy][z]);
                                                }
                                                addBorderFog(fog, x, y, z, ox, oy, spill);

                                                /* ── renforcement de la brume dans les coins ─────────────────────── */
                                                if (borderX && borderY) {
                                                        addCornerFog(fog, x, y, z, ox, oy);
                                                }
                                        } else if (isFace) {
                                                /* -------------------------------------------------- */
                                                /* Traitement des faces haut/bas */
                                                /* -------------------------------------------------- */
                                                if (isBottomFace)
                                                        processHorizontalFaceCell(blocks, fog, x, y, z, dimX, dimY, dimZ,
                                                                isBottomFace);
                                        }
                                }
                        }
                }

                return fog;
        }

        /* ------------------------------------------------------------------ */
        /* Ajout de fog pour une cellule de bordure */
        /* ------------------------------------------------------------------ */
        private static void addBorderFog(Map<Vector, Block> fog,
                        int x, int y, int z,
                        int ox, int oy, boolean spillInterior) {

                double cx = x + 0.5;
                double cy = y + 0.5;
                double cz = z + 0.5;

                // petit jitter commun pour ne jamais avoir deux identiques
                double rand = Math.random() / 1.5 - 0.4;
                double jx = (ox != 0 ? rand * ox : 0.0);
                double jy = (oy != 0 ? rand * oy : 0.0);

                // fog.put(new Vector(cx + jx, cy + jy, cz), FOG10);
                // fog.put(new Vector(cx + jx - ox * 0.25, cy + jy - oy * 0.25, cz), FOG25);
                fog.put(new Vector(cx + jx - ox * 0.75, cy + jy - oy * 0.7, cz), FOG50);
                // fog.put(new Vector(cx + jx - ox * 0.75, cy + jy - oy * 0.75, cz), FOG75);
                fog.put(new Vector(cx + jx - ox, cy + jy - oy, cz), FOG75);

                // fog.put(new Vector(cx + jx - ox, cy + jy - oy, cz + 0.5 + jz), FOG75);
        }

        /* ------------------------------------------------------------------ */
        /* Renforcement de la brume sur les coins (plus sombre) */
        /* ------------------------------------------------------------------ */
        private static void addCornerFog(Map<Vector, Block> fog,
                        int x, int y, int z,
                        int ix, int iy) { // ix/iy pointent vers l’intérieur

                double cx = x + 0.5;
                double cy = y + 0.5;
                double cz = z + 0.5;

                /* Couche très opaque juste à l’extérieur du coin */
                // fog.put(new Vector(cx - ix, cy - iy, cz), FOG100);
                fog.put(new Vector(cx - ix * 0.75, cy, cz), FOG75); // vers le bord X
                fog.put(new Vector(cx, cy - iy * 0.75, cz), FOG75); // vers le bord Y

                // fog.put(new Vector(cx - ix * 0.75, cy - iy * 0.75, cz + 0.25), FOG75);
                // fog.put(new Vector(cx - ix * 0.5, cy - iy * 0.5, cz + 0.5), fog25or50());
                // fog.put(new Vector(cx - ix * 0.25, cy - iy * 0.25, cz + 0.5), fog10or25());
        }

        /* ------------------------------------------------------------------ */
        /* Ajout de fog pour une cellule sur la face z==0 ou z==max */
        /* ------------------------------------------------------------------ */
        private static void processHorizontalFaceCell(Block[][][] blocks, Map<Vector, Block> fog,
                        int x, int y, int z,
                        int dimX, int dimY, int dimZ, boolean isBottomFace) {

                double dirZ = isBottomFace ? 1 : -1;
                double adj = isBottomFace ? 0.25 : -0.25;

                double cx = x + 0.5;
                double cy = y + 0.5;
                double cz = z + 0.5;

                double rand = Math.random();
                double jx = rand / 4 - 0.125;
                double jy = rand / 4 - 0.125;

                // Couche principale
                fog.put(new Vector(cx + jx, cy + jy, cz - dirZ + adj), FOG75);
                fog.put(new Vector(cx + jx, cy + jy, cz - dirZ * 0.75 + adj), FOG50);
                // fog.put(new Vector(cx + jx, cy + jy, cz - dirZ * 0.5 + adj), FOG50);
                // fog.put(new Vector(cx + jx, cy + jy, cz - dirZ * 0.25 + adj), FOG25);
                // fog.put(new Vector(cx + jx, cy + jy, cz + adj), FOG10);

        }

        /* ------------------------------------------------------------------ */
        /* Animation légère : identique à l'ancienne implémentation */
        /* ------------------------------------------------------------------ */
        public Map<Vector, Block> randomizeFogMap(Map<Vector, Block> currentFogMap) {
                Map<Vector, Block> randomizedFog = new HashMap<>();
                for (Map.Entry<Vector, Block> entry : currentFogMap.entrySet()) {
                        Vector v = entry.getKey();
                        Block b = entry.getValue();
                        double dx = (Math.random() - 0.5) * 0.05;
                        double dy = (Math.random() - 0.5) * 0.05;
                        double dz = (Math.random() - 0.5) * 0.05;
                        Vector newV = new Vector(v.x + dx, v.y + dy, v.z + dz);
                        // Swap occasionnel entre FOG25 et FOG50
                        if (b == FOG25 || b == FOG50) {
                                if (Math.random() < 0.05) {
                                        b = (b == FOG25 ? FOG50 : FOG25);
                                }
                        }
                        randomizedFog.put(newV, b);
                }
                return randomizedFog;
        }
}
