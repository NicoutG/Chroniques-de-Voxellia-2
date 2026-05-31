package audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import objects.ObjectInstance;
import objects.block.Block;
import objects.entity.Entity;
import objects.entity.Player;
import objects.property.PropertySound;
import tools.PathManager;
import tools.PathManager.Data;
import tools.Vector;
import world.World;

public class SoundManager {
    private static World world;
    private static double globalVolume;
    private static boolean needToResetAllSounds = true;
    private static boolean needToResetAllSoundsFromPositions = true;

    private static final double MAX_DISTANCE = 15.0;
    private final static String SOUND = "ambientSound";

    private static HashMap<ISoundType, Data> sounds = new HashMap<>();
    private static HashMap<ISoundType, ArrayList<SimpleAudioSource>> playingSounds = new HashMap<>();
    private static HashMap<SimpleAudioSource, SoundTypeAndCoordinates> playingSoundsFromCoordinates = new HashMap<>();

    public SoundManager(World world) {
        if (world == null)
            return;
        SoundManager.world = world;
        globalVolume = 0.2;

        for (ISoundType st : SoundType.values())
            loadSound(st);
    }

    public void tick() {
        // update volume
        for (var sas : new HashSet<SimpleAudioSource>(playingSoundsFromCoordinates.keySet()))
            updateSound(sas);

        // find sounds in property and states
        Block[][][] blocks = world.getBlocks();
        if (blocks != null) {
            for (int x = 0; x < blocks.length; x++)
                for (int y = 0; y < blocks[0].length; y++)
                    for (int z = 0; z < blocks[0][0].length; z++) {
                        Block b = blocks[x][y][z];
                        if (b == null)
                            continue;

                        playObjectInstanceSounds(b, new Vector(x + 0.5, y + 0.5, z + 0.5));
                    }
        }
        for (Entity e : world.getEntities()) {
            if (e == null)
                continue;
            playObjectInstanceSounds(e, e.getPosition());
        }

        needToResetAllSounds = false;
        needToResetAllSoundsFromPositions = false;
    }

    //#region play

    public static SimpleAudioSource playSound(ISoundType st) {
        return playSound(st, st.getVolume() * globalVolume);
    }

    public static SimpleAudioSource playSound(ISoundType st, Vector pos) {
        double d2 = getVolumeFromDistance2(st, dist2(world.getPlayer().getPosition(), pos));
        SimpleAudioSource sas = playSound(st, d2);
        if (sas == null)
            return null;
        ArrayList<Vector> positions;
        if (playingSoundsFromCoordinates.containsKey(sas))
            positions = playingSoundsFromCoordinates.get(sas).pos();
        else {
            positions = new ArrayList<>();
            playingSoundsFromCoordinates.put(sas, new SoundTypeAndCoordinates(st, positions));
            sas.onFinish(() -> playingSoundsFromCoordinates.remove(sas));
        }
        positions.add(pos);
        return sas;
    }

