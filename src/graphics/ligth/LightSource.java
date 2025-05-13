package graphics.ligth;

public final class LightSource {
    private final ColorRGB color;
    private final double intensity, falloff, oscillation;

    public LightSource(ColorRGB color, double intensity, double falloff, double oscillation) {
        this.color = color;
        this.intensity = intensity;
        this.falloff = falloff;
        this.oscillation = oscillation;
    }

    public ColorRGB color() {
        return color;
    }

    public double intensity() {
        return intensity;
    }

    public double oscillation() {
        return oscillation;
    }

    public double oscillatingIntensity() {
        if (oscillation == 0) return intensity;
        else {
            double variation = intensity * oscillation * (Math.random() * 2 - 1);
            return intensity + variation;
        }
    }

    public double falloff() {
        return falloff;
    }
}
