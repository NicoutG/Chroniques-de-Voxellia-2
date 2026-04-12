package tools.PetriNet;

import objects.entity.Entity;
import objects.entity.entityBehavior.EntityBehaviorConstantVelocity;
import tools.Vector;
import tools.PetriNet.States.PetriNetState;
import world.World;

public class PetriNetShoot extends PetriNetState {


    public PetriNetShoot() {
        super();
    }

    public static int getDirectedShootTexture(Vector direction, int leftTexture, int rightTexture, int topTexture, int bottomTexture) {
        if (Math.abs(direction.y) < Math.abs(direction.x)) {
            if (direction.x > 0)
                return rightTexture;
            else
                return leftTexture;
        }
        else {
            if (direction.y > 0)
                return bottomTexture;
            else
                return topTexture;
        }
    }

    public static void shoot(World world, Entity entity, Vector direction, String projectileName, double projectileSpeed) {
        Entity projectile = world.getEntity(projectileName);
        projectile.setPosition(entity.getPosition().x + direction.x, entity.getPosition().y + direction.y, entity.getPosition().z + direction.z);
        projectile.setState(EntityBehaviorConstantVelocity.VELOCITY, new double[] {direction.x * projectileSpeed, direction.y * projectileSpeed, direction.z * projectileSpeed});
        world.addEntity(projectile);
    }
}
