package game;

import java.awt.image.BufferedImage;

public class Player extends Entity {

    public Player(BufferedImage texture, double x, double y, double z) {
        super(texture, x, y, z);
    }

    // Déplacement simple (par pas de 0.1)
    public void move(double dx, double dy, double dz) {
        setPosition(getX() + dx, getY() + dy, getZ() + dz);
    }
}
