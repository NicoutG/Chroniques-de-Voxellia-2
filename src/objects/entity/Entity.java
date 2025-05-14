package objects.entity;

import objects.ObjectInstance;
import tools.*;

public class Entity extends ObjectInstance<EntityType>{
    protected Vector position;

    public Entity(EntityType type,double x, double y, double z) {
        super(type);
        position = new Vector();
        setPosition(x,y,z);
    }

    public double getX() {
        return position.x;
    }

    public double getY() {
        return position.y;
    }

    public double getZ() {
        return position.z;
    }

    public void setPosition(double x, double y, double z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
}
