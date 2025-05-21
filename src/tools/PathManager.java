package tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import world.World;

public class PathManager {
    public static final String TEXTURE_PATH = "/resources/textures/outlined/";
    public static final String MASK_PATH = "/resources/masks/";
    public static final String WORLD_PATH = "src/resources/worlds/";

    public static BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(World.class.getResource(PathManager.TEXTURE_PATH + filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
