package objects.collision;

import tools.*;;

public class BoundingCollision extends Collision {
    public double xMin;
    public double xMax;
    public double yMin;
    public double yMax;
    public double zMin;
    public double zMax;

    public BoundingCollision() {
        this.xMin = 0;
        this.xMax = 0;
        this.yMin = 0;
        this.yMax = 0;
        this.zMin = 0;
        this.zMax = 0;
    }

    public BoundingCollision(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    @Override
    public double[] getBounds(Vector position) {
        return new double[] {position.x + xMin, position.x+xMax, position.y+yMin, position.y+yMax, position.z+zMin, position.z+zMax};
    }
}
