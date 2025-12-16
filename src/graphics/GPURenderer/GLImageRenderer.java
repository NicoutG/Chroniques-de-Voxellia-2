package graphics.GPURenderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.BufferUtils;

public class GLImageRenderer {
    private static HashMap<BufferedImage, Integer> imgToInt = new HashMap<>();
    private static long window = 0;

    static {
        String path = new File("lib/native").getAbsolutePath();
        System.setProperty("org.lwjgl.librarypath", path);

        if (!glfwInit()) throw new IllegalStateException("GLFW init failed");
    }

    public static long createWindow(int width, int height) {
        window = glfwCreateWindow(width, height, "Chroniques de Voxellia 2", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        return window;
    }

    public static long createFullscreenWindow() {

        long monitor = glfwGetPrimaryMonitor();
        if (monitor == 0)
            throw new IllegalStateException("No monitor found");

        GLFWVidMode videoMode = glfwGetVideoMode(monitor);
        if (videoMode == null)
            throw new IllegalStateException("Impossible to get the resolution");

        int width = videoMode.width();
        int height = videoMode.height();

        window = glfwCreateWindow(width, height, "Chroniques de Voxellia 2", monitor, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        return window;
    }

    public static int getWindowWidth() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            glfwGetWindowSize(window, w, h);
            return w.get(0);
        }
    }

    public static int getWindowHeight() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            glfwGetWindowSize(window, w, h);
            return h.get(0);
        }
    }

    public static void renderer(List<Sprite> sprites) {
        glClear(GL_COLOR_BUFFER_BIT);

        for (Sprite s : sprites)
            GLImageRenderer.drawSprite(s);

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    private static int uploadTexture(BufferedImage img) {
        if (imgToInt.containsKey(img))
            return imgToInt.get(img);

        int w = img.getWidth();
        int h = img.getHeight();

        ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = img.getRGB(x, y);
                buffer.put((byte) ((argb >> 16) & 0xFF)); // R
                buffer.put((byte) ((argb >> 8) & 0xFF));  // G
                buffer.put((byte) (argb & 0xFF));         // B
                buffer.put((byte) ((argb >> 24) & 0xFF)); // A
            }
        }
        buffer.flip();

        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                w,
                h,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                buffer
        );

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        imgToInt.put(img, texId);

        return texId;
    }

    private static void drawSprite(Sprite s) {
        int texId = uploadTexture(s.image);
        float w = s.width;
        float h = s.height;

        glBindTexture(GL_TEXTURE_2D, texId);

        if (s.color != null) {
            // on normalise la couleur de 0-255 → 0-1 et applique brightness
            float r = s.color[0] / 255f * s.brightness;
            float g = s.color[1] / 255f * s.brightness;
            float b = s.color[2] / 255f * s.brightness;
            glColor4f(r, g, b, 1f); // alpha = 1
        } else {
            // si pas de couleur, applique juste brightness sur la texture
            glColor4f(s.brightness, s.brightness, s.brightness, 1f);
        }

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(s.x,     s.y);
        glTexCoord2f(1, 0); glVertex2f(s.x + w, s.y);
        glTexCoord2f(1, 1); glVertex2f(s.x + w, s.y + h);
        glTexCoord2f(0, 1); glVertex2f(s.x,     s.y + h);
        glEnd();

        glColor4f(1, 1, 1, 1); // reset
    }
}
