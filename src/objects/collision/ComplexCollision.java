package objects.collision;

import java.util.ArrayList;

import tools.*;;

public class ComplexCollision extends Collision {
    private ArrayList<BoundingCollision> collisions = new ArrayList<>();

    public ComplexCollision() {

    }

    public ComplexCollision(BoundingCollision ... collisions) {
        for (BoundingCollision collision : collisions)
            addCollision(collision);
    }

    public double[] getBounds(Vector position) {
        double[] bounds = new double[] {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        for (BoundingCollision collision : collisions) {
            double[] boundsCol = collision.getBounds(position);
            for (int i = 0; i < 6; i++)
                if (i % 2 == 0)
                    if (boundsCol[i] < bounds[i])
                        bounds[i] = boundsCol[i];
                else
                    if (boundsCol[i] > bounds[i])
                        bounds[i] = boundsCol[i];
        }
        return bounds;
    }

    public ArrayList<BoundingCollision> getCollisions() {
        return collisions;
    }

    public void addCollision(BoundingCollision collision) {
        collisions.add(collision);
    }

    @Override
    public boolean collision(Vector position1, Collision collision, Vector position2) {
        if (collision instanceof ComplexCollision)
            return complexCollision(position1, (ComplexCollision)collision, position2);
        for (BoundingCollision collision1 : collisions) {
            if (collision1.collision(position1, collision, position2))
                return true;
        }
        return false;
    }

    @Override
    public boolean complexCollision(Vector position1, ComplexCollision collision, Vector position2) {
        for (BoundingCollision collision1 : collisions) {
            for (BoundingCollision collision2 : collision.getCollisions())
                if (collision1.collision(position1, collision2, position2))
                    return true;
        }
        return false;
    }
    
}
