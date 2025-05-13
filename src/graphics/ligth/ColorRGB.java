package graphics.ligth;

public record ColorRGB(double r, double g, double b) {

    public static final ColorRGB BLACK = new ColorRGB(0, 0, 0);

    /* --- scalar multiplier ------------------------------------------------ */
    public ColorRGB mul(double s) {
        return new ColorRGB(r * s, g * s, b * s);
    }

    /* --- additive blend (no global state, no weighting) ------------------- */
    public ColorRGB add(ColorRGB o) {
        return new ColorRGB(r + o.r, g + o.g, b + o.b);
    }

    /* --- clamp to [0 ; 1] -------------------------------------------------- */
    public ColorRGB clamp() {
        return new ColorRGB(Math.min(r, 1), Math.min(g, 1), Math.min(b, 1));
    }
}
