/*  graphics/texture/Texture.java  */
package graphics;

import graphics.shape.Face;
import graphics.shape.Shape;
import graphics.shape.ShapeList;
import tools.PathManager;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Isometric texture (64×64) with an attached {@link Shape}.
 *
 * <p>
 * • Handles one-frame sprites *or* looped animations.<br>
 * • Cuts every frame into three faces (left / top / right) using
 * the masks supplied by the <em>shape</em>.<br>
 * • No mask or face ever recalculated twice.
 * </p>
 */
public final class Texture {

    /* -------------------------- immutable state -------------------------- */
    private final Shape shape; // the geometry
    private final BufferedImage[] full; // original frames
    private final BufferedImage[][] faces; // [frame][Face.index]
    private final int ticksPerFrame;

    /* =========================== CTORS ================================= */

    public static Texture createBasicTexture(String texturePath) {
        return new Texture(ShapeList.CUBE, PathManager.loadImage(texturePath));
    }

    public static Texture createBasicTexture(String[] texturePaths, int ticksPerFrame) {
        BufferedImage[] images = new BufferedImage[texturePaths.length];
        for (int i = 0; i < texturePaths.length; i++)
            images[i] = PathManager.loadImage(texturePaths[i]);
        return new Texture(ShapeList.CUBE, images, ticksPerFrame);
    }

    public static Texture createBasicTexture(Shape shape,String[] texturePaths, int ticksPerFrame) {
        BufferedImage[] images = new BufferedImage[texturePaths.length];
        for (int i = 0; i < texturePaths.length; i++)
            images[i] = PathManager.loadImage(texturePaths[i]);
        return new Texture(shape, images, ticksPerFrame);
    }

    /** Static sprite – one frame, no animation. */
    public Texture(Shape shape, BufferedImage frame) {
        this(shape, new BufferedImage[] { frame }, Integer.MAX_VALUE);
    }

    /**
     * Animated sprite.
     *
     * @param shape         geometry that supplies the three masks
     * @param frames        64×64 frames (must be ≥ 1)
     * @param ticksPerFrame game ticks between two frames (≥ 1)
     */
    public Texture(Shape shape, BufferedImage[] frames, int ticksPerFrame) {
        this.shape = Objects.requireNonNull(shape, "shape");
        Objects.requireNonNull(frames, "frames");
        if (frames.length == 0)
            throw new IllegalArgumentException("frames.length == 0");
        if (ticksPerFrame < 1)
            throw new IllegalArgumentException("ticksPerFrame < 1");

        this.full = frames.clone();
        this.ticksPerFrame = ticksPerFrame;

        /* ---- pre-cut all faces exactly once ---- */
        faces = new BufferedImage[frames.length][Face.values().length];
        for (int i = 0; i < frames.length; i++) {
            faces[i][Face.LEFT.index] = applyMask(frames[i], shape.getLeftMask());
            faces[i][Face.TOP.index] = applyMask(frames[i], shape.getTopMask());
            faces[i][Face.RIGHT.index] = applyMask(frames[i], shape.getRightMask());
        }
    }

    /* ======================== RUNTIME ACCESS =========================== */

    private int frameIndex(long tick) {
        return (int) ((tick / ticksPerFrame) % full.length);
    }

    /** Whole 64×64 frame suitable for the given game tick. */
    public BufferedImage full(long tick) {
        return full[frameIndex(tick)];
    }

    /** A single face of the current frame. */
    public BufferedImage face(Face f, long tick) {
        return faces[frameIndex(tick)][f.index];
    }

    public BufferedImage left(long tick) {
        return face(Face.LEFT, tick);
    }

    public BufferedImage top(long tick) {
        return face(Face.TOP, tick);
    }

    public BufferedImage right(long tick) {
        return face(Face.RIGHT, tick);
    }

    public boolean takesFullSpace() {
        return shape.takesFullSpace();
    }

    /* ============================ HELPERS ================================ */

    /** AND-the-alpha of tex with mask. */
    private static BufferedImage applyMask(BufferedImage tex, BufferedImage mask) {

        int w = tex.getWidth(), h = tex.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        int[] tp = tex.getRGB(0, 0, w, h, null, 0, w);
        int[] mp = mask.getRGB(0, 0, w, h, null, 0, w);

        for (int i = 0; i < tp.length; i++) {
            int alpha = (mp[i] >>> 24); // 0-255
            // si le masque n’est pas transparent, on garde la couleur de la texture
            out.setRGB(i % w, i / w,
                    (alpha == 0) ? 0 : tp[i]);
        }
        return out;
    }
}
