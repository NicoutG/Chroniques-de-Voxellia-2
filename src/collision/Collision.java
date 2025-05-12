package collision;

public class Collision {

    public double[] getBounds(double x, double y, double z) {
        return new double[] {x, x+1, y, y+1, z, z+1};
    }

    public boolean collision(double x1, double y1, double z1, Collision collision, double x2, double y2, double z2) {
        double[] bounds1 = getBounds(x1, y1, z1);
        double[] bounds2 = getBounds(x2, y2, z2);
        if (bounds1[0]>=bounds2[1] || bounds1[1]<=bounds2[0] || bounds1[2]>=bounds2[3] || bounds1[3]<=bounds2[2] || bounds1[4]>=bounds2[5] || bounds1[5]<=bounds2[4])
            return false;
        return true;
    }

    public boolean collision(double x1, double y1, double z1, ComplexCollision collision, double x2, double y2, double z2) {
        for (BoundingCollision collision2 : collision.getCollisions()) {
            if (collision(x1, y1, z1, collision2, x2, y2, z2))
                return true;
        }
        return false;
    }
}
