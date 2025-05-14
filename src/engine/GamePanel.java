package engine;

import javax.swing.*;

import graphics.Renderer;
import model.world.World;
import objects.entity.Player;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    private final Renderer renderer;
    private final World world;
    private long tick = 0;

    public GamePanel() {
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();

        this.world = World.createDemoWorld();
        this.renderer = new Renderer(world);

        // Écouteur de touches
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Player player = world.getPlayer();
                if (player == null) {
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> player.move(world, 0, -0.1, 0);
                    case KeyEvent.VK_DOWN -> player.move(world, 0, 0.1, 0);
                    case KeyEvent.VK_LEFT -> player.move(world, -0.1, 0, 0);
                    case KeyEvent.VK_RIGHT -> player.move(world, 0.1, 0, 0);
                    case KeyEvent.VK_SPACE -> player.addVelocity(0,0,1.5);
                }
            }
        });
    }

    public void tick() {
        tick++;
        world.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render((Graphics2D) g, getWidth(), getHeight(), tick);
    }
}
