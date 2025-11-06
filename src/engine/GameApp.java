package engine;

import javax.swing.*;
import java.awt.*;

public class GameApp extends JFrame {

    private static final int TARGET_FPS = 25;

    @SuppressWarnings("unused")
    public GameApp() {
        super("Chroniques de Voxellia 2");
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        add(panel);
        GameControls.listenKeys(panel);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        gd.setFullScreenWindow(this);

        final long FRAME_TIME = 1000 / TARGET_FPS; // en ms

        new Thread(() -> {
            while (panel.isDisplayable()) {
                long startTime = System.currentTimeMillis();

                panel.tick();
                panel.repaint();

                long elapsed = System.currentTimeMillis() - startTime;
                long sleepTime = FRAME_TIME - elapsed;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameApp::new);
    }
}
