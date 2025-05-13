package block.blockBehavior;

import block.Block;
import entity.Entity;
import model.world.World;
import tools.*;;

public abstract class BlockBehavior {

    public void onAttachToBlock(Block block) {
    }

    public BlockBehavior clone() {
        return this;
    }

    public void onUpdate(World world, Block block) {
    }

    public void onInteraction(World world, Block block, Vector position, Entity entity) {
    }

    public void onPush(World world, Block block, Vector position, int depX, int depY, int depZ) {
    }

    public void onActivated(World world, Block block, Vector position, int network) {
    }

    public void onDesactivated(World world, Block block, Vector position, int network) {
    }

    public void onEntityIn(World world, Block block, Vector position, Entity entity) {
    }

    public void onEntityCollision(World world, Block block, Vector position, Entity entity) {
    }


}
