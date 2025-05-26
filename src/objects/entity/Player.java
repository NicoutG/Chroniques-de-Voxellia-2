package objects.entity;

import world.World;

public class Player extends Entity {
    private final static long WAITING_TIME_INTERACT = 500;
    private long lastInteraction = 0;

    public Player(EntityType type, double x, double y, double z) {
        super(type, x, y, z);
    }

    @Override
    public void interact(World world) {
        long now = System.currentTimeMillis();
        if (lastInteraction + WAITING_TIME_INTERACT <= now) {
            lastInteraction = now;
            super.interact(world);
        }
    }
}
