package tools;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import objects.block.BlockTemplate;
import objects.block.BlockType;

public class BlockJsonExporter {

    private static final String DEFAULT_JSON_OUTPUT = "builder/blocks.json";
    private static final String TEXTURES_FOLDER = "builder/textures";

    public static void main(String[] args) {
        // 1) Determine output file path
        String outputFile = (args.length > 0) ? args[0] : DEFAULT_JSON_OUTPUT;

        try {
            // 2) Load block definitions from your game / registry
            List<BlockDefinition> definitions = loadBlockDefinitionsFromGame();

            // 3) Transform to export model (categories with variants)
            List<BlockCategoryExport> exportData = buildExportModel(definitions);

            // 4) Serialize to JSON
            writeJsonToFile(exportData, outputFile);

            // 5) Export textures (upscaled to 64x64) to builder/textures/
            exportTextures(definitions);

            System.out.println("Block JSON successfully written to: " + Path.of(outputFile).toAbsolutePath());
            System.out.println("Block textures exported to: " + Path.of(TEXTURES_FOLDER).toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error while exporting block JSON / textures: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Uses the same system as TemplateListCreator:
     *   for (BlockTemplate bt : BlockTemplate.values()) {
     *       BlockType block = bt.blockType;
     *   }
     *
     * This method returns ONE BlockDefinition per block *category*.
     *
     * - category_id: we use the loop index (same as "Index" in TemplateListCreator HTML).
     *   If you have a real internal ID (e.g. block.getId()), replace the 'id' assignment here.
     * - baseName: block.getName()
     * - baseImage: "name.png" (you can adapt this convention if needed)
     * - variantCount: max(1, block.getNbTextures())
     *      * If a block has several textures, we treat them as variants.
     *      * If it has 0 textures, we still export 1 variant.
     */
    private static List<BlockDefinition> loadBlockDefinitionsFromGame() {
        List<BlockDefinition> blocks = new ArrayList<>();

        int index = 0;
        for (BlockTemplate bt : BlockTemplate.values()) {
            BlockType block = bt.blockType;

            // If BlockType has a real ID method, you can use that instead of 'index'.
            int id = index;

            String baseName = block.getName();
            int nbTextures = block.getNbTextures();
            int variantCount = (nbTextures <= 0) ? 1 : nbTextures;

            // Simple convention: "redBlock" -> "redBlock.png"
            String baseImage = baseName + ".png";

            blocks.add(new BlockDefinition(id, baseName, baseImage, variantCount, block));
            index++;
        }

        return blocks;
    }

    /**
     * Builds the JSON-ready export model from the simple definitions.
     */
    private static List<BlockCategoryExport> buildExportModel(List<BlockDefinition> definitions) {
        List<BlockCategoryExport> categories = new ArrayList<>();

        for (BlockDefinition def : definitions) {
            BlockCategoryExport category = new BlockCategoryExport();
            category.category_id = def.id;
            category.category_name = def.baseName;
            category.category_img = def.baseImage;
            category.blocks = new ArrayList<>();

            int variantCount = Math.max(def.variantCount, 1); // Safety: at least 1

            for (int type = 0; type < variantCount; type++) {
                BlockVariantExport variant = new BlockVariantExport();

                // Format: block_id is "categoryId-variantIndex" as a string
                variant.block_id = def.id + "-" + type;

                if (variantCount == 1) {
                    // No variants → code is just "id"
                    variant.code = String.valueOf(def.id);
                    variant.name = def.baseName;
                    variant.image = def.baseImage;
                } else {
                    // Has variants
                    if (type == 0) {
                        // First variant: code is just "id"
                        variant.code = String.valueOf(def.id);
                        variant.name = def.baseName;
                        variant.image = def.baseImage;
                    } else {
                        // Next variants: code is "id/type=n"
                        variant.code = def.id + "/type=" + type;

                        // Other variants get suffixed name & image
                        variant.name = def.baseName + type;
                        // e.g. "redBlock1.png", "redBlock2.png"
                        String baseNameNoExt = def.baseImage;
                        String extension = "";

                        int dotIndex = def.baseImage.lastIndexOf('.');
                        if (dotIndex != -1) {
                            baseNameNoExt = def.baseImage.substring(0, dotIndex);
                            extension = def.baseImage.substring(dotIndex); // includes '.'
                        }

                        variant.image = baseNameNoExt + type + extension;
                    }
                }

                category.blocks.add(variant);
            }

            categories.add(category);
        }

        return categories;
    }

    /**
     * Writes the given export data as pretty-ish JSON to a file using only standard Java.
     */
    private static void writeJsonToFile(List<BlockCategoryExport> data, String outputFile) throws IOException {
        ensureParentDirectoryExists(outputFile);

        try (FileWriter writer = new FileWriter(outputFile, false)) {
            writer.write("[\n");

            for (int i = 0; i < data.size(); i++) {
                BlockCategoryExport cat = data.get(i);
                writer.write("  {\n");
                writer.write("    \"category_id\": " + cat.category_id + ",\n");
                writer.write("    \"category_name\": \"" + escapeJson(cat.category_name) + "\",\n");
                writer.write("    \"category_img\": \"" + escapeJson(cat.category_img) + "\",\n");
                writer.write("    \"blocks\": [\n");

                for (int j = 0; j < cat.blocks.size(); j++) {
                    BlockVariantExport v = cat.blocks.get(j);
                    writer.write("      {\n");
                    writer.write("        \"block_id\": \"" + escapeJson(v.block_id) + "\",\n");
                    writer.write("        \"code\": \"" + escapeJson(v.code) + "\",\n");
                    writer.write("        \"name\": \"" + escapeJson(v.name) + "\",\n");
                    writer.write("        \"image\": \"" + escapeJson(v.image) + "\"\n");
                    writer.write("      }");
                    if (j < cat.blocks.size() - 1) {
                        writer.write(",");
                    }
                    writer.write("\n");
                }

                writer.write("    ]\n");
                writer.write("  }");
                if (i < data.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("]\n");
        }
    }

    private static void ensureParentDirectoryExists(String outputFile) throws IOException {
        Path path = Path.of(outputFile).toAbsolutePath();
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    /**
     * Export all block textures as 64x64 PNGs into builder/textures/
     * Filenames match the JSON "image" field:
     *   - baseName.png for the first / non-variant
     *   - baseName1.png, baseName2.png, ... for additional variants
     */
    private static void exportTextures(List<BlockDefinition> definitions) throws IOException {
        Path texturesDir = Path.of(TEXTURES_FOLDER).toAbsolutePath();
        if (!Files.exists(texturesDir)) {
            Files.createDirectories(texturesDir);
        }

        for (BlockDefinition def : definitions) {
            BlockType block = def.blockType;
            int variantCount = Math.max(def.variantCount, 1);

            for (int type = 0; type < variantCount; type++) {
                String imageName;

                if (variantCount == 1 || type == 0) {
                    // First / only variant uses baseImage
                    imageName = def.baseImage;
                } else {
                    // Variant image names: baseNameNoExt + type + extension
                    String baseNameNoExt = def.baseImage;
                    String extension = "";

                    int dotIndex = def.baseImage.lastIndexOf('.');
                    if (dotIndex != -1) {
                        baseNameNoExt = def.baseImage.substring(0, dotIndex);
                        extension = def.baseImage.substring(dotIndex); // includes '.'
                    }

                    imageName = baseNameNoExt + type + extension;
                }

                BufferedImage src;

                if (block.getNbTextures() == 0) {
                    // If no texture at all, create a simple placeholder
                    src = createPlaceholderTexture();
                } else {
                    int textureIndex = Math.min(type, block.getNbTextures() - 1);
                    src = block.getTexture(textureIndex).full(0);
                }

                BufferedImage upscaled = upscaleTo64(src);

                Path outPath = texturesDir.resolve(imageName);
                ImageIO.write(upscaled, "png", outPath.toFile());
            }
        }
    }

    /**
     * Upscale any source image to 64x64 using nearest-neighbor (pixelated zoom).
     * If the source is 16x16, this is a clean 4x zoom.
     */
    private static BufferedImage upscaleTo64(BufferedImage src) {
        int targetSize = 64;
        BufferedImage dest = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = dest.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(src, 0, 0, targetSize, targetSize, null);
        g.dispose();

        return dest;
    }

    /**
     * Simple placeholder texture used when a block has no textures.
     */
    private static BufferedImage createPlaceholderTexture() {
        int size = 16; // will be upscaled anyway
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new java.awt.Color(200, 200, 200, 255));
        g.fillRect(0, 0, size, size);
        g.setColor(java.awt.Color.DARK_GRAY);
        g.drawLine(0, 0, size - 1, size - 1);
        g.drawLine(size - 1, 0, 0, size - 1);
        g.dispose();
        return img;
    }

    /**
     * Minimal JSON string escaper (enough for filenames, codes, simple names).
     */
    private static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Internal data model(s)
    // -------------------------------------------------------------------------

    /**
     * Simple "source" definition you map from your game.
     * One instance per block category.
     */
    public static class BlockDefinition {
        public final int id;
        public final String baseName;
        public final String baseImage;
        public final int variantCount;
        public final BlockType blockType;

        public BlockDefinition(int id, String baseName, String baseImage, int variantCount, BlockType blockType) {
            this.id = id;
            this.baseName = baseName;
            this.baseImage = baseImage;
            this.variantCount = variantCount;
            this.blockType = blockType;
        }
    }

    /**
     * JSON export model: one object per category.
     */
    public static class BlockCategoryExport {
        public int category_id;
        public String category_name;
        public String category_img;
        public List<BlockVariantExport> blocks;
    }

    /**
     * JSON export model: one object per block variant.
     */
    public static class BlockVariantExport {
        public String block_id; // "categoryId-variantIndex"
        public String code;
        public String name;
        public String image;
    }
}
