/*  graphics/shape/Face.java  */
package graphics.shape;

/** Logical ordering shared by every isometric shape. */
public enum Face {
    LEFT (0), TOP (1), RIGHT (2);

    public final int index;
    Face(int idx) { this.index = idx; }
}
