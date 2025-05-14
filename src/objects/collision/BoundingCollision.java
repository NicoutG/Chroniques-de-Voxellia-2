package objects.collision;

import tools.*;;

public class BoundingCollision extends Collision {
    public double xMin;
    public double xMax;
    public double yMin;
    public double yMax;
    public double zMin;
    public double zMax;

    @Override
    public double[] getBounds(Vector position) {
        return new double[] {position.x + xMin, position.x+xMax, position.y+yMin, position.y+yMax, position.z+zMin, position.z+zMax};
    }
}
