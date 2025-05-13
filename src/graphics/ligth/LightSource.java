package graphics.ligth;

public final class LightSource {
    private final ColorRGB color;
    private final double intensity, falloff;

    public LightSource(ColorRGB color, double intensity, double falloff) {
        this.color = color;
        this.intensity = intensity;
        this.falloff = falloff;
    }

    public ColorRGB color() {
        return color;
    }

    public double intensity() {
        return intensity;
    }

    public double falloff() {
        return falloff;
    }
}
