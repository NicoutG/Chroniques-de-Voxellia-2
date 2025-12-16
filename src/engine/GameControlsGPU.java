package engine;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashSet;

public class GameControlsGPU {

    private static final HashSet<Integer> pressedKeys = new HashSet<>();

    /**
     * Initialise le callback clavier pour une fenêtre GLFW avec mapping AZERTY
     */
    public static void listenKeys(long window) {
        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            int mappedKey = mapAZERTY(key);

            if (action == GLFW_PRESS) {
                pressedKeys.add(mappedKey);
            } else if (action == GLFW_RELEASE) {
                pressedKeys.remove(mappedKey);
            }
        });
    }

    /** Vérifie si une touche est pressée */
    public static boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    /** Mapping QWERTY → AZERTY pour lettres principales */
    private static int mapAZERTY(int key) {
        return switch (key) {
            case GLFW_KEY_Q -> GLFW_KEY_A; // Q → A
            case GLFW_KEY_W -> GLFW_KEY_Z; // W → Z
            case GLFW_KEY_A -> GLFW_KEY_Q; // A → Q
            case GLFW_KEY_Z -> GLFW_KEY_W; // Z → W
            default -> key; // les autres touches restent identiques
        };
    }
}
