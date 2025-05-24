package audio;

public enum SoundType {
    FIRE("fire-loop.wav", true);

    final String path;
    final boolean looping;

    SoundType(String path, boolean looping) {
        this.path = path;
        this.looping = looping;
    }
}