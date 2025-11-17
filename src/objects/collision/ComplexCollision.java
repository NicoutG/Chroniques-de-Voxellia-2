package objects.collision;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import tools.*;;

public class ComplexCollision extends Collision {
    private ArrayList<BoundingCollision> collisions = new ArrayList<>();

    public ComplexCollision() {

    }

    public ComplexCollision(BoundingCollision ... collisions) {
        for (BoundingCollision collision : collisions)
            addCollision(collision);
    }

    public double[] getBounds(Vector position) {
        double[] bounds = new double[] {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        for (BoundingCollision collision : collisions) {
            double[] boundsCol = collision.getBounds(position);
            for (int i = 0; i < 6; i++)
                if (i % 2 == 0) {
                    if (boundsCol[i] < bounds[i])
                        bounds[i] = boundsCol[i];
                }
                else
                    if (boundsCol[i] > bounds[i])
                        bounds[i] = boundsCol[i];
        }
        return bounds;
    }

    public ArrayList<BoundingCollision> getCollisions() {
        return collisions;
    }

    public void addCollision(BoundingCollision collision) {
        collisions.add(collision);
    }

    @Override
    public boolean collision(Vector position1, Collision collision, Vector position2) {
        if (collision instanceof ComplexCollision)
            return complexCollision(position1, (ComplexCollision)collision, position2);
        for (BoundingCollision collision1 : collisions) {
            if (collision1.collision(position1, collision, position2))
                return true;
        }
        return false;
    }

    @Override
    public boolean complexCollision(Vector position1, ComplexCollision collision, Vector position2) {
        for (BoundingCollision collision1 : collisions) {
            for (BoundingCollision collision2 : collision.getCollisions())
                if (collision1.collision(position1, collision2, position2))
                    return true;
        }
        return false;
    }

    @Override
    public BufferedImage getImage(int size) {
        BufferedImage base = null;
        for (Collision collision : collisions)
            base = mergeImages(base, collision.getImage(size));
        return base;
    }

    private static BufferedImage mergeImages(BufferedImage base, BufferedImage overlay) {
        if (overlay == null)
            return base;
        if (base == null)
            return overlay;
        int width = Math.max(base.getWidth(), overlay.getWidth());
        int height = Math.max(base.getHeight(), overlay.getHeight());

        // Crée une nouvelle image avec canal alpha
        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();

        // Dessine la première image (base)
        g.drawImage(base, 0, 0, null);
        // Dessine la seconde par-dessus (transparence respectée)
        g.drawImage(overlay, 0, 0, null);

        g.dispose();
        return combined;
    }
    
}
