package objects;

import java.util.ArrayList;
import java.util.HashMap;

import graphics.Texture;
import graphics.ligth.ColorRGB;
import objects.collision.Collision;
import objects.objectBehavior.ObjectBehavior;
import objects.property.Property;

public abstract class ObjectType<I extends ObjectInstance<?, ?, ?>, B extends ObjectBehavior<?, ?, ?>> {
    private String name;
    private double opacity = 1;
    private ColorRGB color; // When 0 < opacity < 1
    private boolean[] allowLight = new boolean[] { false, false, false };
    private ArrayList<Texture> textures = new ArrayList<>();
    private ArrayList<Collision> collisions = new ArrayList<>();
    private HashMap<String, Property> properties = new HashMap<>();
    protected ArrayList<B> behaviors = new ArrayList<>();

    public ObjectType(String name, Texture[] textures, Double opacity, Collision[] collisions, Property[] properties,
            B[] behaviors) {
        this.name = name;
        if (textures != null)
            for (Texture texture : textures)
                addTexture(texture);
        if (opacity != null)
            setOpacity(opacity);
        if (collisions != null)
            for (Collision collision : collisions)
                addCollision(collision);
        if (properties != null)
            for (Property property : properties)
                addProperty(property);
        if (behaviors != null)
            for (B behavior : behaviors)
                addBehavior(behavior);
    }

    public ObjectType(String name, Texture[] textures, Collision[] collisions, Property[] properties, B[] behaviors) {
        this(name, textures, null, collisions, properties, behaviors);
    }

    public ObjectType(String name, String texturePath, Collision[] collisions, Property[] properties, B[] behaviors) {
        this(name, new Texture[] { Texture.createBasicTexture(texturePath) }, collisions, properties, behaviors);
    }

    public ObjectType(String name, Texture[] textures, Property[] properties, B[] behaviors) {
        this(name, textures, null, properties, behaviors);
    }

    public ObjectType(String name, String texturePath, Property[] properties, B[] behaviors) {
        this(name, texturePath, null, properties, behaviors);
    }

    public ObjectType(String name, Texture[] textures, B[] behaviors) {
        this(name, textures, null, behaviors);
    }

    public ObjectType(String name, String texturePath, B[] behaviors) {
        this(name, texturePath, null, behaviors);
    }

    public ObjectType(String name, String texturePath) {
        this(name, texturePath, null);
    }

    public ObjectType(String name) {
        this(name, null, null, null, null, null);
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
