package objects.block;

import java.util.ArrayList;
import java.util.function.Consumer;

import objects.ObjectInstance;
import objects.block.blockBehavior.BlockBehavior;
import objects.entity.Entity;
import tools.*;
import world.World;

public class Block extends ObjectInstance<BlockType>{
    private ArrayList<BlockBehavior> blockBehaviors = new ArrayList<>();

    public Block(BlockType blockType) {
        super(blockType);
    }

    public void addBlockBehavior(BlockBehavior blockBehavior) {
        blockBehaviors.add(blockBehavior);
        blockBehavior.onAttachToBlock(this);
    }

    //#region behavior events

    private void executeEvent(Consumer<BlockBehavior> fonction) {
        for (BlockBehavior blockBehavior : blockBehaviors)
            fonction.accept(blockBehavior);
        updateStates();
    }

    public void onUpdate(World world,Vector position) {
        executeEvent(b -> b.onUpdate(world,this,position));
    }

    public void onInteraction(World world, Vector position, Entity entity) {
        executeEvent(b -> b.onInteraction(world,this,position,entity));
    }

    public void onPush(World world, Vector position, int depX, int depY, int depZ) {
        executeEvent(b -> b.onPush(world,this,position,depX,depY,depZ));
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
