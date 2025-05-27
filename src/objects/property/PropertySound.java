package objects.property;

import audio.SoundType;

public class PropertySound extends Property {
    public static final String NAME = "sound";

    private SoundType sound;

    public PropertySound(SoundType sound) {
        super(NAME);
        this.sound = sound;
    }

    public SoundType getSound() {
        return sound;
    }
}
