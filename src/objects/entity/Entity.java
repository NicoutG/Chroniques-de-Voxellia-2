package objects.entity;

import audio.SoundManager;
import audio.SoundType;
import objects.ObjectInstanceMovable;
import objects.block.Block;
import objects.entity.entityBehavior.EntityBehavior;
import tools.*;
import world.World;

public class Entity extends ObjectInstanceMovable<EntityType, Entity, EntityBehavior>{
    private final static long WAITING_TIME_INTERACT = 500;
    private final static long WAITING_TIME_JUMP = 100;
    public final static String SPEED = "speed";

    private long lastInteraction = 0;
    private long lastJump = 0;

    public Entity(EntityType type, double x, double y, double z) {
        super(type,x,y,z);
    }

    public void setSpeed(double speed) {
        setState(SPEED,speed);
    }

    public double getSpeed() {
        Object state = getState(SPEED);
        if (state != null && state instanceof Double)
            return (double)state;
        return -1;
    }

    public void notifyCloseBlocks(World world) {
        int x = (int)position.x;
        int y = (int)position.y;
        int z = (int)position.z;
        Vector pos = new Vector();
        for (int i = x - 1; i <= x + 1; i++) {
            pos.x = i + 0.5;
            for (int j = y - 1; j <= y + 1; j++) {
                pos.y = j + 0.5;
                for (int k = z - 1; k <= z + 1; k++) {
                    pos.z = k + 0.5;
                    Block block = world.getBlock(i, j, k);
                    if (block != null)
                        block.onEntityClose(world, pos, this);
                }
            }
        }
    }

    public void doActions(World world, EntityAction ... entityActions) {
        Vector move = new Vector(0,0,0);
        for (EntityAction action : entityActions) {
            executeEvent(e -> e.onAction(world,this,action));
            switch (action) {
                case LEFT: move.x -= 1;break;
                case RIGHT: move.x += 1;break;
                case TOP: move.y -= 1;break;
                case BOTTOM: move.y += 1;break;
                case ABOVE: move.z += 1;break;
                case BELOW: move.z -= 1;break;
                case JUMP: jump(world);break;
                case INTERACT: interact(world);break;
                default:break;
            }
        }
        double norm = move.getNorm();
        if (norm != 0) {
            double speed = getSpeed();
            double adherency = World.AIR_ADHERENCY;
            if (getFloor() != null)
                adherency = getFloor().getAdherency();
            double velocityX = velocity.x;
            if ((move.x < 0 && velocityX > -speed) || (move.x > 0 && velocityX < speed))
                velocityX += adherency * speed * move.x / norm;
            double velocityY = velocity.y;
            if ((move.y < 0 && velocityY > -speed) || (move.y > 0 && velocityY < speed))
                velocityY += adherency * speed * move.y / norm;
            double velocityZ = velocity.z;
            if ((move.z < 0 && velocityZ > -speed) || (move.z > 0 && velocityZ < speed))
                velocityZ += adherency * speed * move.z / norm;
            setVelocity(velocityX, velocityY, velocityZ);
        }
    }

    protected void interact(World world) {
        long now = System.currentTimeMillis();
        if (lastInteraction + WAITING_TIME_INTERACT <= now) {
            lastInteraction = now;
            // interact with blocks
            int xMin = (int)position.x - 1;
            int yMin = (int)position.y - 1;
            int zMin = (int)position.z - 1;
            Vector positionBlock = new Vector();
            for (int z = zMin; z < zMin + 3; z++) {
                positionBlock.z = z + 0.5;
                for (int y = yMin; y < yMin + 3; y++) {
                    positionBlock.y = y + 0.5;
                    for (int x = xMin; x < xMin + 3; x++) {
                        Block block = world.getBlock(x,y,z);
                        if (block != null) {
                            positionBlock.x = x + 0.5;
                            block.onInteraction(world, positionBlock, this);
                        }
                    }
                }
            }
            // interact with entities
            double radius = 1.5;
            for (Entity entity : world.getEntities())
                if (entity != this)
                    if (Math.abs(getX() - entity.getX()) < radius && Math.abs(getY() - entity.getY()) < radius && Math.abs(getZ() - entity.getZ()) < radius)
                        entity.onInteraction(world, this);
        }
    }

    protected boolean jump(World world) {
        long now = System.currentTimeMillis();
        if (lastJump + WAITING_TIME_JUMP <= now) {
            if (getFloor() != null) {
                addVelocity(0, 0, 1.05);
                lastJump = now;
                SoundManager.playSoundFromCoordinates(SoundType.JUMP2, this.getX(), this.getY(), this.getZ());
                return true;
            }
        }
        return false;
    }

    public void destroyEntity(World world) {
        world.getEntities().remove(this);
    }

    //#region behavior events

    public void onStart(World world) {
        onStart();
        executeEvent(e -> e.onStart(world,this));
    }

    public void onUpdate(World world) {
        move(world, velocity.x, velocity.y, velocity.z);
        heightCheck(world);
        updateFloor(world);
        updateVelocity();
        executeEvent(e -> e.onUpdate(world,this));
        notifyCloseBlocks(world);
    }

    private void updateVelocity() {
        double coef = 1- World.AIR_ADHERENCY;
        if (getFloor() != null)
            coef = 1 - getFloor().getAdherency();
        setVelocity(coef * velocity.x, coef * velocity.y, coef * velocity.z);
        final double epsilon = 0.01;
        if (Math.abs(velocity.x) < epsilon)
            velocity.x = 0;
        if (Math.abs(velocity.y) < epsilon)
            velocity.y = 0;
        if (Math.abs(velocity.z) < epsilon)
            velocity.z = 0;
    }

    public void onInteraction(World world, Entity entityInteract) {
        executeEvent(e -> e.onInteraction(world,this,entityInteract));
    }

    public void onPush(World world, Vector move, Entity entityPush) {
        executeEvent(e -> e.onPush(world,this,move,entityPush));
    }

    public void onActivated(World world, int network) {
        executeEvent(e -> e.onActivated(world,this,network));
    }

    public void onDesactivated(World world, int network) {
        executeEvent(e -> e.onDesactivated(world,this,network));
    }

    public void onEntityCollision(World world, Entity entityCollision) {
        executeEvent(e -> e.onEntityCollision(world,this,entityCollision));
    }

    public void onDeath(World world) {
        executeEvent(e -> e.onDeath(world,this));
    }

    //#endregion

    private void heightCheck(World world) {
        if (position.z < -20) {
            onDeath(world);
            world.executeAfterUpdate(() -> this.destroyEntity(world));
        }
    }
}
