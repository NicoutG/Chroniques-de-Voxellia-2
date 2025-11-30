package graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.shape.Shape;
import objects.block.BlockType;
import objects.block.blockBehavior.BlockBehavior;
import objects.block.blockBehavior.BlockBehaviorVariant;
import objects.collision.Collision;
import objects.collision.CollisionList;
import objects.property.Property;

public class VariantTextureMaker {
    private final static int[] LEFT_TEXTURE_MAP = {
        166,113,114,115,116,117,230,177,178,179,180,181,182,129,130,131,
        182,129,130,131,132,133,134,81,194,195,196,197,198,145,146,147,
        198,145,146,147,148,149,150,97,98,99,212,213,214,161,162,163,
        214,161,162,163,164,165,166,113,114,115,116,117,230,177,178,179,
        230,177,178,179,180,181,182,129,130,131,132,133,134,81,194,195,
        134,81,194,195,196,197,198,145,146,147,148,149,150,97,98,99,
        150,97,98,99,212,213,214,161,162,163,164,165,166,113,114,115,
        166,113,114,115,116,117,230,177,178,179,180,181,182,129,130,131,
        182,129,130,131,132,133,134,81,194,195,196,197,198,145,146,147,
        198,145,146,147,148,149,150,97,98,99,212,213,214,161,162,163,
        214,161,162,163,164,165,166,113,114,115,116,117,230,177,178,179,
        230,177,178,179,180,181,182,129,130,131,132,133,134,81,194,195,
        134,81,194,195,196,197,198,145,146,147,148,149,150,97,98,99,
        150,97,98,99,212,213,214,161,162,163,164,165,166,113,114,115,
        166,113,114,115,116,117,230,177,178,179,180,181,182,129,130,131,
        182,129,130,131,132,133,134,81,194,195,196,197,198,145,146,147
    };


    private final static int[] RIGHT_TEXTURE_MAP = {
        140,141,142,185,186,187,188,189,190,233,122,123,124,125,126,169,
        156,157,158,201,202,203,204,205,94,137,138,139,140,141,142,185,
        172,173,174,217,218,219,108,109,110,153,154,155,156,157,158,201,
        188,189,190,233,122,123,124,125,126,169,170,171,172,173,174,217,
        204,205,94,137,138,139,140,141,142,185,186,187,188,189,190,233,
        108,109,110,153,154,155,156,157,158,201,202,203,204,205,94,137,
        124,125,126,169,170,171,172,173,174,217,218,219,108,109,110,153,
        140,141,142,185,186,187,188,189,190,233,122,123,124,125,126,169,
        156,157,158,201,202,203,204,205,94,137,138,139,140,141,142,185,
        172,173,174,217,218,219,108,109,110,153,154,155,156,157,158,201,
        188,189,190,233,122,123,124,125,126,169,170,171,172,173,174,217,
        204,205,94,137,138,139,140,141,142,185,186,187,188,189,190,233,
        108,109,110,153,154,155,156,157,158,201,202,203,204,205,94,137,
        124,125,126,169,170,171,172,173,174,217,218,219,108,109,110,153,
        140,141,142,185,186,187,188,189,190,233,122,123,124,125,126,169,
        156,157,158,201,202,203,204,205,94,137,138,139,140,141,142,185
    };

    private final static int[] TOP_TEXTURE_MAP = {
        54,  55,  56,  57,  58,  59,  60, 103, 104,  51,  52,  53,  54,  55,  56,  57,
        70,  71,  72,  73,  74,  75,  76,  23,  24,  67,  68,  69,  70,  71,  72,  73,
        86,  87,  88,  89,  90,  37,  38,  39,  40,  41,  42,  85,  86,  87,  88,  89,
        60, 103, 104,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60, 103, 104,  51,
        76,  23,  24,  67,  68,  69,  70,  71,  72,  73,  74,  75,  76,  23,  24,  67,
        38,  39,  40,  41,  42,  85,  86,  87,  88,  89,  90,  37,  38,  39,  40,  41,
        54,  55,  56,  57,  58,  59,  60, 103, 104,  51,  52,  53,  54,  55,  56,  57,
        70,  71,  72,  73,  74,  75,  76,  23,  24,  67,  68,  69,  70,  71,  72,  73,
        86,  87,  88,  89,  90,  37,  38,  39,  40,  41,  42,  85,  86,  87,  88,  89,
        60, 103, 104,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60, 103, 104,  51,
        76,  23,  24,  67,  68,  69,  70,  71,  72,  73,  74,  75,  76,  23,  24,  67,
        38,  39,  40,  41,  42,  85,  86,  87,  88,  89,  90,  37,  38,  39,  40,  41,
        54,  55,  56,  57,  58,  59,  60, 103, 104,  51,  52,  53,  54,  55,  56,  57,
        70,  71,  72,  73,  74,  75,  76,  23,  24,  67,  68,  69,  70,  71,  72,  73,
        86,  87,  88,  89,  90,  37,  38,  39,  40,  41,  42,  85,  86,  87,  88,  89,
        60, 103, 104,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60, 103, 104,  51
    };

