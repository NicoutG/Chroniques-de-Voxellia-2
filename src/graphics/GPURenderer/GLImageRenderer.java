package graphics.GPURenderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.GL;

import tools.PathManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;

public class GLImageRenderer {

    private static HashMap<BufferedImage, Integer> imgToInt = new HashMap<>();
    private static long window = 0;

    private static int windowWidth;
    private static int windowHeight;


    // --- shader ---
    private static int shaderProgram;
    private static int uTex, uMaskLeft, uMaskRight, uMaskTop;
    private static int uLightLeft, uLightRight, uLightTop, uBrightness;
    private static int uSpriteColor;
    private static int uUseColor;


    static {
        String path = new File(PathManager.LIB_PATH).getAbsolutePath();
        System.setProperty("org.lwjgl.librarypath", path);

        if (!glfwInit())
            throw new IllegalStateException("GLFW init failed");
    }

    // ------------------------------------------------------------
    // WINDOW
    // ------------------------------------------------------------

    public static long createWindow(int width, int height) {
        windowWidth = width;
        windowHeight = height;

        window = glfwCreateWindow(width, height, "Chroniques de Voxellia 2", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glfwShowWindow(window);
        glfwFocusWindow(window);

        initGL(width, height);
        initShaders();

        return window;
    }


    public static long createFullWindow() {
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);

        if (mode == null)
            throw new IllegalStateException("Unable to get video mode");

        windowWidth  = mode.width();
        windowHeight = mode.height();

        window = glfwCreateWindow(
            windowWidth,
            windowHeight,
            "Chroniques de Voxellia 2",
            monitor,
            0
        );

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glfwShowWindow(window);
        glfwFocusWindow(window);

        initGL(windowWidth, windowHeight);
        initShaders();

        return window;
    }

    public static int getWidth() {
        return windowWidth;
    }

    public static int getHeight() {
        return windowHeight;
    }


    private static void initGL(int width, int height) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
    }

    // ------------------------------------------------------------
    // SHADERS
    // ------------------------------------------------------------

    private static void initShaders() {
        shaderProgram = glCreateProgram();

        int vert = loadShader(PathManager.SHADER_PATH + "sprite.vert", GL_VERTEX_SHADER);
        int frag = loadShader(PathManager.SHADER_PATH + "sprite.frag", GL_FRAGMENT_SHADER);

        glAttachShader(shaderProgram, vert);
        glAttachShader(shaderProgram, frag);
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
        }

        // uniforms
        uTex        = glGetUniformLocation(shaderProgram, "tex");
        uMaskLeft   = glGetUniformLocation(shaderProgram, "maskLeft");
        uMaskRight  = glGetUniformLocation(shaderProgram, "maskRight");
        uMaskTop    = glGetUniformLocation(shaderProgram, "maskTop");

        uLightLeft  = glGetUniformLocation(shaderProgram, "lightLeft");
        uLightRight = glGetUniformLocation(shaderProgram, "lightRight");
        uLightTop   = glGetUniformLocation(shaderProgram, "lightTop");
        uBrightness = glGetUniformLocation(shaderProgram, "brightness");

        uSpriteColor = glGetUniformLocation(shaderProgram, "spriteColor");
        uUseColor    = glGetUniformLocation(shaderProgram, "useColor");
    }

    private static int loadShader(String file, int type) {
        try {
            String src = Files.readString(new File(file).toPath());
            int shader = glCreateShader(type);
            glShaderSource(shader, src);
            glCompileShader(shader);

            if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
                throw new RuntimeException(glGetShaderInfoLog(shader));
            }
            return shader;
        } catch (Exception e) 
        {
            throw new RuntimeException("Failed to load shader " + file, e);
        }
    }

    // ------------------------------------------------------------
    // RENDER
    // ------------------------------------------------------------

    public static void renderer(List<Sprite> sprites) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(shaderProgram);

        for (Sprite s : sprites)
            drawSprite(s);

        glUseProgram(0);

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    // ------------------------------------------------------------
    // TEXTURE
    // ------------------------------------------------------------

    private static int uploadTexture(BufferedImage img) {
        if (imgToInt.containsKey(img))
            return imgToInt.get(img);

        int w = img.getWidth();
        int h = img.getHeight();
        ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int argb = img.getRGB(x, y);
                buffer.put((byte)((argb >> 16) & 0xFF));
                buffer.put((byte)((argb >> 8) & 0xFF));
                buffer.put((byte)(argb & 0xFF));
                buffer.put((byte)((argb >> 24) & 0xFF));
            }

        buffer.flip();

        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        imgToInt.put(img, texId);
        return texId;
    }

    // ------------------------------------------------------------
    // DRAW SPRITE
    // ------------------------------------------------------------

    private static void drawSprite(Sprite s) {

        // textures
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, uploadTexture(s.image));
        glUniform1i(uTex, 0);

        if (s.color != null) {
            glUniform3f(
                uSpriteColor,
                s.color[0] / 255f,
                s.color[1] / 255f,
                s.color[2] / 255f
            );
            glUniform1i(uUseColor, 1);
        } else {
            glUniform3f(uSpriteColor, 1f, 1f, 1f);
            glUniform1i(uUseColor, 0);
        }


        // mask LEFT
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(
            GL_TEXTURE_2D,
            s.maskLeft != null ? uploadTexture(s.maskLeft) : getEmptyMaskTexture()
        );
        glUniform1i(uMaskLeft, 1);

        // mask RIGHT
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(
            GL_TEXTURE_2D,
            s.maskRight != null ? uploadTexture(s.maskRight) : getEmptyMaskTexture()
        );
        glUniform1i(uMaskRight, 2);

        // mask TOP
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(
            GL_TEXTURE_2D,
            s.maskTop != null ? uploadTexture(s.maskTop) : getEmptyMaskTexture()
        );
        glUniform1i(uMaskTop, 3);

        // lights
        glUniform3f(uLightLeft,  s.lightLeft[0],  s.lightLeft[1],  s.lightLeft[2]);
        glUniform3f(uLightRight, s.lightRight[0], s.lightRight[1], s.lightRight[2]);
        glUniform3f(uLightTop,   s.lightTop[0],   s.lightTop[1],   s.lightTop[2]);
        glUniform1f(uBrightness, s.brightness);

        float x = s.x, y = s.y, w = s.width, h = s.height;

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(x,     y);
        glTexCoord2f(1, 0); glVertex2f(x + w, y);
        glTexCoord2f(1, 1); glVertex2f(x + w, y + h);
        glTexCoord2f(0, 1); glVertex2f(x,     y + h);
        glEnd();
    }

    private static int EMPTY_MASK_TEX = -1;

    private static int getEmptyMaskTexture() {
        if (EMPTY_MASK_TEX != -1)
            return EMPTY_MASK_TEX;

        ByteBuffer buffer = BufferUtils.createByteBuffer(4);
        buffer.put((byte)0); // R
        buffer.put((byte)0); // G
        buffer.put((byte)0); // B
        buffer.put((byte)0); // A
        buffer.flip();

        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA,
            1,
            1,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            buffer
        );

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        EMPTY_MASK_TEX = tex;
        return tex;
    }

}
