package objects.collision;

import tools.*;;

public class Collision {

    public double[] getBounds(Vector position) {
        return new double[] {position.x - 0.5, position.x + 0.5, position.y - 0.5, position.y + 0.5, position.z - 0.5, position.z + 0.5};
    }

    public double getPourcentage2In1(Vector position1, Collision collision, Vector position2) {
        double[] bounds1 = getBounds(position1);
        double[] bounds2 = collision.getBounds(position2);
        double xOverlap = Math.max(0, Math.min(bounds1[1], bounds2[1]) - Math.max(bounds1[0], bounds2[0]));
        double yOverlap = Math.max(0, Math.min(bounds1[3], bounds2[3]) - Math.max(bounds1[2], bounds2[2]));
        double zOverlap = Math.max(0, Math.min(bounds1[5], bounds2[5]) - Math.max(bounds1[4], bounds2[4]));

        double overlapVolume = xOverlap * yOverlap * zOverlap;

        double bounds2Volume = 
            Math.max(0, bounds2[1] - bounds2[0]) *
            Math.max(0, bounds2[3] - bounds2[2]) *
            Math.max(0, bounds2[5] - bounds2[4]);

        if (bounds2Volume == 0) return 0;

        return overlapVolume / bounds2Volume;
    }

    public boolean collision(Vector position1, Collision collision, Vector position2) {
        if (collision instanceof ComplexCollision)
            return complexCollision(position1, (ComplexCollision)collision, position2);
        double[] bounds1 = getBounds(position1);
        double[] bounds2 = collision.getBounds(position2);
        if (bounds1[0]>=bounds2[1] || bounds1[1]<=bounds2[0] || bounds1[2]>=bounds2[3] || bounds1[3]<=bounds2[2] || bounds1[4]>=bounds2[5] || bounds1[5]<=bounds2[4])
            return false;
        return true;
    }

    public boolean complexCollision(Vector position1, ComplexCollision collision, Vector position2) {
        for (BoundingCollision collision2 : collision.getCollisions()) {
            if (collision(position1, collision2, position2))
                return true;
        }
        return false;
    }
}
