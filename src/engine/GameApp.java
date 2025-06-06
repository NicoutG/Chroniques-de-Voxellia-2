package engine;

import javax.swing.*;
import java.awt.*;

public class GameApp extends JFrame {

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

        int delayMs = 1000 / 20;
        new Timer(delayMs, e -> {
            panel.tick();
            panel.repaint();
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameApp::new);
    }
}
