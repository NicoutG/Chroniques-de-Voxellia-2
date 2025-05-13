package graphics.shape;

import java.awt.image.BufferedImage;
import java.io.IOException;

public final class CustomShape extends Shape {

    /* ------------------------------------------------------------------
     *  Option 1 – take already-loaded images
     * ------------------------------------------------------------------ */
    public CustomShape(BufferedImage leftMask,
                       BufferedImage rightMask,
                       BufferedImage topMask) {

        super(leftMask, rightMask, topMask);
    }

    /* ------------------------------------------------------------------
     *  Option 2 – take resource paths
     * ------------------------------------------------------------------ */
    public CustomShape(String leftResource,
                       String rightResource,
                       String topResource) throws IOException {

        super(Cache.get(leftResource),
              Cache.get(rightResource),
              Cache.get(topResource));
    }

    /* ---------- Tiny (thread-safe) one-liner cache ---------- */
    private static final class Cache {
        private Cache() { }

        private static final java.util.Map<String, BufferedImage> MAP =
                new java.util.concurrent.ConcurrentHashMap<>();

        static BufferedImage get(String resource) throws IOException {
            return MAP.computeIfAbsent(resource, r -> {
                try {
                    return loadMask(r);          // Utility in Shape
                } catch (IOException ex) {
                    // convert to unchecked so computeIfAbsent can rethrow
                    throw new RuntimeException(ex);
                }
            });
        }
    }
}
