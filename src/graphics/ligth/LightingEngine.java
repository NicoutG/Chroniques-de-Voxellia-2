package graphics.ligth;

import java.util.ArrayDeque;

import block.Block;
import graphics.shape.Face;

public final class LightingEngine {

    public static FaceLighting[][][] compute(Block[][][] blocks) {

        int X = blocks.length, Y = blocks[0].length, Z = blocks[0][0].length;
        FaceLighting[][][] out = new FaceLighting[X][Y][Z];
        float[][][] best = new float[X][Y][Z];           // intensité max déjà visitée

        // init tableau résultat
        for (int x=0;x<X;++x)
            for (int y=0;y<Y;++y)
                for (int z=0;z<Z;++z)
                    out[x][y][z] = new FaceLighting();

        record Node(int x,int y,int z, ColorRGB col){}

        ArrayDeque<Node> q = new ArrayDeque<>();

        /* 1. pousse les sources */
        for (int x=0;x<X;++x)
            for (int y=0;y<Y;++y)
                for (int z=0;z<Z;++z) {
                    Block b = blocks[x][y][z];
                    if (b == null || b.light()==null) continue;
                    LightSource l = b.light();
                    out[x][y][z].inject(l.color().mul(l.intensity())); // éclaire soi-même
                    best[x][y][z] = l.intensity();
                    q.add(new Node(x,y,z,l.color().mul(l.intensity())));
                }

        /* 2. BFS */
        final int[][] DIRS = {
            {+1,0,0},{-1,0,0},{0,+1,0},{0,-1,0},{0,0,+1},{0,0,-1}
        };

        while (!q.isEmpty()) {
            Node n = q.removeFirst();
            Block from = blocks[n.x][n.y][n.z];
            float falloff = (from.light()!=null)?from.light().falloff():0.8f;

            for (int[] d: DIRS) {
                int nx=n.x+d[0], ny=n.y+d[1], nz=n.z+d[2];
                if (nx<0||ny<0||nz<0||nx>=X||ny>=Y||nz>=Z) continue;
                Block to = blocks[nx][ny][nz];
                if (to==null) continue;

                ColorRGB nextCol = n.col.mul(falloff);

                if (nextCol.r()<0.01 && nextCol.g()<0.01 && nextCol.b()<0.01)
                    continue;

                /* Détermine quelle face du bloc cible on éclaire */
                Face face = Face.fromDir(-d[0], -d[1], -d[2]);
                out[nx][ny][nz].accumulate(face, nextCol);

                /* Stop si le bloc est opaque */
                if (to.isOpaque()) continue;

                /* Propager plus loin ? */
                float nextI = Math.max(nextCol.r(),Math.max(nextCol.g(),nextCol.b()));
                if (nextI > best[nx][ny][nz]) {
                    best[nx][ny][nz] = nextI;
                    q.add(new Node(nx,ny,nz,nextCol));
                }
            }
        }
        return out;
    }
}
