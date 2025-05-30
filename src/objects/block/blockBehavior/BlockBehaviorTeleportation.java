package objects.block.blockBehavior;

import java.util.ArrayList;
import java.util.Random;

import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorTeleportation extends BlockBehaviorActivable {
    public final static String TELEPORTATION = "teleportation";
    private ArrayList<Entity> teleportedEntities = new ArrayList<>();

    @Override
    protected void activate(World world, Block block, Vector position, int network) {
        block.setIndexTexture(1);
    }

    @Override
    protected void desactivate(World world, Block block, Vector position, int network) {
        teleportedEntities.clear();
        block.setIndexTexture(0);
    }

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(TELEPORTATION, 0);
    }

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        if (getActivationState(block)) {
            for (int i = teleportedEntities.size() - 1; i >= 0; i--) {
                if (!isClose(position, teleportedEntities.get(i)))
                    teleportedEntities.remove(i);
            }
            for (Entity entity : world.getEntities())
                if (isClose(position, entity) && !teleportedEntities.contains(entity))
                    teleportedEntities.add(entity);
        }
    }

    @Override
    public void onEntityClose(World world, Block block, Vector position, Entity entity) {
        if (getActivationState(block)) {
            if (isClose(position, entity) && !teleportedEntities.contains(entity)) {
                ArrayList<Vector> teleporters = getTeleporters(world, block);
                if (!teleporters.isEmpty()) {
                    Vector posEntity = entity.getPosition().clone();
                    Random random = new Random();
                    boolean found = false;
                    do {
                        int index = random.nextInt(teleporters.size());
                        Vector positionEntity = findTeleportationPosition(world, teleporters.get(index), entity);
                        if (positionEntity != null)
                            found = true;
                        else
                            teleporters.remove(index);
                    }while(!found && !teleporters.isEmpty());
                    if (!found)
                        entity.setPosition(posEntity.x, posEntity.y, posEntity.z);
                }
            }
        }
    }

    @Override
    public BlockBehavior clone() {
        BlockBehaviorTeleportation behavior = (BlockBehaviorTeleportation)super.clone();
        behavior.teleportedEntities = (ArrayList)teleportedEntities.clone();
        return behavior;
    }

    public int getTeleportation(Block block) {
        Object state = block.getState(TELEPORTATION);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }

    private boolean isClose(Vector position, Entity entity) {
        int x = (int)position.x;
        int xEntity = (int)entity.getX();
        int difX = Math.abs(x - xEntity);
        if (difX > 1)
            return false;
        int y = (int)position.y;
        int yEntity = (int)entity.getY();
        int difY = Math.abs(y - yEntity);
        if (difY > 1)
            return false;
        int z = (int)position.z;
        int zEntity = (int)entity.getZ();
        int difZ = Math.abs(z - zEntity);
        if (difZ > 1)
            return false;
        if (difX + difY + difZ == 1)
            return true;
        return false;
    }

    private ArrayList<Vector> getTeleporters(World world, Block block) {
        Block[][][] blocks = world.getBlocks();
        int teleportation = getTeleportation(block);
        ArrayList<Vector> teleporters = new ArrayList<>();
        for (int x = 0; x < blocks.length; x++)
            for (int y = 0; y < blocks[x].length; y++)
                for (int z = 0; z < blocks[x][y].length; z++) {
                    Block otherBlock = blocks[x][y][z];
                    if (otherBlock != null && otherBlock != block && block.areSameType(otherBlock) && teleportation == getTeleportation(otherBlock) && getActivationState(otherBlock))
                        teleporters.add(new Vector(x + 0.5, y + 0.5, z + 0.5));
                }
        return teleporters;
    }

    private Vector findTeleportationPosition(World world, Vector teleporterPosition, Entity entity) {
        double xTel = teleporterPosition.x;
        double yTel = teleporterPosition.y;
        double zTel = teleporterPosition.z;
        double[][] positions = new double[][] {
            {xTel, yTel, zTel + 1}, {xTel, yTel, zTel - 1},
            {xTel, yTel + 1, zTel}, {xTel, yTel - 1, zTel},
            {xTel + 1, yTel, zTel}, {xTel - 1, yTel, zTel},
        };
        int X = world.getX();
        int Y = world.getY();
        int Z = world.getZ();
        for (int i = 0; i < positions.length; i++) {
            double x = positions[i][0];
            double y = positions[i][1];
            double z = positions[i][2];
            if (0 <= x && x < X && 0 <= y && y < Y && 0 <= z && z < Z) {
                entity.setPosition(x, y, z);
                if (!entity.isColliding(world))
                    return new Vector(x ,y ,z);
            }
        }
        return null;
    }
}
