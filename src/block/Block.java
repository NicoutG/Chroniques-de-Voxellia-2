package block;

import java.util.ArrayList;
import java.util.HashMap;

import block.blockBehavior.BlockBehavior;
import block.blockProperty.BlockProperty;

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
}
