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