    private static SimpleAudioSource playSound(ISoundType st, double volume) {
        if (st == null)
            return null;
        try {
            if (st.isAmbient() && playingSounds.containsKey(st)) {
                var audios = playingSounds.get(st);
                if (audios.size() > 0) {
                    SimpleAudioSource sas = audios.get(0);
                    sas.setVolume(volume);
                    if (!st.isLooping())
                        sas.play(st.isLooping());
                    return sas;
                }
            }
            Data data = getData(st);
            SimpleAudioSource sas = new SimpleAudioSource(data.bytes(), data.format());
            sas.setVolume(volume);
            sas.play(st.isLooping());
            ArrayList<SimpleAudioSource> audioList;
            if (playingSounds.containsKey(st))
                audioList = playingSounds.get(st);
            else {
                audioList = new ArrayList<>();
                playingSounds.put(st, audioList);
            }
            audioList.add(sas);
            sas.onFinish(() -> removeFromPlayingSound(st, sas));
            return sas;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //#endregion

    //#region stop

    public static void stopAll() {
        HashMap<ISoundType, ArrayList<SimpleAudioSource>> snapshot =
                new HashMap<>(playingSounds);

        playingSounds.clear();
        playingSoundsFromCoordinates.clear();

        needToResetAllSounds = true;

        for (ArrayList<SimpleAudioSource> audioList : snapshot.values()) {

            for (SimpleAudioSource sas : new ArrayList<>(audioList)) {
                try {
                    sas.stop();
                } catch (Exception ignored) {}
            }
        }
    }

    public static void stopAllFromCoordinates() {

        HashSet<SimpleAudioSource> snapshot =
                new HashSet<>(playingSoundsFromCoordinates.keySet());

        playingSoundsFromCoordinates.clear();

        needToResetAllSoundsFromPositions = true;

        for (SimpleAudioSource sas : snapshot) {
            try {
                sas.stop();
            } catch (Exception ignored) {}
        }
    }

    //#endregion

    //#region utils

    private static double getVolumeFromDistance2(ISoundType st, double d2) {
        double vol = 0;
        if (d2 <= MAX_DISTANCE * MAX_DISTANCE) {
            double d = Math.sqrt(d2);
            float t = (float)(d / MAX_DISTANCE);
            t = Math.min(1f, t);

            float smooth = t * t * (3 - 2 * t);

            vol = (globalVolume * st.getVolume() * (1.0 - smooth));
        }
        return vol;
    }

    private static void updateSound(SimpleAudioSource sas) {
        if (!playingSoundsFromCoordinates.containsKey(sas))
            return;
        SoundTypeAndCoordinates stc = playingSoundsFromCoordinates.get(sas);
        if (stc.pos.size() <= 0)
            return;
        Player player = world.getPlayer();

        double closestDistance = Double.MAX_VALUE;
        for (Vector pos : stc.pos) {
            double d2 = dist2(player.getPosition(), pos);
            if (d2 < closestDistance)
                closestDistance = d2;
        }
        sas.setVolume(getVolumeFromDistance2(stc.st, closestDistance));
    }

    private static void loadSound(ISoundType sound) {
        Data data = PathManager.loadSound(sound.getPath());
        if (data != null)
            sounds.put(sound, data);
    }

    private static Data getData(ISoundType soundType) {
        if (!sounds.containsKey(soundType))
            loadSound(soundType);
        return sounds.get(soundType);
    }

    private record SoundTypeAndCoordinates(ISoundType st, ArrayList<Vector> pos) {}

    private static double dist2(Vector pos1, Vector pos2) {
        double dx = pos1.x - pos2.x;
        double dy = pos1.y - pos2.y;
        double dz = pos1.z - pos2.z;
        return dx * dx + dy * dy + dz * dz;
    }

    private static void playObjectInstanceSounds(ObjectInstance<?,?,?> oi, Vector pos) {
        ISoundType st = getAmbientSoundFromObjectInstance(oi);
        if (st != null)
            if (needToResetAllSounds) {
                    playSound(st);
            }
        st = getSoundFromObjectInstance(oi);
        if (st != null) {
            if (!st.isLooping() || needToResetAllSoundsFromPositions || needToResetAllSounds)
                playSound(st, pos);
        }
    }

    private static ISoundType getSoundFromObjectInstance(ObjectInstance<?,?,?> oi) {

        if (oi.getProperty(PropertySound.NAME) != null) {
            PropertySound soundProp = (PropertySound) oi.getProperty(PropertySound.NAME);
            ISoundType st = soundProp.getSound();
            if (st != null)
                return st;
        }

        return null;
    }

    private static ISoundType getAmbientSoundFromObjectInstance(ObjectInstance<?,?,?> oi) {
        Object state = oi.getState(SOUND);
        if (state != null && state instanceof String soundPath) {
            ISoundType sound = SoundType.getSoundType(soundPath);
            if (sound != null)
                return sound;
        }

        return null;
    }

    private static void removeFromPlayingSound(ISoundType st, SimpleAudioSource sas) {
        var audioList = playingSounds.get(st);
        if (audioList == null)
            return;
        if (audioList.size() <= 1)
            playingSounds.remove(st);
        else
            audioList.remove(sas);
    }

    //#endregion
}
