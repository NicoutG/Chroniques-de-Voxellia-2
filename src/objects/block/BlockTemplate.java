package objects.block;

import org.w3c.dom.Text;

import graphics.Texture;
import graphics.ligth.*;
import graphics.shape.*;
import objects.collision.*;
import objects.property.*;
import tools.PathManager;

public enum BlockTemplate {
    BLUE(new BlockType("blueBlock", "blue-block.png")),
    RED(new BlockType("redBlock", "red-block.png")),
    PURPLE(new BlockType("purpleBlock", "purple-block.png")),
    FIRE_CAMP(new BlockType("fireCamp", 
        new Texture[] {BlockType.createBasicTexture(new String[] { "fire-0.png", "fire-1.png", "fire-2.png" }, 1)}, 
        0,
        null,
        new Property[] {new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.2), 1.2, 0.8, 0.1)), new Property("noCollision")}, 
        null
        )),
    BLUE_STAIRS_RIGHT(new BlockType("blueStairsRight",
        new Texture[] {new Texture(new Rectangle(),PathManager.loadImage("blue-stairs-right.png"))},
        new Collision[] {CollisionList.STEP_RIGHT},
        null,
        null
        )),
    LAVA(new BlockType("lava",
        new Texture[] {new Texture(new Rectangle(),PathManager.loadImage("lava.png"))},
        0,
        null,
        new Property[] {new PropertyLight(new LightSource(new ColorRGB(1, 0.6, 0.4), 1.5, 0.5, 0.15))},
        null
        )),

    ;

    private static BlockType loadSunBlock() {
        BlockType blockType = new BlockType("sun");
        ColorRGB[] pulse = {
                new ColorRGB(1, 1, 1), // plain day (white)
                new ColorRGB(1, 1, 1), // (white)
                new ColorRGB(1, 1, 1),
                new ColorRGB(1, 1, 1),
                new ColorRGB(1, 0.9, 0.75),
                new ColorRGB(1, 0.5, 0.3), // evening (orange-pink)
                new ColorRGB(0.6, 0.3, 0.35),
                new ColorRGB(0.2, 0.2, 0.4), // night (low blue-white)
                new ColorRGB(0.2, 0.2, 0.4),
                new ColorRGB(0.2, 0.2, 0.4),
                new ColorRGB(0.2, 0.2, 0.4),
                new ColorRGB(0.6, 0.3, 0.35),
                new ColorRGB(0.8, 0.3, 0.3), // morning (reddish)
                new ColorRGB(1, 0.9, 0.75),
        };
        LightSource light = new LightSource(pulse, 1200, 0.75, 1, 0);
        Property propLight = new PropertyLight(light);
        blockType.addProperty(propLight);
        return blockType;
    }

    public final BlockType blockType;

    BlockTemplate(BlockType blockType) {
        this.blockType = blockType;
    }
}
