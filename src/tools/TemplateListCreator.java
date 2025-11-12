package tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import graphics.shape.Shape;
import graphics.shape.ShapeList;
import objects.block.BlockTemplate;
import objects.block.BlockType;
import objects.collision.Collision;
import objects.collision.CollisionList;
import objects.entity.EntityTemplate;
import objects.entity.EntityType;

public class TemplateListCreator {

    private final static String FOLDER = PathManager.DOC_PATH + "templateLists/";

    public static void main (String ... args) throws Exception {
        createBlockTemplateList(FOLDER+"blocks.html");
        createEntityTemplateList(FOLDER+"entities.html");
        createShapeTemplateList(FOLDER+"shapes.html");
        createCollisionTemplateList(FOLDER+"collisions.html");
    }

    public static void createBlockTemplateList(String path) throws Exception {
        FileWriter writer = new FileWriter(path);

        writer.write("""
        <html><head><meta charset='UTF-8'><title>Block List</title>
        <style>
        table { border-collapse: collapse; }
        td, th { border: 1px solid #888; padding: 6px; text-align: center; }
        img { width: 64px; height: 64px; }
        </style>
        </head><body>
        <h1>Liste des Blocs</h1>
        <table><tr><th>Index</th><th>Nom</th><th>Image</th><th>Shape</th><th>Collision</th></tr>
        """);

        int index = 0;
        for (BlockTemplate bt : BlockTemplate.values()) {
            BlockType block = bt.blockType;
            writer.write("<tr>");
            writer.write("<td>" + index + "</td>");
            writer.write("<td>" + block.getName() + "</td>");
            // Images
            writer.write("<td>");
            if (block.getNbTextures() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else
                for (int i = 0; i < block.getNbTextures(); i++) {
                    BufferedImage img = block.getTexture(i).full(0);
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                }
            writer.write("</td>");
            // Shapes
            writer.write("<td style='background-color: black;'>");
            if (block.getNbTextures() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else
                for (int i = 0; i < block.getNbTextures(); i++) {
                    BufferedImage img = block.getTexture(i).getShape().getGlobalMask();
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                }
            writer.write("</td>");
            // Collisions
            writer.write("<td>");
            if (block.getNbCollisions() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else
                for (int i = 0; i < block.getNbCollisions(); i++) {
                    BufferedImage img = block.getCollision(i).getImage(64);
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                }
            writer.write("</td>");
            writer.write("</tr>");
            index++;
        }

        writer.write("</table></body></html>");
        writer.close();

        System.out.println("Block list created");
    }

    public static void createEntityTemplateList(String path) throws Exception {
        FileWriter writer = new FileWriter(path);

        writer.write("""
        <html><head><meta charset='UTF-8'><title>Entity List</title>
        <style>
        table { border-collapse: collapse; }
        td, th { border: 1px solid #888; padding: 6px; text-align: center; }
        img { width: 64px; height: 64px; }
        </style>
        </head><body>
        <h1>Liste des Entités</h1>
        <table><tr><th>Index</th><th>Nom</th><th>Image</th><th>Shape</th><th>Collision</th></tr>
        """);

        int index = 0;
        for (EntityTemplate et : EntityTemplate.values()) {
            EntityType entity = et.entityType;
            writer.write("<tr>");
            writer.write("<td>" + index + "</td>");
            writer.write("<td>" + entity.getName() + "</td>");
            // Images
            writer.write("<td>");
            if (entity.getNbTextures() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else
                for (int i = 0; i < entity.getNbTextures(); i++) {
                    BufferedImage img = entity.getTexture(i).full(0);
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                }
            writer.write("</td>");
            // Shapes
            writer.write("<td style='background-color: black;'>");
            if (entity.getNbTextures() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else
                for (int i = 0; i < entity.getNbTextures(); i++) {
                    BufferedImage img = entity.getTexture(i).getShape().getGlobalMask();
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                }
            writer.write("</td>");
            // Collisions
            writer.write("<td>");
            if (entity.getNbCollisions() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else
                for (int i = 0; i < entity.getNbCollisions(); i++) {
                    BufferedImage img = entity.getCollision(i).getImage(64);
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                }
            writer.write("</td>");
            writer.write("</tr>");
            index++;
        }

        writer.write("</table></body></html>");
        writer.close();

        System.out.println("Entity list created");
    }

    public static void createShapeTemplateList(String path) throws Exception {
        FileWriter writer = new FileWriter(path);

        writer.write("""
        <html><head><meta charset='UTF-8'><title>Shape List</title>
        <style>
        table { border-collapse: collapse; }
        td, th { border: 1px solid #888; padding: 6px; text-align: center; }
        img { width: 64px; height: 64px; }
        </style>
        </head><body>
        <h1>Liste des Shapes</h1>
        <table><tr><th>Nom</th><th>Global</th><th>Top</th><th>Left</th><th>Right</th></tr>
        """);

        List<Shape> shapes = ShapeList.getAllShapes();
        List<String> shapeNames = ShapeList.getAllShapeNames();
        for (int i = 0; i < shapes.size(); i++) {
            Shape shape = shapes.get(i);
            String name = shapeNames.get(i);
            String base64Top = imageToBase64(shape.getTopMask());
            String base64Left = imageToBase64(shape.getLeftMask());
            String base64Right = imageToBase64(shape.getRightMask());
            String base64Global = imageToBase64(shape.getGlobalMask());
            writer.write("<tr>");
            writer.write("<td>" + name + "</td>");
            writer.write("<td style='background-color: black;'><img src='data:image/png;base64," + base64Global + "'></td>");
            writer.write("<td><img src='data:image/png;base64," + base64Top + "'></td>");
            writer.write("<td><img src='data:image/png;base64," + base64Left + "'></td>");
            writer.write("<td><img src='data:image/png;base64," + base64Right + "'></td>");
            writer.write("</tr>");
        }

        writer.write("</table></body></html>");
        writer.close();

        System.out.println("Shape List created");
    }

    public static void createCollisionTemplateList(String path) throws Exception {
        FileWriter writer = new FileWriter(path);

        writer.write("""
        <html><head><meta charset='UTF-8'><title>Collision List</title>
        <style>
        table { border-collapse: collapse; }
        td, th { border: 1px solid #888; padding: 6px; text-align: center; }
        img { width: 64px; height: 64px; }
        </style>
        </head><body>
        <h1>Liste des Collisions</h1>
        <table><tr><th>Nom</th><th>Image</th></tr>
        """);

        List<Collision> collisions = CollisionList.getAllCollisions();
        List<String> collisionNames = CollisionList.getAllCollisionNames();
        for (int i = 0; i < collisions.size(); i++) {
            Collision collision = collisions.get(i);
            String name = collisionNames.get(i);
            String base64 = imageToBase64(collision.getImage(64));
            writer.write("<tr>");
            writer.write("<td>" + name + "</td>");
            writer.write("<td><img src='data:image/png;base64," + base64 + "'></td>");
            writer.write("</tr>");
        }

        writer.write("</table></body></html>");
        writer.close();

        System.out.println("Collision List created");
    }

    private static String imageToBase64(BufferedImage image) throws IOException {
        if (image == null) {
            // crée une petite image 64x64 transparente ou grise
            image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            var g = image.createGraphics();
            g.setColor(new java.awt.Color(200, 200, 200, 255)); // gris clair
            g.fillRect(0, 0, 64, 64);
            g.setColor(java.awt.Color.DARK_GRAY);
            g.drawString("N/A", 20, 35); // texte de secours
            g.dispose();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
