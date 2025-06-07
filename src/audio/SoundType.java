package audio;

public enum SoundType {
    FIRE("fire-loop.wav", true, 0.35),
    LAVA("lava-loop.wav", true, 1.15),
    LEVER("lever.wav", false, 1),
    WATER("water-loop.wav", true, 5),
    HELICOPTER("helicopter-loop.wav", true, 0.7),
    TELEPORTATION("teleportation.wav", false, 15),
    JUMP1("movements/jump1.wav", false, 0.2),
    JUMP2("movements/jump2.wav", false, 0.4),
    DEATH("death.wav", false, 2),
    TYPING("typing.wav", false, 0.5),
    AMBIENT1("ambient/moon-origin-loop.wav", true, 1, true);

    final String path;
    final boolean looping;
    final double volume;
    final boolean ambient;

    SoundType(String path, boolean looping, double volume, boolean ambient) {
        this.path = path;
        this.looping = looping;
        this.volume = volume;
        this.ambient = ambient;
    }
    
    SoundType(String path, boolean looping, double volume) {
        this.path = path;
        this.looping = looping;
        this.volume = volume;
        this.ambient = false;
    }
}