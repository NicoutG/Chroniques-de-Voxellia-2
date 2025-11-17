package objects.collision;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import tools.*;;

public class Collision {

    public double[] getBounds(Vector position) {
        return new double[] {position.x - 0.5, position.x + 0.5, position.y - 0.5, position.y + 0.5, position.z - 0.5, position.z + 0.5};
    }

    public double getPourcentage2In1(Vector position1, Collision collision, Vector position2) {
        double[] bounds1 = getBounds(position1);
        double[] bounds2 = collision.getBounds(position2);
        double xOverlap = Math.max(0, Math.min(bounds1[1], bounds2[1]) - Math.max(bounds1[0], bounds2[0]));
        double yOverlap = Math.max(0, Math.min(bounds1[3], bounds2[3]) - Math.max(bounds1[2], bounds2[2]));
        double zOverlap = Math.max(0, Math.min(bounds1[5], bounds2[5]) - Math.max(bounds1[4], bounds2[4]));

        double overlapVolume = xOverlap * yOverlap * zOverlap;

        double bounds2Volume = 
            Math.max(0, bounds2[1] - bounds2[0]) *
            Math.max(0, bounds2[3] - bounds2[2]) *
            Math.max(0, bounds2[5] - bounds2[4]);

        if (bounds2Volume == 0) return 0;

        return overlapVolume / bounds2Volume;
    }

    public boolean collision(Vector position1, Collision collision, Vector position2) {
        if (collision instanceof ComplexCollision)
            return complexCollision(position1, (ComplexCollision)collision, position2);
        double[] bounds1 = getBounds(position1);
        double[] bounds2 = collision.getBounds(position2);
        if (bounds1[0]>=bounds2[1] || bounds1[1]<=bounds2[0] || bounds1[2]>=bounds2[3] || bounds1[3]<=bounds2[2] || bounds1[4]>=bounds2[5] || bounds1[5]<=bounds2[4])
            return false;
        return true;
    }

    public boolean complexCollision(Vector position1, ComplexCollision collision, Vector position2) {
        for (BoundingCollision collision2 : collision.getCollisions()) {
            if (collision(position1, collision2, position2))
                return true;
        }
        return false;
    }

    public BufferedImage getImage(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, size, size);
        g.setComposite(AlphaComposite.SrcOver);

        g.setColor(Color.BLACK);

        double[] bounds = getBounds(new Vector(0, 0, 0));
        
        double xMin = bounds[0], xMax = bounds[1];
        double yMin = bounds[2], yMax = bounds[3];
        double zMin = bounds[4], zMax = bounds[5];

        double[][] corners = new double[][] {
            {xMin, yMin, zMin},
            {xMax, yMin, zMin},
            {xMax, yMax, zMin},
            {xMin, yMax, zMin},
            {xMin, yMin, zMax},
            {xMax, yMin, zMax},
            {xMax, yMax, zMax},
            {xMin, yMax, zMax},
        };

        double sqrt3 = Math.sqrt(3)/2;
        double[] isoXArr = new double[8];
        double[] isoYArr = new double[8];

        double xMinIso = -1, xMaxIso = 1;
        double yMinIso = -1, yMaxIso = 1;

        for (int i = 0; i < 8; i++) {
            double x = corners[i][0];
            double y = corners[i][1];
            double z = corners[i][2];

            double isoX = (x - y) * sqrt3;
            double isoY = z - (x + y)/2;

            isoXArr[i] = isoX;
            isoYArr[i] = isoY;
        }

        int[][] projected = new int[8][2];
        for (int i = 0; i < 8; i++) {
            int px = (int) Math.round((isoXArr[i] - xMinIso) / (xMaxIso - xMinIso) * (size - 1));
            int py = (int) Math.round((yMaxIso - isoYArr[i]) / (yMaxIso - yMinIso) * (size - 1));
            projected[i][0] = px;
            projected[i][1] = py;
        }

        int[][] edges = new int[][] {
            {1,2},{2,3}, // bottom
            {4,5},{5,6},{6,7},{7,4}, // up
            {1,5},{2,6},{3,7}  // vertical
        };

        for (int[] e : edges) {
            int x0 = projected[e[0]][0];
            int y0 = projected[e[0]][1];
            int x1 = projected[e[1]][0];
            int y1 = projected[e[1]][1];
            g.drawLine(x0, y0, x1, y1);
        }

        g.dispose();
        return image;
    }


}
