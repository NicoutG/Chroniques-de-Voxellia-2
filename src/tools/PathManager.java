package tools;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.awt.image.BufferedImage;
import java.io.File;

import world.World;

public class PathManager {
    public static final String TEXTURE_PATH = "/resources/textures/outlined/";
    public static final String MASK_PATH = "/resources/masks/";
    public static final String WORLD_PATH = "src/resources/worlds/";
    public static final String SOUND_PATH = "src/resources/sounds/";
    public static final String DOC_PATH = "doc/";
    public static final String LIB_PATH = "lib/native/";
    public static final String SHADER_PATH = "src/graphics/GPURenderer/shaders/";

    public static BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(World.class.getResource(PathManager.TEXTURE_PATH + filePath));
        } catch (Exception e) {
            System.out.println("Error loading : "+filePath);
            e.printStackTrace();
        }
        return null;
    }

    public static Data loadSound(String path) {
        try {
            File file = new File(SOUND_PATH + path);

            if (!file.exists()) {
                System.err.println("Sound not found: " + file.getAbsolutePath());
                return null;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(file);

            AudioFormat format = ais.getFormat();
            byte[] bytes = ais.readAllBytes();

            ais.close();

            return new Data(bytes, format);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public record Data(byte[] bytes, AudioFormat format) {}
}
