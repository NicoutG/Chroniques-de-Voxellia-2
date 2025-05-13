package objects;

import graphics.Texture;
import objects.collision.Collision;
import objects.property.Property;

public class ObjectInstance<T extends ObjectType> {
    protected final T type;
    protected int indexTexture = 0;
    protected int indexCollision = 0;

    public ObjectInstance(T type) {
        this.type = type;
    }

    public String getName() {
        return type.getName();
    }

    public double getOpacity() {
        return type.getOpacity();
    }

    public Texture getTexture() {
        return type.getTexture(indexTexture);
    }

    public Collision getCollision() {
        return type.getCollision(indexCollision);
    }

    public Property getProperty(String propertyName) {
        return type.getProperty(propertyName);
    }
}
