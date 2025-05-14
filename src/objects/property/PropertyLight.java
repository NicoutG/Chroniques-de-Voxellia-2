package objects.property;

import graphics.ligth.LightSource;

public class PropertyLight extends Property {

    private LightSource light;

    public PropertyLight(LightSource lightSource) {
        super("light");
        light = lightSource;
    }

    public LightSource getLight() {
        return light;
    }
}
