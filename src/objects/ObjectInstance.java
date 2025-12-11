package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import graphics.Texture;
import graphics.ligth.ColorRGB;
import objects.collision.Collision;
import objects.objectBehavior.ObjectBehavior;
import objects.property.Property;
import tools.Vector;

public class ObjectInstance<
    T extends ObjectType<?, ?>,
    I extends ObjectInstance<T, I, B>,
    B extends ObjectBehavior<T, I, B>
> {
    protected final T type;
    protected int indexTexture = 0;
    protected int indexCollision = 0;
    private HashMap<String, Object> states = new HashMap<>();
    protected ArrayList<B> behaviors = new ArrayList<>();
    private long tickFrame0 = 0;

    private static final String COLOR = "color";
    private static final String TYPE = "type";

    public ObjectInstance(T type) {
        this.type = type;
        setState(COLOR, new int[0]);
    }

    public void onStart() {
        int type = getType();
        if (0 <= type) {
            setIndexTexture(type);
            setIndexCollision(type);
        }
    }

    public String getName() {
        return type.getName();
    }

    public void initTickFrame0(long tick) {
        tickFrame0 = tick;
    }

    public long getTickFrame0() {
        return tickFrame0;
    }

    public int getIndexTexture() { return indexTexture; }

    public void setIndexTexture(int index) { indexTexture = index; }

    public int getIndexCollision() { return indexCollision; }

    public void setIndexCollision(int index) { indexCollision = index; }

    public double getOpacity() {
        if (type.getOpacity() != 1)
            return type.getOpacity();
        Texture texture = getTexture();
        if (texture != null)
            return texture.getShape().getOpacity();
        return 0;
    }

    public int[] getColor() {
        Object state = getState(COLOR);
        if (state != null && (state instanceof int[])) {
            int[] color = (int[]) state;
            if (color.length == 3)
                return color;
        }
        return null;
    }

    public Texture getTexture() {
        return type.getTexture(indexTexture);
    }

    public boolean isLightAllowed(int index) {
        Boolean light = type.isLightAllowed(index);
        if (light != null)
            return light;
        Texture texture = getTexture();
        if (texture != null)
            return texture.getShape().isLightAllowed(index);
        return true;
    }
    
    public ColorRGB getColorLight() {
        Object state = getState("color");
        if (state == null || !(state instanceof ColorRGB))
            return type.getColor();
        else
            return (ColorRGB) state;
    }

    public boolean collision(Vector position1, ObjectInstance objectInstance, Vector position2) {
        Collision collision1 = getCollision();
        if (collision1 != null) {
            Collision collision2 = objectInstance.getCollision();
            if (collision2 != null)
                return collision1.collision(position1, collision2, position2);
        }
        return false;
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

    public List<String> getAllStateNames() {
        List<String> stateNames = new ArrayList<>();
        for (String stateName : states.keySet())
            stateNames.add(stateName);
        return stateNames;
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

    @SuppressWarnings("unchecked")
    public void addBehavior(B behavior) {
        behaviors.add(behavior);
        behavior.onAttachTo((I)this);
    }

    protected void executeEvent(Consumer<B> fonction) {
        for (B behavior : behaviors)
            fonction.accept(behavior);
    }

    public boolean areSameType(I objectInstance) {
        return objectInstance.type == type;
    }

    public double getAdherency() {
        return type.getAdherency();
    }

    public int getType() {
        Object state = getState(TYPE);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
