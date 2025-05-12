package block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import block.blockBehavior.BlockBehavior;
import block.blockProperty.BlockProperty;
import entity.Entity;
import game.Game;
import tools.*;;

public class Block {
    private BlockType blockType;
    private ArrayList<BlockBehavior> blockBehaviors = new ArrayList<>();
    private HashMap<String, Object> states = new HashMap<>();
    private HashMap<String, Object> stateModifications = null;

    public Block(BlockType blockType) {
        this.blockType = blockType;
    }

    public String getName() {
        return blockType.getName();
    }

    public void addBlockBehavior(BlockBehavior blockBehavior) {
        blockBehaviors.add(blockBehavior);
        blockBehavior.onAttachToBlock(this);
    }

    public BlockProperty getBlockProperty(String propertyName) {
        return blockType.getBlockProperty(propertyName);
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

    public void onUpdate(Game game) {
        executeEvent(b -> b.onUpdate(game,this));
    }

    public void onInteraction(Game game, Vector position, Entity entity) {
        executeEvent(b -> b.onInteraction(game,this,position,entity));
    }

    public void onPush(Game game, Vector position, int depX, int depY, int depZ) {
        executeEvent(b -> b.onPush(game,this,position,depX,depY,depZ));
    }

    public void onActivated(Game game, Vector position, int network) {
        executeEvent(b -> b.onActivated(game,this,position,network));
    }

    public void onDesactivated(Game game, Vector position, int network) {
        executeEvent(b -> b.onDesactivated(game,this,position,network));
    }

    public void onEntityIn(Game game, Vector position, Entity entity) {
        executeEvent(b -> b.onEntityIn(game,this,position,entity));
    }

    public void onEntityCollision(Game game, Vector position, Entity entity) {
        executeEvent(b -> b.onEntityCollision(game,this,position,entity));
    }

    //#endregion
}
