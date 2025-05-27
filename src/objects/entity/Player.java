package objects.entity;

import world.World;

public class Player extends Entity {
    private final static long WAITING_TIME_INTERACT = 500;
    private long lastInteraction = 0;
    private final static long WAITING_TIME_JUMP = 100;
    private long lastJump = 0;

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

    @Override
    public boolean jump(World world) {
        long now = System.currentTimeMillis();
        if (lastJump + WAITING_TIME_JUMP <= now) {
            boolean hadJump = super.jump(world);
            if (hadJump)
                lastJump = now;
            return hadJump;
        }
        return false;
    }
}
