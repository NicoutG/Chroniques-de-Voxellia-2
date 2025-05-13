package graphics.ligth;

public final class LightSource {
    private final ColorRGB color;
    private final float intensity, falloff;

    public LightSource(ColorRGB color, float intensity, float falloff) {
        this.color = color;
        this.intensity = intensity;
        this.falloff = falloff;
    }

    public ColorRGB color() {
        return color;
    }

    public float intensity() {
        return intensity;
    }

    public float falloff() {
        return falloff;
    }
}
