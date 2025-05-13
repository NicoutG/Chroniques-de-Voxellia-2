package graphics.shape;

public enum Face {
    LEFT (0),   // +Y
    RIGHT(1),   // +X
    TOP  (2);   // +Z

    public final int index;
    Face(int i){ index = i; }

    /** Visible faces only : +X, +Y, +Z. Others return {@code null}. */
    public static Face fromDir(int dx,int dy,int dz){
        if (dx == +1) return RIGHT;   // +X  → right face
        if (dy == +1) return LEFT;    // +Y  → left  face
        if (dz == +1) return TOP;     // +Z  → top   face
        return null;                  // faces we never draw
    }
}
