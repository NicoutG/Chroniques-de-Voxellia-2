package objects;

import java.util.ArrayList;
import java.util.HashMap;

import graphics.Texture;
import objects.collision.Collision;
import objects.property.Property;

public abstract class ObjectType {
    private String name;
    private double opacity;
    private ArrayList<Texture> textures = new ArrayList<>();
    private ArrayList<Collision> collisions = new ArrayList<>();
    private HashMap<String, Property> properties = new HashMap<>();

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

    public Texture getTexture(int index) {
        if (0 <= index && index < textures.size())
            return textures.get(index);
        return null;
    }

    public void addTexture(Texture texture) { 
        textures.add(texture);
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

    public abstract <T extends ObjectType> ObjectInstance<T> getInstance();
}
