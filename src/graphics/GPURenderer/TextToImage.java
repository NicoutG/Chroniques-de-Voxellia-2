package graphics.GPURenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TextToImage {
    private static final Font PIXEL_FONT;

    static {
        Font f;
        try (InputStream is = GPURenderer.class.getResourceAsStream("/resources/fonts/basis33.ttf")) {
            f = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
        } catch (FontFormatException | IOException e) {
            f = new Font("Monospaced", Font.PLAIN, 24);
        }
        PIXEL_FONT = f.deriveFont(Font.PLAIN, 36f);
    }

    public static void init() {
        
    }

    /**
     * Crée un BufferedImage à partir du texte, avec contour noir et intérieur blanc semi-transparent.
     */
    public static BufferedImage createTextImage(String text) {
        // 1. Mesurer le texte
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tmp.createGraphics();
        g2.setFont(PIXEL_FONT);
        FontMetrics fm = g2.getFontMetrics();
        int width = fm.stringWidth(text) + 4;  // marge pour contour
        int height = fm.getHeight() + 4;
        g2.dispose();

        // 2. Créer l'image finale
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setFont(PIXEL_FONT);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int x = 2; // offset pour contour
        int y = fm.getAscent() + 2;

        // 3. Dessiner le contour noir (4 directions)
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(text, x - 2, y);
        g2.drawString(text, x + 2, y);
        g2.drawString(text, x, y - 2);
        g2.drawString(text, x, y + 2);

        // 4. Dessiner l'intérieur blanc semi-transparent
        g2.setColor(new Color(255, 255, 255, 240));
        g2.drawString(text, x, y);

        g2.dispose();
        return image;
    }
}
