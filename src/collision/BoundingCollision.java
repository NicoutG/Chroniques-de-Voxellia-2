package collision;

public class BoundingCollision extends Collision {
    public double xMin;
    public double xMax;
    public double yMin;
    public double yMax;
    public double zMin;
    public double zMax;

    @Override
    public double[] getBounds(double x, double y, double z) {
        return new double[] {x + xMin, x+xMax, y+yMin, y+yMax, z+zMin, z+zMax};
    }
}
