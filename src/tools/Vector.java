package tools;

public class Vector {
    public double x;
    public double y;
    public double z;

    public Vector() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector clone() {
        return new Vector(x,y,z);
    }
}
