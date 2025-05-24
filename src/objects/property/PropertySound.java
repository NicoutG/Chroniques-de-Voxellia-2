package objects.property;

import audio.SoundType;

public class PropertySound extends Property {

    private SoundType sound;

    public PropertySound(SoundType sound) {
        super("sound");
        this.sound = sound;
    }

    public SoundType getSound() {
        return sound;
    }
}