    public static BufferedImage createVariantTexture(BufferedImage cube, Shape shape) {
        final int w = 16, h = 16;

        // Masks
        BufferedImage maskTop   = shape.getTopMask();
        BufferedImage maskLeft  = shape.getLeftMask();
        BufferedImage maskRight = shape.getRightMask();

        // Output texture
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                int index = y * 16 + x;

                int r = 0, g = 0, b = 0, a = 0;
                int count = 0;

                /* === TOP FACE === */
                if (getAlpha(maskTop.getRGB(x, y)) > 0) {
                    int srcIndex = TOP_TEXTURE_MAP[index];         // pixel index to sample from cube
                    int pixel = getPixelFromCube(cube, srcIndex);
                    r += (pixel >> 16) & 255;
                    g += (pixel >> 8)  & 255;
                    b += pixel & 255;
                    a += (pixel >> 24) & 255;
                    count++;
                }

                /* === LEFT FACE === */
                if (getAlpha(maskLeft.getRGB(x, y)) > 0) {
                    int srcIndex = LEFT_TEXTURE_MAP[index];
                    int pixel = getPixelFromCube(cube, srcIndex);
                    r += (pixel >> 16) & 255;
                    g += (pixel >> 8)  & 255;
                    b += pixel & 255;
                    a += (pixel >> 24) & 255;
                    count++;
                }

                /* === RIGHT FACE === */
                if (getAlpha(maskRight.getRGB(x, y)) > 0) {
                    int srcIndex = RIGHT_TEXTURE_MAP[index];
                    int pixel = getPixelFromCube(cube, srcIndex);
                    r += (pixel >> 16) & 255;
                    g += (pixel >> 8)  & 255;
                    b += pixel & 255;
                    a += (pixel >> 24) & 255;
                    count++;
                }

                // Si aucun mask actif → pixel transparent
                if (count == 0) {
                    out.setRGB(x, y, 0x00000000);
                    continue;
                }

                // Moyenne des couleurs si superposition
                r /= count;
                g /= count;
                b /= count;
                a /= count;

                out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }

        return out;
    }

    public static BufferedImage createTextureFromMap(BufferedImage cube, int[] map) {
        int size = (int) Math.sqrt(map.length); // On suppose un tableau carré
        BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < map.length; i++) {
            int srcIndex = map[i];
            int xSrc = srcIndex % cube.getWidth();
            int ySrc = srcIndex / cube.getWidth();

            int xDst = i % size;
            int yDst = i / size;

            int color = cube.getRGB(xSrc, ySrc);
            result.setRGB(xDst, yDst, color);
        }

        return result;
    }

    public static BlockType createBlockType(String name, Texture texture, Property[] properties, BlockBehavior[] behaviors, Shape ... variants) {
        if (variants == null || variants.length == 0)
            return new BlockType(name, new Texture[]{texture},null, properties, behaviors);

        BufferedImage[] cubes = texture.getAllImages();
        int ticksPerFrame = texture.getTicksPerFrame();
        Texture[] textures = new Texture[variants.length + 1];
        textures[0] = texture;
        Collision[] collisions = new Collision[variants.length + 1];
        collisions[0] = CollisionList.CUBE;
        for (int i = 0; i < variants.length; i++) {
            BufferedImage[] images = new BufferedImage[cubes.length];
            Shape variant = variants[i];
            for (int j = 0; j < cubes.length; j++)
                images[j] = createVariantTexture(cubes[j], variant);
            textures[i + 1] = new Texture(variant, images, ticksPerFrame);
            collisions[i + 1] = ShapeCollisionLink.getCollision(variant);
        }
        BlockType blockType = new BlockType(name, textures, collisions, properties, behaviors);
        blockType.addBehavior(new BlockBehaviorVariant());
        return blockType;
    }

    public static BlockType createBlockType(String name, Texture texture, Property[] properties, BlockBehavior[] behaviors, Shape[]... variants)
    {
        List<Shape> shapes = new ArrayList<>();
        for (Shape[] arr : variants)
            shapes.addAll(Arrays.asList(arr));

        return createBlockType(name, texture, properties, behaviors, shapes.toArray(new Shape[0]));
    }

    public static BlockType createBlockType(String name, Texture texture, Shape ... variants) {
        return createBlockType(name, texture, null, null, variants);
    }

    public static BlockType createBlockType(String name, Texture texture, Shape[]... variants) {
        return createBlockType(name, texture, null, null, variants);
    }

    public static BlockType createBlockType(String name, String texturePath, Property[] properties, BlockBehavior[] behaviors, Shape ... variants) {
        return createBlockType(name, Texture.createBasicTexture(texturePath), properties, behaviors, variants);
    }

    public static BlockType createBlockType(String name, String texturePath, Property[] properties, BlockBehavior[] behaviors, Shape[]... variants) {
        return createBlockType(name, Texture.createBasicTexture(texturePath), properties, behaviors, variants);
    }

    public static BlockType createBlockType(String name, String texturePath, Shape ... variants) {
        return createBlockType(name, texturePath, null, null, variants);
    }

    public static BlockType createBlockType(String name, String texturePath, Shape[]... variants) {
        return createBlockType(name, texturePath, null, null, variants);
    }


    /* ============================================
    * Help Functions
    * ============================================ */

    /** Retourne la transparence (0 → transparent) */
    private static int getAlpha(int argb) {
        return (argb >> 24) & 255;
    }

    /** Retourne un pixel de cube via index 1D */
    private static int getPixelFromCube(BufferedImage cube, int index) {
        int x = index % 16;
        int y = index / 16;
        return cube.getRGB(x, y);
    }
}
