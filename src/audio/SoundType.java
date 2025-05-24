package audio;

public enum SoundType {
    FIRE("fire-loop.wav", true, 0.4),
    LAVA("lava-loop.wav", true, 1.25);

    final String path;
    final boolean looping;
    final double volume;

    SoundType(String path, boolean looping, double volume) {
        this.path = path;
        this.looping = looping;
        this.volume = volume;
    }
}