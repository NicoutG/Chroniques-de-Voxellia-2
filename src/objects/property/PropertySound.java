package objects.property;

import java.util.Random;

import audio.SoundType;
import world.World;

public class PropertySound extends Property {
    public static final String NAME = "sound";

    private SoundType sound;
    private int waitingTick = 0;
    private int minWaitingTime = 0;
    private int maxWaitingTime = 0;
    private int lastTick = -1;

    public PropertySound(SoundType sound) {
        super(NAME);
        this.sound = sound;
    }

    public PropertySound(SoundType sound, int waitingTick) {
        super(NAME);
        this.sound = sound;
        this.waitingTick = waitingTick;
    }

    public PropertySound(SoundType sound, int minWaitingTime, int maxWaitingTime) {
        super(NAME);
        this.sound = sound;
        this.minWaitingTime = minWaitingTime;
        this.maxWaitingTime = maxWaitingTime;
        waitingTick = getWaitingTick();
    }

    private int getWaitingTick() {
        if (0 <= minWaitingTime && 0 < maxWaitingTime) {
            Random random = new Random();
            return minWaitingTime + random.nextInt(maxWaitingTime - minWaitingTime);
        }
        return waitingTick;
    }

    public SoundType getSound() {
        int currentTick = World.getTick();
        if (lastTick == currentTick || lastTick + waitingTick <= currentTick) {
            lastTick = currentTick;
            waitingTick = getWaitingTick();
            return sound;
        }
        return null;
    }
}
