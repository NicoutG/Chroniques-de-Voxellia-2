package graphics.variantTexture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.Texture;
import graphics.shape.Shape;
import objects.entity.EntityType;
import objects.entity.entityBehavior.EntityBehavior;
import objects.collision.Collision;
import objects.property.Property;

public class VariantEntityMaker extends VariantTextureMaker {

    public static EntityType createEntityType(String name, Texture texture, Property[] properties, EntityBehavior[] behaviors, Shape ... variants) {
        Texture[] textures = createTextures(texture, variants);
        Collision[] collisions = getCollisions(textures);
        EntityType blockType = new EntityType(name, textures, collisions, properties, behaviors);
        return blockType;
    }

    public static EntityType createEntityType(String name, Texture texture, Property[] properties, EntityBehavior[] behaviors, Shape[]... variants)
    {
        List<Shape> shapes = new ArrayList<>();
        for (Shape[] arr : variants)
            shapes.addAll(Arrays.asList(arr));

        return createEntityType(name, texture, properties, behaviors, shapes.toArray(new Shape[0]));
    }

    public static EntityType createEntityType(String name, Texture texture, Shape ... variants) {
        return createEntityType(name, texture, null, null, variants);
    }

    public static EntityType createEntityType(String name, Texture texture, Shape[]... variants) {
        return createEntityType(name, texture, null, null, variants);
    }

    public static EntityType createEntityType(String name, String texturePath, Property[] properties, EntityBehavior[] behaviors, Shape ... variants) {
        return createEntityType(name, Texture.createBasicTexture(texturePath), properties, behaviors, variants);
    }

    public static EntityType createEntityType(String name, String texturePath, Property[] properties, EntityBehavior[] behaviors, Shape[]... variants) {
        return createEntityType(name, Texture.createBasicTexture(texturePath), properties, behaviors, variants);
    }

    public static EntityType createEntityType(String name, String texturePath, Shape ... variants) {
        return createEntityType(name, texturePath, null, null, variants);
    }

    public static EntityType createEntityType(String name, String texturePath, Shape[]... variants) {
        return createEntityType(name, texturePath, null, null, variants);
    }
}