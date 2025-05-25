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

    public Vector add(Vector vector) {
        return new Vector(x + vector.x, y + vector.y, z + vector.z);
    }

    public Vector sub(Vector vector) {
        return new Vector(x - vector.x, y - vector.y, z - vector.z);
    }

    public Vector mult(double coef) {
        return new Vector(coef * x , coef * y, coef * z);
    }

    public double getNorm() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vector norm() {
        return mult(1.0/getNorm());
    }

    public Vector clone() {
        return new Vector(x,y,z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (!(o instanceof Vector)) 
            return false;
       Vector v = (Vector) o;
        return x == v.x && y == v.y && z == v.z;
    }

    public String toString() {
        return x+" "+y+" "+z;
    }
}
