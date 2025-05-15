package objects;

import java.util.HashMap;

import graphics.Texture;
import graphics.ligth.ColorRGB;
import objects.collision.Collision;
import objects.property.Property;

public class ObjectInstance<T extends ObjectType> {
    protected final T type;
    protected int indexTexture = 0;
    protected int indexCollision = 0;
    private HashMap<String, Object> states = new HashMap<>();
    private HashMap<String, Object> stateModifications = null;

    public ObjectInstance(T type) {
        this.type = type;
    }

    public String getName() {
        return type.getName();
    }

    public double getOpacity() {
        Object state = getState("opacity");
        if (state == null || !(state instanceof Double))
            return type.getOpacity();
        else
            return (double)state;
    }

    public Texture getTexture() {
        return type.getTexture(indexTexture);
    }
    
    public ColorRGB getColor() {
        Object state = getState("color");
        if (state == null || !(state instanceof ColorRGB))
            return type.getColor();
        else
            return (ColorRGB) state;
    }

    public Collision getCollision() {
        return type.getCollision(indexCollision);
    }

    public Property getProperty(String propertyName) {
        Object state = getState(propertyName);
        if (state == null || !(state instanceof Double) || (boolean)state)
            return type.getProperty(propertyName);
        else
            return null;
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
        if (stateModifications == null || stateModifications.isEmpty())
            return false;
        for (var entrySet : stateModifications.entrySet())
            setState(entrySet.getKey(), entrySet.getValue());
        stateModifications = null;
        return true;
    }
}
