package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import graphics.Texture;
import graphics.ligth.ColorRGB;
import objects.collision.Collision;
import objects.property.Property;

public class ObjectInstance<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, B>,
    B extends ObjectBehavior<T, I, B>
> {
    protected final T type;
    protected int indexTexture = 0;
    protected int indexCollision = 0;
    private HashMap<String, Object> states = new HashMap<>();
    private HashMap<String, Object> stateModifications = null;
    protected ArrayList<B> behaviors = new ArrayList<>();

    public ObjectInstance(T type) {
        this.type = type;
    }

    public String getName() {
        return type.getName();
    }

    public int getIndexTexture() { return indexTexture; }

    public void setIndexTexture(int index) { indexTexture = index; }

    public int getIndexCollision() { return indexCollision; }

    public void setIndexCollision(int index) { indexCollision = index; }

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
        if (state == null || !(state instanceof Boolean) || (boolean)state)
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

    @SuppressWarnings("unchecked")
    public void addBehavior(B behavior) {
        behaviors.add(behavior);
        behavior.onAttachTo((I)this);
    }

    protected void executeEvent(Consumer<B> fonction) {
        for (B behavior : behaviors)
            fonction.accept(behavior);
        updateStates();
    }
}
