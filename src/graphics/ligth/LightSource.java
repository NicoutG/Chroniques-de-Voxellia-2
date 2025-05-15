package graphics.ligth;

public final class LightSource {

    /* ------------------------------------------------------------ */
    /* CONSTANT DATA */
    /* ------------------------------------------------------------ */

    private final ColorRGB baseColour; // kept for backward-compat
    private final ColorRGB[] keyColours; // animation key-frames
    private final int animDuration; // ticks for a full loop

    private final double intensity;
    private final double falloff;
    private final double oscillation;

    /* ------------------------------------------------------------ */
    /* RUNTIME STATE */
    /* ------------------------------------------------------------ */

    private int tick = 0; // advances every colour() call

    /* ------------------------------------------------------------ */
    /* CONSTRUCTORS */
    /* ------------------------------------------------------------ */

    /** Simple, non-animated lamp. */
    public LightSource(ColorRGB colour,
            double intensity,
            double falloff,
            double oscillation) {

        this.baseColour = colour;
        this.keyColours = new ColorRGB[0];
        this.animDuration = 0;

        this.intensity = intensity;
        this.falloff = falloff;
        this.oscillation = oscillation;
    }

    /**
     * Animated lamp: the colour cycles through {@code colours} over
     * {@code animationDuration} ticks, then loops.
     *
     * @param colours           at least 2 colours (otherwise behaves like static
     *                          lamp)
     * @param animationDuration > 0, total length of one full cycle (ticks)
     */
    public LightSource(ColorRGB[] colours,
            int animationDuration,
            double intensity,
            double falloff,
            double oscillation) {

        this.baseColour = (colours != null && colours.length > 0)
                ? colours[0]
                : ColorRGB.BLACK;
        this.keyColours = (colours != null) ? colours.clone() : new ColorRGB[0];
        this.animDuration = Math.max(animationDuration, 0);

        this.intensity = intensity;
        this.falloff = falloff;
        this.oscillation = oscillation;
    }

    /* ------------------------------------------------------------ */
    /* COLOUR ANIMATION */
    /* ------------------------------------------------------------ */

    /** Linear interpolation helper. */
    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    /** Returns the animated colour and advances the internal tick. */
    private ColorRGB animatedColour() {

        /* Edge-cases: no animation requested or bad parameters. */
        if (keyColours.length < 2 || animDuration <= 0) {
            tick++; // still advance, to stay deterministic
            return baseColour;
        }

        /* --- time parameters ---------------------------------------- */
        int cycleTick = tick % animDuration; // position inside current cycle
        double phase = (double) cycleTick / animDuration; // [0,1)

        /* --- determine current segment ------------------------------ */
        int segments = keyColours.length; // one segment per key colour
        double segLength = 1.0 / segments; // phase span of each segment
        int segIndex = (int) (phase / segLength);
        double localT = (phase - segIndex * segLength) / segLength; // 0-1 inside segment

        ColorRGB c0 = keyColours[segIndex];
        ColorRGB c1 = keyColours[(segIndex + 1) % segments];

        /* --- component-wise interpolation --------------------------- */
        ColorRGB out = new ColorRGB(
                lerp(c0.r(), c1.r(), localT),
                lerp(c0.g(), c1.g(), localT),
                lerp(c0.b(), c1.b(), localT));

        tick++; // advance for next call
        return out;
    }

    /* ------------------------------------------------------------ */
    /* PUBLIC API */
    /* ------------------------------------------------------------ */

    /** The colour that should be used **right now**. */
    public ColorRGB color() {
        return (animDuration == 0) ? baseColour : animatedColour();
    }

    public double intensity() {
        return intensity;
    }

    public double falloff() {
        return falloff;
    }

    public double oscillation() {
        return oscillation;
    }

    /** Intensity with a per-tick random “sparkle” (unchanged behaviour). */
    public double oscillatingIntensity() {
        if (oscillation == 0)
            return intensity;
        double variation = intensity * oscillation * (Math.random() * 2 - 1);
        return intensity + variation;
    }
}
