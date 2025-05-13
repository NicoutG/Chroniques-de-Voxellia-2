/*  graphics/shape/Face.java  */
package graphics.shape;

/** Logical ordering shared by every isometric shape. */
public enum Face {
    LEFT (0), TOP (1), RIGHT (2);

    public final int index;
    Face(int idx) { this.index = idx; }

    public static Face fromDir(int dx,int dy,int dz){
        if (dx== 1) return LEFT;
        if (dx==-1) return RIGHT;
        if (dy== 1) return TOP;
        // faces non visibles dans iso -> optionnel
        return LEFT;
    }
}
