package objects.block.blockBehavior;

import objects.block.Block;
import objects.entity.Entity;
import tools.*;
import world.World;

public abstract class BlockBehavior {

    public void onAttachToBlock(Block block) {
    }

    public BlockBehavior clone() {
        return this;
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
