package graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Texture isométrique 64×64 :
 * • Peut contenir 1 ou N frames (animation bouclée).
 * • Pré-découpe chaque frame en trois faces (gauche / dessus / droite) via masques.
 * • Aucune image rechargée deux fois : les masques sont statiques et
 *   les faces sont calculées une seule fois dans le constructeur.
 */
public final class Texture {

    /* ----------- masques statiques partagés (une seule fois) ------------- */
    private static final BufferedImage LEFT_MASK  = loadMask("/resource/mask/mask-left.png");
    private static final BufferedImage TOP_MASK   = loadMask("/resource/mask/mask-top.png");
    private static final BufferedImage RIGHT_MASK = loadMask("/resource/mask/mask-right.png");

    /* ----------- frames & faces pré-calculées ---------------------------- */
    private final BufferedImage[] full;          // frame complète
    private final BufferedImage[][] faces;       // faces[frameIdx][0=left,1=top,2=right]

    private final int ticksPerFrame;             // vitesse d’anim (1 = 60 fps, 4 = 15 fps, …)

    /* =====================  CONSTRUCTEURS  =============================== */

    /** Texture statique (1 seule frame, pas d’animation). */
    public Texture(BufferedImage img) {
        this(new BufferedImage[]{img}, Integer.MAX_VALUE);
    }

    /**
     * Texture animée.
     *
     * @param frames         tableau de frames (64×64)
     * @param ticksPerFrame  nbre de ticks entre 2 frames (GamePanel.tick)
     */
    public Texture(BufferedImage[] frames, int ticksPerFrame) {
        Objects.requireNonNull(frames);
        if (frames.length == 0) throw new IllegalArgumentException("frames.length == 0");

        this.full = frames.clone();
        this.ticksPerFrame = Math.max(1, ticksPerFrame);

        // pré-découpe toutes les faces une seule fois
        faces = new BufferedImage[frames.length][3];
        for (int i = 0; i < frames.length; i++) {
            faces[i][0] = applyMask(frames[i], LEFT_MASK);
            faces[i][1] = applyMask(frames[i], TOP_MASK);
            faces[i][2] = applyMask(frames[i], RIGHT_MASK);
        }
    }

    /* =====================  ACCÈS RUNTIME  =============================== */

    private int frameIndex(long tick) {
        return (int) ((tick / ticksPerFrame) % full.length);
    }

    /** Frame complète adaptée au tick courant. */
    public BufferedImage full(long tick) {
        return full[frameIndex(tick)];
    }

    /** Faces individuelles (left/top/right) adaptées au tick courant. */
    public BufferedImage left(long tick)  { return faces[frameIndex(tick)][0]; }
    public BufferedImage top(long tick)   { return faces[frameIndex(tick)][1]; }
    public BufferedImage right(long tick) { return faces[frameIndex(tick)][2]; }

    /* ====================  HELPERS PRIVÉS  =============================== */

    private static BufferedImage loadMask(String path) {
        try {
            return ImageIO.read(
                    Objects.requireNonNull(Texture.class.getResource(path),
                            () -> "Missing resource " + path));
        } catch (IOException e) {
            throw new IllegalStateException("Can't load mask " + path, e);
        }
    }

    private static BufferedImage applyMask(BufferedImage tex, BufferedImage mask) {
        int w = tex.getWidth(), h = tex.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        int[] tp = tex.getRGB(0, 0, w, h, null, 0, w);
        int[] mp = mask.getRGB(0, 0, w, h, null, 0, w);
        for (int i = 0; i < tp.length; i++) {
            out.setRGB(i % w, i / w, (mp[i] >>> 24 == 0) ? 0 : tp[i]);
        }
        return out;
    }
}
