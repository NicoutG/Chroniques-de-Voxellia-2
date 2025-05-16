package engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import javax.swing.JPanel;

public class GameControls {
    private static HashSet<Integer> pressedKeys = new HashSet<>();

    public static void listenKeys (JPanel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });
        panel.requestFocus();
    }

    public static boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
