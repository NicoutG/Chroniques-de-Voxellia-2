package tools;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.image.BufferedImage;
import java.io.File;

import world.World;

public class PathManager {
    public static final String TEXTURE_PATH = "/resources/textures/outlined/";
    public static final String MASK_PATH = "/resources/masks/";
    public static final String WORLD_PATH = "src/resources/worlds/";
    public static final String SOUND_PATH = "src/resources/sounds/";

    public static BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(World.class.getResource(PathManager.TEXTURE_PATH + filePath));
        } catch (Exception e) {
            System.out.println("Error loading : "+filePath);
            e.printStackTrace();
        }
        return null;
    }

    public static ManagedClip loadSound(String path, boolean looping) {
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(new File(SOUND_PATH + path))) {
            AudioFormat format = ais.getFormat();
            byte[] data = ais.readAllBytes();
            Clip clip = AudioSystem.getClip();
            clip.open(format, data, 0, data.length);
            return new ManagedClip(clip, data, looping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
