package objects.block;

import objects.ObjectInstance;
import objects.block.blockBehavior.BlockBehavior;
import objects.entity.Entity;
import tools.*;
import world.World;

public class Block extends ObjectInstance<BlockType, Block, BlockBehavior>{

    public Block(BlockType blockType) {
        super(blockType);
    }

    //#region behavior events

    public void onStart(World world, Vector position) {
        executeEvent(b -> b.onStart(world,this,position));
    }

    public void onUpdate(World world,Vector position) {
        executeEvent(b -> b.onUpdate(world,this,position));
    }

    public void onInteraction(World world, Vector position, Entity entity) {
        executeEvent(b -> b.onInteraction(world,this,position,entity));
    }

    public void onActivated(World world, Vector position, int network) {
        executeEvent(b -> b.onActivated(world,this,position,network));
    }

    public void onDesactivated(World world, Vector position, int network) {
        executeEvent(b -> b.onDesactivated(world,this,position,network));
    }

    public void onEntityClose(World world, Vector position, Entity entity) {
        executeEvent(b -> b.onEntityClose(world,this,position,entity));
    }

    //#endregion
}
