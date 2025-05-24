package tools;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.image.BufferedImage;

import world.World;

public class PathManager {
    public static final String TEXTURE_PATH = "/resources/textures/outlined/";
    public static final String MASK_PATH = "/resources/masks/";
    public static final String WORLD_PATH = "src/resources/worlds/";
    public static final String SOUND_PATH = "/resources/sounds/";

    public static BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(World.class.getResource(PathManager.TEXTURE_PATH + filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Clip loadSound(String filePath) {
        // Make sure your audio files are in WAV, AU, or AIFF format (not MP3 or others)
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(
                PathManager.class.getResourceAsStream(SOUND_PATH + filePath))) {
            Clip c = AudioSystem.getClip();
            c.open(ais);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
