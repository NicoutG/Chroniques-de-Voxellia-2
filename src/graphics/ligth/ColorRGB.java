package graphics.ligth;

public record ColorRGB(double r, double g, double b) {

    public static final ColorRGB BLACK = new ColorRGB(0,0,0);

    public ColorRGB mul(double s) {               // brightness scaling
        return new ColorRGB(r*s, g*s, b*s);
    }

    public ColorRGB add(ColorRGB o) {            // additive blend
        return new ColorRGB(r+o.r, g+o.g, b+o.b);
    }

    public ColorRGB clamp() {                    // keep in [0,1]
        return new ColorRGB(Math.min(r,1), Math.min(g,1), Math.min(b,1));
    }
}