package engine;

import javax.swing.*;

import audio.SoundManager;
import graphics.GPURenderer.GLImageRenderer;
import graphics.GPURenderer.GPURenderer;
import world.World;

import static org.lwjgl.glfw.GLFW.*;

public class GameAppGPU {

    private final GPURenderer renderer;
    private final SoundManager soundManager;
    private final World world;

    private static final int TARGET_FPS = 25;
    private static int windowWidth = 1200;
    private static int windowHeight = 800;

    public GameAppGPU() {
        // this.world = new World("tests/lost-city.json");
        this.world = new World("chapter1/1-1.json");
        // this.world = new World("chapter1/1-3/1-3-1.json");
        // this.world = new World("tests/testLight.json");
        // this.world = new World("chapter2/2-1.json");

        this.soundManager = new SoundManager(world);
        this.renderer = new GPURenderer(world);

        // long window = GLImageRenderer.createWindow(windowWidth, windowHeight);
        long window = GLImageRenderer.createFullscreenWindow();
        windowWidth = GLImageRenderer.getWindowWidth();
        windowHeight = GLImageRenderer.getWindowHeight();

        GameControlsGPU.listenKeys(window);

        final long FRAME_TIME_MS = 1000 / TARGET_FPS;

        // Thread pour la logique du jeu
        new Thread(() -> {
            while (!glfwWindowShouldClose(window)) {
                long startTime = System.currentTimeMillis();
                tick();
                long elapsed = System.currentTimeMillis() - startTime;
                long sleepTime = FRAME_TIME_MS - elapsed;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // Boucle principale de rendu dans le thread courant (context OpenGL courant)
        long lastTime = System.nanoTime();
        int frames = 0;
        while (!glfwWindowShouldClose(window)) {
            long startTime = System.nanoTime();

            renderer.render(windowWidth, windowHeight, World.getTick());

            frames++;
            long now = System.nanoTime();
            if (now - lastTime >= 1_000_000_000L) { // 1 seconde
                int fps = frames;
                glfwSetWindowTitle(window, "Chroniques de Voxellia 2 - FPS: " + fps);
                frames = 0;
                lastTime = now;
            }

            long elapsed = (System.nanoTime() - startTime) / 1_000_000; // en ms
            long sleepTime = FRAME_TIME_MS - elapsed;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        glfwTerminate();
    }

    private void tick() {
        synchronized (world) {
            world.update();
        }
        soundManager.tick();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameAppGPU::new);
    }
}
