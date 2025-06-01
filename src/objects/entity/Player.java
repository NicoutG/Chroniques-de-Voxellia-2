package objects.entity;

import tools.Vector;
import tools.PathFinding.PathFinding;
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
    protected void interact(World world) {
        long now = System.currentTimeMillis();
        if (lastInteraction + WAITING_TIME_INTERACT <= now) {
            PathFinding.findPath(world, this, position, new Vector(0.5,0.5,8.5));
            lastInteraction = now;
            super.interact(world);
        }
    }

    @Override
    protected boolean jump(World world) {
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
