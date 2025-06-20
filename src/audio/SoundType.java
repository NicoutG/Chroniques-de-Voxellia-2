package audio;

public enum SoundType {
    FIRE("fire-loop.wav", true, 0.35),
    LAVA("lava-loop.wav", true, 1.15),
    LEVER("lever.wav", false, 0.85),
    WATER("water-loop.wav", true, 6),
    HELICOPTER("helicopter-loop.wav", true, 0.7),
    TELEPORTATION("teleportation.wav", false, 15),
    JUMP1("movements/jump1.wav", false, 0.2),
    JUMP2("movements/jump2.wav", false, 0.4),
    DEATH("death.wav", false, 2),
    WORLD_LOADER_ACTIVATION("world-loader-activation.wav", false, 7),
    TELEPORTER_ACTIVATION("teleporter-activation.wav", false, 4),
    TYPING("typing.wav", false, 0.5),
    EXPLOSION("explosion.wav", false, 2),
    // Ambient :
    AMBIENT1("ambient/moon-origin-loop.wav", true, 1, true),
    AMBIENT2("ambient/lost-in-forest-loop.wav", true, 1, true),
    BACKGROUND_NATURE("ambient/nature-background-loop.wav", true, 4, true);

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