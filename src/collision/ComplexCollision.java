package collision;

import java.util.ArrayList;

public class ComplexCollision extends Collision {
    private ArrayList<BoundingCollision> collisions = new ArrayList<>();

    public ArrayList<BoundingCollision> getCollisions() {
        return collisions;
    }

    @Override
    public boolean collision(double x1, double y1, double z1, Collision collision, double x2, double y2, double z2) {
        for (BoundingCollision collision1 : collisions) {
            if (collision1.collision(x1, y1, z1, collision, x2, y2, z2))
                return true;
        }
        return false;
    }

    @Override
    public boolean collision(double x1, double y1, double z1, ComplexCollision collision, double x2, double y2, double z2) {
        for (BoundingCollision collision1 : collisions) {
            for (BoundingCollision collision2 : collision.getCollisions())
                if (collision1.collision(x1, y1, z1, collision2, x2, y2, z2))
                    return true;
        }
        return false;
    }
    
}
