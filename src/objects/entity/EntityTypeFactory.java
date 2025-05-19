package objects.entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import graphics.Texture;
import graphics.shape.Cube;
import graphics.shape.Shape;
import objects.collision.CollisionList;
import tools.PathManager;
import world.World;

public class EntityTypeFactory {

    public static ArrayList<EntityType> loadEntityTypes() {
        ArrayList<EntityType> entityTypes = new ArrayList<>();
        // 0
        entityTypes.add(loadPlayer()); // must be the player
        return entityTypes;
    }

    private static BufferedImage getImage(String filePath) {
        try {
            return ImageIO.read(World.class.getResource(PathManager.TEXTURE_PATH + filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static EntityType createBasicEntityType(String name, String textureName) {
        EntityType entityType = new EntityType(name);
        BufferedImage img = getImage(textureName);
        Shape shape = new Cube();
        Texture text = new Texture(shape, img);
        entityType.addTexture(text);
        return entityType;
    }

    private static EntityType createBasicEntityType(String name, String[] textureNames, int ticksPerFrame) {
        EntityType entityType = new EntityType(name);
        BufferedImage[] images = new BufferedImage[textureNames.length];
        for (int i = 0; i < textureNames.length; i++)
            images[i] = getImage(textureNames[i]);
        Shape shape = new Cube();
        Texture text = new Texture(shape, images, ticksPerFrame);
        entityType.addTexture(text);
        return entityType;
    }

    private static EntityType loadPlayer() {
        EntityType entityType = createBasicEntityType("player", "blue-block.png");
        entityType.addCollision(CollisionList.SMALL_CUBE);
        return entityType;
    }
}
