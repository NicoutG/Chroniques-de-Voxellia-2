package entity;

import graphics.Texture;
import tools.*;;

public class Entity {
    protected final Texture tex;
    protected Vector position;

    public Entity(Texture tex, double x, double y, double z) {
        this.tex = tex;
        position = new Vector();
        setPosition(x,y,z);
    }

    public Texture getTexture() {
        return tex;
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
