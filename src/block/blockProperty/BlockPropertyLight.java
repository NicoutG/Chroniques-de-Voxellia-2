package block.blockProperty;

import graphics.ligth.LightSource;

public class BlockPropertyLight extends BlockProperty {

    private LightSource light;

    public BlockPropertyLight(LightSource lightSource) {
        super("light");
        light = lightSource;
    }

    public LightSource getLight() {
        return light;
    }
}
