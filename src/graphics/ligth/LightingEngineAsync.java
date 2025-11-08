package graphics.ligth;

import world.World;

public class LightingEngineAsync extends LightingEngine {
    private volatile FaceLighting[][][] faceLightings = null;
    private volatile boolean threadFree = true;

    @Override
    public FaceLighting[][][] getLightings(World world, long tick) {
        if (faceLightings == null)
            return super.getLightings(world, tick);
        else {
            if (threadFree) {
                threadFree = false;
                new Thread(() -> {
                    faceLightings = super.getLightings(world, tick);
                    threadFree = true;
                }).start();
            }
            return faceLightings;
        }
    }
}
