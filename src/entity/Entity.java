package entity;

import graphics.Texture;

public class Entity {
    protected final Texture tex;
    protected double x, y, z;

    public Entity(Texture tex, double x, double y, double z) {
        this.tex = tex;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Texture getTexture() {
        return tex;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
