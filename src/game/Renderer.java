package game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Renderer {

    private final World world;
    public static final int TILE_SIZE = 16;
    public static final int DRAW_TILE_SIZE = 64;

    public Renderer(World world) {
        this.world = world;
    }

    public void render(Graphics2D g2, int screenWidth, int screenHeight) {
        Block[][][] blocks = world.getBlocks();
        Entity[] entities = world.getEntities();
        if (blocks == null) return;

        // ---- Trouver le joueur pour centrer ----
        double originX = screenWidth / 2.0;
        double originY = screenHeight / 2.0;

        Entity player = world.getPlayer();
        if (player != null) {
            Point2D p = toScreen(player.getX(), player.getY(), player.getZ());
            originX -= p.getX();
            originY -= p.getY();
        } else {
            originY = screenHeight / 4.0;
        }

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // ---- Fusionner blocs + entités dans une liste triée ----
        List<Drawable> drawables = new ArrayList<>();

        // Blocs
        for (int z = 0; z < blocks[0][0].length; z++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int x = 0; x < blocks.length; x++) {
                    Block b = blocks[x][y][z];
                    if (b == null || b.texture == null) continue;
                    drawables.add(new Drawable(b.texture, x, y, z));
                }
            }
        }

        // Entités
        if (entities != null) {
            for (Entity e : entities) {
                if (e == null || e.getTexture() == null) continue;
                drawables.add(new Drawable(e.getTexture(), e.getX(), e.getY(), e.getZ()));
            }
        }

        // Tri : ordre de profondeur visuel
        drawables.sort(Comparator.comparingDouble(Drawable::getSortKey));

        // Rendu dans l’ordre
        for (Drawable d : drawables) {
            Point2D p = toScreen(d.x, d.y, d.z);
            int drawX = (int) Math.round(originX + p.getX());
            int drawY = (int) Math.round(originY + p.getY());

            g2.drawImage(d.texture,
                    drawX, drawY,
                    drawX + DRAW_TILE_SIZE, drawY + DRAW_TILE_SIZE,
                    0, 0, TILE_SIZE, TILE_SIZE,
                    null);
        }
    }

    private Point2D toScreen(double x, double y, double z) {
        double isoX = (x - y) * (DRAW_TILE_SIZE / 2.0);
        double isoY = (x + y) * (DRAW_TILE_SIZE / 4.0) - z * (DRAW_TILE_SIZE / 2.0);
        return new Point2D.Double(isoX, isoY);
    }

    private static class Drawable {
        final BufferedImage texture;
        final double x, y, z;

        Drawable(BufferedImage texture, double x, double y, double z) {
            this.texture = texture;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        double getSortKey() {
            return x + y + z * 2; // pondère légèrement la hauteur
        }
    }
}
