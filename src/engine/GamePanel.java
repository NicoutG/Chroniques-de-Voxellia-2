package engine;

import javax.swing.*;

import graphics.Renderer;
import world.World;

import java.awt.*;

public class GamePanel extends JPanel {

    private final Renderer renderer;
    private final World world;
    private long tick = 0;

    public GamePanel() {
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();

        // this.world = new World("world-0-0.txt");
         this.world = new World("test2.txt");


        this.renderer = new Renderer(world);
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
