package objects.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import model.world.World;
import objects.ObjectInstance;
import objects.block.blockBehavior.BlockBehavior;
import objects.entity.Entity;
import tools.*;

public class Block extends ObjectInstance<BlockType>{
    private ArrayList<BlockBehavior> blockBehaviors = new ArrayList<>();
    private HashMap<String, Object> states = new HashMap<>();
    private HashMap<String, Object> stateModifications = null;

    public Block(BlockType blockType) {
        super(blockType);
    }

    public void addBlockBehavior(BlockBehavior blockBehavior) {
        blockBehaviors.add(blockBehavior);
        blockBehavior.onAttachToBlock(this);
    }

    public Object getState(String stateName) {
        return states.get(stateName);
    }

    public boolean setState(String stateName, Object value) {
        if(existingState(stateName)) {
            states.replace(stateName, value);
            return true;
        }
        states.put(stateName, value);
        return false;
    }

    public boolean existingState(String stateName) {
        return states.containsKey(stateName);
    }

    public boolean setStateModification(String stateName, Object value) {
        if (stateModifications == null)
            stateModifications = new HashMap<>();
        if(stateModifications.containsKey(stateName)) {
            stateModifications.replace(stateName, value);
            return true;
        }
        stateModifications.put(stateName, value);
        return false;
    }

    public boolean updateStates() {
        if (stateModifications.isEmpty())
            return false;
        for (var entrySet : stateModifications.entrySet())
            setState(entrySet.getKey(), entrySet.getValue());
        stateModifications = null;
        return true;
    }

    //#region behavior events

    private void executeEvent(Consumer<BlockBehavior> fonction) {
        for (BlockBehavior blockBehavior : blockBehaviors)
            fonction.accept(blockBehavior);
        updateStates();
    }

    public void onUpdate(World world) {
        executeEvent(b -> b.onUpdate(world,this));
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

    public void onEntityIn(World world, Vector position, Entity entity) {
        executeEvent(b -> b.onEntityIn(world,this,position,entity));
    }

    public void onEntityCollision(World world, Vector position, Entity entity) {
        executeEvent(b -> b.onEntityCollision(world,this,position,entity));
    }

    //#endregion
}
