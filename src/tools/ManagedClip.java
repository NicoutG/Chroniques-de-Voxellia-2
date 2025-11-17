package tools;

import javax.sound.sampled.Clip;

public final class ManagedClip {
    public Clip clip;
    public byte[] data;
    public boolean looping;

    public ManagedClip(Clip clip, byte[] data, boolean looping) {
        this.clip = clip;
        this.data = data;
        this.looping = looping;
    }
}
