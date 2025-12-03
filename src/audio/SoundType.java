package audio;

import java.util.ArrayList;
import java.util.List;

public enum SoundType implements ISoundType { 
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
    BACKGROUND_NATURE("ambient/nature-background-loop.wav", true, 4, true), 
    PIRATE_MUSIC("ambient/pirate-music-loop.wav", true, 2, true), 
    
    ;

    final String path;
    final boolean looping;
    final double volume;
    final boolean ambient;

    private static final List<ISoundType> otherSounds = new ArrayList<>();

    SoundType(String path, boolean looping, double volume, boolean ambient) {
        this.path = path;
        this.looping = looping;
        this.volume = volume;
        this.ambient = ambient;
    }

    SoundType(String path, boolean looping, double volume) {
        this(path, looping, volume, false);
    }

    public static List<ISoundType> getOtherSounds() {
        return otherSounds;
    }

    public static ISoundType getSoundType(String path) {
        for (SoundType s : values())
            if (s.path.equals(path)) return s;

        for (ISoundType s : otherSounds)
            if (s.getPath().equals(path)) return s;

        // Crée un son dynamique
        DynamicSound ds = new DynamicSound(path);
        otherSounds.add(ds);
        return ds;
    }

    @Override
    public String getPath() { return path; }

    @Override
    public boolean isLooping() { return looping; }

    @Override
    public double getVolume() { return volume; }

    @Override
    public boolean isAmbient() { return ambient; }
}

// Interface commune pour tous les sons
interface ISoundType {
    String getPath();
    boolean isLooping();
    double getVolume();
    boolean isAmbient();
}

// Classe pour les sons dynamiques
class DynamicSound implements ISoundType {
    private final String path;
    private final boolean looping = true;
    private final double volume = 1.0;
    private final boolean ambient = true;

    public DynamicSound(String path) { this.path = path; }

    @Override public String getPath() { return path; }
    @Override public boolean isLooping() { return looping; }
    @Override public double getVolume() { return volume; }
    @Override public boolean isAmbient() { return ambient; }

    @Override
    public String toString() { return "DynamicSound(" + path + ")"; }
}
