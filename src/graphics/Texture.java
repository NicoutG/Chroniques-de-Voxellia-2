/*  graphics/texture/Texture.java  */
package graphics;

import graphics.ligth.ColorRGB;
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
            faces[i] = applyMasks(frames[i], shape.getLeftMask(), shape.getRightMask(), shape.getTopMask());
            // faces[i][Face.LEFT.index] = applyMask(frames[i], shape.getLeftMask());
            // faces[i][Face.TOP.index] = applyMask(frames[i], shape.getTopMask());
            // faces[i][Face.RIGHT.index] = applyMask(frames[i], shape.getRightMask());
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

    public Shape getShape() {
        return shape;
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
    private static BufferedImage[] applyMasks(BufferedImage tex, BufferedImage maskLeft, BufferedImage maskRight, BufferedImage maskTop) {

        int w = tex.getWidth(), h = tex.getHeight();
        BufferedImage[] out = new BufferedImage[3];
        for (int i = 0; i < out.length; i++)
            out[i] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        int[] tp = tex.getRGB(0, 0, w, h, null, 0, w);
        int[] mpLeft = maskLeft.getRGB(0, 0, w, h, null, 0, w);
        int[] mpRight = maskRight.getRGB(0, 0, w, h, null, 0, w);
        int[] mpTop = maskTop.getRGB(0, 0, w, h, null, 0, w);

        for (int i = 0; i < tp.length; i++) {
            int alpha = (mpLeft[i] >>> 24); // 0-255
            // si le masque n’est pas transparent, on garde la couleur de la texture
            if (alpha != 0) {
                out[Face.LEFT.index].setRGB(i % w, i / w, tp[i]);
                continue;
            }
            alpha = (mpRight[i] >>> 24); // 0-255
            if (alpha != 0) {
                out[Face.RIGHT.index].setRGB(i % w, i / w, tp[i]);
                continue;
            }
            alpha = (mpTop[i] >>> 24); // 0-255
            if (alpha != 0) {
                out[Face.TOP.index].setRGB(i % w, i / w, tp[i]);
                continue;
            }
        }

        return out;
    }

    public BufferedImage shade(BufferedImage src, ColorRGB cLeft, ColorRGB cRight, ColorRGB cTop) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        int[] mpLeft = shape.getLeftMask().getRGB(0, 0, w, h, null, 0, w);
        int[] mpRight = shape.getRightMask().getRGB(0, 0, w, h, null, 0, w);
        int[] mpTop = shape.getTopMask().getRGB(0, 0, w, h, null, 0, w);

        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                int index = y * w + x;

                int argb = src.getRGB(x, y);
                int a = argb >>> 24;

                if (a != 0) {
                    int r0 = (argb >> 16) & 0xFF;
                    int g0 = (argb >> 8) & 0xFF;
                    int b0 = argb & 0xFF;

                    int count = 0;
                    double rSum = 0, gSum = 0, bSum = 0;

                    if ((mpLeft[index] >>> 24) != 0) {
                        count++;
                        rSum += cLeft.r();
                        gSum += cLeft.g();
                        bSum += cLeft.b();
                    }
                    if ((mpRight[index] >>> 24) != 0) {
                        count++;
                        rSum += cRight.r();
                        gSum += cRight.g();
                        bSum += cRight.b();
                    }
                    if ((mpTop[index] >>> 24) != 0) {
                        count++;
                        rSum += cTop.r();
                        gSum += cTop.g();
                        bSum += cTop.b();
                    }

                    if (count == 0) {
                        dst.setRGB(x, y, argb);
                        continue;
                    }
                    else {
                        int r = Math.min(255, (int) (r0 * (1.0 * rSum / count)));
                        int g = Math.min(255, (int) (g0 * (1.0 * gSum / count)));
                        int b = Math.min(255, (int) (b0 * (1.0 * bSum / count)));

                        dst.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                    }
                }
            }
        }

        return dst;
    }

    public BufferedImage shade(BufferedImage src, ColorRGB c) {
        BufferedImage dst = new BufferedImage(src.getWidth(),
                src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < src.getHeight(); ++y)
            for (int x = 0; x < src.getWidth(); ++x) {
                int argb = src.getRGB(x, y);
                int a = argb >>> 24;
                int r = (int) (((argb >> 16) & 0xFF) * c.r());
                int g = (int) (((argb >> 8) & 0xFF) * c.g());
                int b = (int) (((argb) & 0xFF) * c.b());
                dst.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        return dst;
    }
}
