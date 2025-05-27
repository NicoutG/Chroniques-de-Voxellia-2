package objects.property;

import graphics.ligth.LightSource;

public class PropertyLight extends Property {
    public static final String NAME = "light";

    private LightSource light;

    public PropertyLight(LightSource lightSource) {
        super(NAME);
        light = lightSource;
    }

    public LightSource getLight() {
        return light;
    }
}
