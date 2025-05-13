package objects.entity;

public class Player extends Entity {

    public Player(EntityType type, double x, double y, double z) {
        super(type, x, y, z);
    }

    // Déplacement simple (par pas de 0.1)
    public void move(double dx, double dy, double dz) {
        setPosition(getX() + dx, getY() + dy, getZ() + dz);
    }
}
