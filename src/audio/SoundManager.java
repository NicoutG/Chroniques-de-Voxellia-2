package audio;

import objects.block.Block;
import objects.entity.Entity;
import objects.entity.Player;
import objects.property.PropertySound;
import tools.PathManager;
import world.World;

import javax.sound.sampled.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class SoundManager {

    private final World world;

    private double globalVolume;

    /** Maximum distance (in world units) at which a sound is audible. */
    private static final double MAX_DISTANCE = 20.0;

    /** If the linear volume falls below this threshold the clip is paused. */
    private static final double EPSILON = 0.04;

    /* ------------------------------------------------------------------ */

    private record ManagedClip(Clip clip, boolean looping) {
    }

    private static final Map<SoundType, ManagedClip> clips = new EnumMap<>(SoundType.class);

    public SoundManager(World world) {

        this.world = world;
        this.globalVolume = 0.2;

        /* ----------- load & cache every clip once -------------- */
        for (SoundType st : SoundType.values()) {
            Clip c = PathManager.loadSound(st.path);
            if (c != null) {
                clips.put(st, new ManagedClip(c, st.looping));
            }
        }
    }

    /**
     * Call once per game‑loop iteration (e.g. from {@code GamePanel.tick()}).
     */
    public void tick() {

        /*
         * ----------------------------------------------------------
         * 1) Handle queued per-tick sounds (World#getSounds())
         * ----------------------------------------------------------
         */
        List<SoundType> eventSounds = world.getSounds();
        if (eventSounds != null && !eventSounds.isEmpty()) {

            // Copy to avoid concurrent-mod with World.removeSound()
            for (SoundType st : new ArrayList<>(eventSounds)) {
                ManagedClip mc = clips.get(st);
                if (mc == null)
                    continue; // no clip loaded

                /* play only if stopped */
                if (!mc.clip.isRunning()) {
                    mc.clip.setFramePosition(0);
                    setVolume(mc.clip, globalVolume * st.volume); // ① gain FIRST
                    if (st.looping) {
                        mc.clip.loop(Clip.LOOP_CONTINUOUSLY);
                    } else {
                        mc.clip.start();
                    }
                }

                /* remove one-shots through the World API */ // ② proper removal
                if (!st.looping) {
                    world.removeSound(st);
                }
            }
        }

        /*
         * --------------------------------------------------------------
         * 2) Spatialised ambiences: only the closest emitter of each type.
         * --------------------------------------------------------------
         */

        Player player = world.getPlayer();
        if (player == null || world == null)
            return;

        /* 1) Find the closest source for each SoundType. */
        Map<SoundType, Double> nearestSq = new EnumMap<>(SoundType.class);

        /* ---- blocks ---- */
        Block[][][] blocks = world.getBlocks();
        if (blocks != null) {
            for (int x = 0; x < blocks.length; x++)
                for (int y = 0; y < blocks[0].length; y++)
                    for (int z = 0; z < blocks[0][0].length; z++) {
                        Block b = blocks[x][y][z];
                        if (b == null)
                            continue;
                        if (b.getProperty("sound") == null)
                            continue;
                        PropertySound soundProp = (PropertySound) b.getProperty("sound");
                        if (soundProp.getSound() == null)
                            continue;
                        double d2 = dist2(player.getX(), player.getY(), player.getZ(),
                                x + 0.5, y + 0.5, z + 0.5);
                        nearestSq.merge(soundProp.getSound(), d2, Math::min);
                    }
        }

        /* ---- entities ---- */
        for (Entity e : world.getEntities()) {
            if (e == null)
                continue;
            if (e.getProperty("sound") == null)
                continue;
            PropertySound soundProp = (PropertySound) e.getProperty("sound");
            if (soundProp.getSound() == null)
                continue;
            double d2 = dist2(player.getX(), player.getY(), player.getZ(),
                    e.getX(), e.getY(), e.getZ());
            nearestSq.merge(soundProp.getSound(), d2, Math::min);
        }

        /* 2) Play/stop & set volume. */
        for (Map.Entry<SoundType, ManagedClip> entry : clips.entrySet()) {
            SoundType st = entry.getKey();
            ManagedClip mc = entry.getValue();
            Double d2 = nearestSq.get(st);

            if (d2 == null || d2 > MAX_DISTANCE * MAX_DISTANCE) {
                pause(mc);
                continue;
            }

            double d = Math.sqrt(d2);
            double volLinear = (1.0 - (d / MAX_DISTANCE)) * globalVolume * st.volume;

            if (volLinear < EPSILON) {
                pause(mc);
            } else {
                ensurePlaying(mc, volLinear);
            }
        }
    }

    public static void stopSound(SoundType soundType) {
        ManagedClip mc = clips.get(soundType);
        if (mc != null && mc.clip.isRunning())
            mc.clip.stop();
    }

    /** Close every {@link Clip}. Call once when your program exits. */
    public void shutdown() {
        clips.values().forEach(mc -> {
            mc.clip.stop();
            mc.clip.close();
        });
    }

    private void ensurePlaying(ManagedClip mc, double volLin) {
        if (mc.clip.isRunning())
            return;
        mc.clip.setFramePosition(0);
        setVolume(mc.clip, volLin); // gain FIRST
        if (mc.looping) {
            mc.clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            mc.clip.start();
        }
    }

    private void pause(ManagedClip mc) {
        if (!mc.looping)
            return;
        if (mc.clip.isRunning())
            mc.clip.stop();
    }

    private void setVolume(Clip clip, double lin) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
            return;
        FloatControl ctrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        /* convert linear 0‑1 → dB ; clamp to the line’s legal range */
        float dB = (float) (20.0 * Math.log10(Math.max(0.0001, lin)));
        dB = Math.max(ctrl.getMinimum(), Math.min(ctrl.getMaximum(), dB));
        ctrl.setValue(dB);
    }

    private static double dist2(double x1, double y1, double z1,
            double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }
}
