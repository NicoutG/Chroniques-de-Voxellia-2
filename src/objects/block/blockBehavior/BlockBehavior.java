package objects.block.blockBehavior;

import objects.block.Block;
import objects.block.BlockType;
import objects.entity.Entity;
import objects.objectBehavior.ObjectBehavior;
import tools.*;
import world.World;

public abstract class BlockBehavior extends ObjectBehavior<BlockType, Block, BlockBehavior> {

    public void onAttachTo(Block block) {
    }

    public void onStart(World world, Block block, Vector position) {
    }

    public void onUpdate(World world, Block block, Vector position) {
    }

    public void onInteraction(World world, Block block, Vector position, Entity entity) {
    }

    public void onPush(World world, Block block, Vector position, int depX, int depY, int depZ) {
    }

    public void onActivated(World world, Block block, Vector position, int network) {
    }

    public void onDesactivated(World world, Block block, Vector position, int network) {
    }

    public void onEntityClose(World world, Block block, Vector position, Entity entity) {
    }
}
