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
import objects.block.Block;
import objects.block.BlockTemplate;
import objects.block.BlockType;
import objects.collision.Collision;
import objects.collision.CollisionList;
import objects.entity.Entity;
import objects.entity.EntityTemplate;
import objects.entity.EntityType;

public class TemplateListCreator {

    private final static String FOLDER = PathManager.DOC_PATH + "templateLists/";

    private final static int NB_IMAGES_PER_LINE = 8;

    public static void main (String ... args) throws Exception {
        createBlockTemplateList(FOLDER+"blocks.html");
        createEntityTemplateList(FOLDER+"entities.html");
        createShapeTemplateList(FOLDER+"shapes.html");
        createCollisionTemplateList(FOLDER+"collisions.html");
    }

    // ------------------ BLOCKS ------------------
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
        <h1>Block List</h1>
        <table><tr><th>Index</th><th>Name</th><th>Images</th><th>Shapes</th><th>Collisions</th><th>States</th></tr>
        """);

        int index = 0;
        for (BlockTemplate bt : BlockTemplate.values()) {
            BlockType block = bt.blockType;
            writer.write("<tr "+ ((index%2 != 0) ? "style='background-color: lightgray;'" : "") +">");
            writer.write("<td>" + index + "</td>");
            writer.write("<td>" + block.getName() + "</td>");
            // Images
            writer.write("<td>");
            int count = 0;
            if (block.getNbTextures() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else {
                for (int i = 0; i < block.getNbTextures(); i++) {
                    BufferedImage img = block.getTexture(i).full(0);
                    String base64 = imageToBase64(img);
                    String fileName = block.getName() + "_texture_" + i + ".png";
                    writer.write("<a href='data:image/png;base64," + base64 + "' download='" + fileName + "'>"
                            + "<img src='data:image/png;base64," + base64 + "'>"
                            + "</a>");
                    count++;
                    if (count % NB_IMAGES_PER_LINE == 0) writer.write("<br>"); // retour à la ligne toutes les 8 images
                }
            }
            writer.write("</td>");
            // Shapes
            writer.write("<td style='background-color: black;'>");
            count = 0;
            if (block.getNbTextures() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else {
                for (int i = 0; i < block.getNbTextures(); i++) {
                    BufferedImage img = block.getTexture(i).getShape().getGlobalMask();
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                    count++;
                    if (count % NB_IMAGES_PER_LINE == 0) writer.write("<br>");
                }
            }
            writer.write("</td>");
            // Collisions
            writer.write("<td>");
            count = 0;
            if (block.getNbCollisions() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else {
                for (int i = 0; i < block.getNbCollisions(); i++) {
                    BufferedImage img = block.getCollision(i).getImage(64);
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                    count++;
                    if (count % NB_IMAGES_PER_LINE == 0) writer.write("<br>");
                }
            }
            writer.write("</td>");
            // States
            writer.write("<td>");
            Block blockInstance = block.getInstance();
            for (String state : blockInstance.getAllStateNames()) {
                Object value = blockInstance.getState(state);
                writer.write("- "+state+" : "+ ((value != null) ? value.getClass().getSimpleName() : "null") + "</br>");
            }
            writer.write("</td>");
            writer.write("</tr>\n");
            index++;
        }

        writer.write("</table></body></html>");
        writer.close();

        System.out.println("Block list created");
    }

    // ------------------ ENTITIES ------------------
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
        <h1>Entity List</h1>
        <table><tr><th>Index</th><th>Name</th><th>Images</th><th>Shapes</th><th>Collisions</th><th>States</th></tr>
        """);

        int index = 0;
        for (EntityTemplate et : EntityTemplate.values()) {
            EntityType entity = et.entityType;
            writer.write("<tr "+ ((index%2 != 0) ? "style='background-color: lightgray;'" : "") +">");
            writer.write("<td>" + index + "</td>");
            writer.write("<td>" + entity.getName() + "</td>");
            // Images
            writer.write("<td>");
            int count = 0;
            if (entity.getNbTextures() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else {
                for (int i = 0; i < entity.getNbTextures(); i++) {
                    BufferedImage img = entity.getTexture(i).full(0);
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                    count++;
                    if (count % NB_IMAGES_PER_LINE == 0) writer.write("<br>");
                }
            }
            writer.write("</td>");
            // Shapes
            writer.write("<td style='background-color: black;'>");
            count = 0;
            if (entity.getNbTextures() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else {
                for (int i = 0; i < entity.getNbTextures(); i++) {
                    BufferedImage img = entity.getTexture(i).getShape().getGlobalMask();
                    String base64 = imageToBase64(img);
                    String fileName = entity.getName() + "_texture_" + i + ".png";
                    writer.write("<a href='data:image/png;base64," + base64 + "' download='" + fileName + "'>"
                            + "<img src='data:image/png;base64," + base64 + "'>"
                            + "</a>");
                    count++;
                    if (count % NB_IMAGES_PER_LINE == 0) writer.write("<br>");
                }
            }
            writer.write("</td>");
            // Collisions
            writer.write("<td>");
            count = 0;
            if (entity.getNbCollisions() == 0) {
                String base64 = imageToBase64(null);
                writer.write("<img src='data:image/png;base64," + base64 + "'>");
            }
            else {
                for (int i = 0; i < entity.getNbCollisions(); i++) {
                    BufferedImage img = entity.getCollision(i).getImage(64);
                    String base64 = imageToBase64(img);
                    writer.write("<img src='data:image/png;base64," + base64 + "'>");
                    count++;
                    if (count % NB_IMAGES_PER_LINE == 0) writer.write("<br>");
                }
            }
            writer.write("</td>");
            // States
            writer.write("<td>");
            Entity entityInstance = entity.getInstance();
            for (String state : entityInstance.getAllStateNames()) {
                Object value = entityInstance.getState(state);
                writer.write("- "+state+" : "+ ((value != null) ? value.getClass().getSimpleName() : "null") + "</br>");
            }
            writer.write("</td>");
            writer.write("</tr>\n");
            index++;
        }

        writer.write("</table></body></html>");
        writer.close();

        System.out.println("Entity list created");
    }

    // ------------------ SHAPES ------------------
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
        <h1>Shape List</h1>
        <table><tr><th>Name</th><th>Global</th><th>Top</th><th>Left</th><th>Right</th></tr>
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
            writer.write("<tr "+ ((i%2 != 0) ? "style='background-color: lightgray;'" : "") +">");
            writer.write("<td>" + name + "</td>");
            writer.write("<td style='background-color: black;'><img src='data:image/png;base64," + base64Global + "'></td>");
            writer.write("<td><img src='data:image/png;base64," + base64Top + "'></td>");
            writer.write("<td><img src='data:image/png;base64," + base64Left + "'></td>");
            writer.write("<td><img src='data:image/png;base64," + base64Right + "'></td>");
            writer.write("</tr>\n");
        }

        writer.write("</table></body></html>");
        writer.close();

        System.out.println("Shape List created");
    }

    // ------------------ COLLISIONS ------------------
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
        <h1>Collision List</h1>
        <table><tr><th>Name</th><th>Image</th></tr>
        """);

        List<Collision> collisions = CollisionList.getAllCollisions();
        List<String> collisionNames = CollisionList.getAllCollisionNames();
        for (int i = 0; i < collisions.size(); i++) {
            Collision collision = collisions.get(i);
            String name = collisionNames.get(i);
            String base64 = imageToBase64(collision.getImage(64));
            writer.write("<tr "+ ((i%2 != 0) ? "style='background-color: lightgray;'" : "") +">");
            writer.write("<td>" + name + "</td>");
            writer.write("<td><img src='data:image/png;base64," + base64 + "'></td>");
            writer.write("</tr>\n");
        }

        writer.write("</table></body></html>");
        writer.close();

        System.out.println("Collision List created");
    }

    // ------------------ UTIL ------------------
    private static String imageToBase64(BufferedImage image) throws IOException {
        if (image == null) {
            image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            var g = image.createGraphics();
            g.setColor(new java.awt.Color(200, 200, 200, 255));
            g.fillRect(0, 0, 64, 64);
            g.setColor(java.awt.Color.DARK_GRAY);
            g.drawString("N/A", 20, 35);
            g.dispose();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
