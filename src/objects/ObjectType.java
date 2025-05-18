package objects;

import java.util.ArrayList;
import java.util.HashMap;

import graphics.Texture;
import graphics.ligth.ColorRGB;
import objects.collision.Collision;
import objects.property.Property;

public abstract class ObjectType<
    I extends ObjectInstance<?, ?, ?>,
    B extends ObjectBehavior<?, ?, ?>
> {
    private String name;
    private double opacity;
    private ColorRGB color; // When 0 < opacity < 1
    private boolean[] allowLight = new boolean[]{false, false, false};
    private ArrayList<Texture> textures = new ArrayList<>();
    private ArrayList<Collision> collisions = new ArrayList<>();
    private HashMap<String, Property> properties = new HashMap<>();
    protected ArrayList<B> behaviors = new ArrayList<>();

    public ObjectType(String name) {
        this.name = name;
        this.opacity = 1;
    }

    public String getName() {
        return name;
    }

    public double getOpacity() { 
        return opacity;
    }

    public void setOpacity(double opacity) { 
        this.opacity = opacity;
    }
    
    public ColorRGB getColor() { 
        return color;
    }

    public void setColor(ColorRGB color) { 
        this.color = color;
    }

    public Texture getTexture(int index) {
        if (0 <= index && index < textures.size())
            return textures.get(index);
        return null;
    }

    public void addTexture(Texture texture) { 
        textures.add(texture);
    }

    public boolean isLightAllowed(int index) { 
        return allowLight[index];
    }

    public void setAllowLight(int index, boolean val) { 
        this.allowLight[index] = val;
    }
    
    public Collision getCollision(int index) {
        int nbCollisions = collisions.size();
        if (nbCollisions == 0)
            return new Collision();
        if (0 <= index && index < nbCollisions)
            return collisions.get(index);
        return null;
    }

    public void addCollision(Collision collision) { 
        collisions.add(collision);
    }

    public Property getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public boolean addProperty(Property property) {
        if (properties.containsKey(property.name))
            return false;
        properties.put(property.name, property);
        return true;
    }

    public void addBehavior(B behavior) {
        behaviors.add(behavior);
    }

    public abstract I getInstance();
}
