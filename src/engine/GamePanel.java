package engine;

import javax.swing.*;

import graphics.Renderer;
import world.World;

import java.awt.*;

public class GamePanel extends JPanel {

    private final Renderer renderer;
    private final World world;
    private long tick = 0;

    private long time = 0;
    public double fps = 0;
    private final int TICK_FPS = 20;

    public GamePanel() {
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();

        this.world = new World("world-0-0.txt");
        //  this.world = new World("lost-city.txt");
        //  this.world = new World("room.txt");


        this.renderer = new Renderer(world);
    }

    public void tick() {
        if (tick % TICK_FPS == 0)
            time = System.currentTimeMillis();

        tick++;
        world.update();
        
        if (tick % TICK_FPS == 0) {
            long dif = (System.currentTimeMillis() - time);
            fps = 1000.0 * TICK_FPS / dif;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render((Graphics2D) g, getWidth(), getHeight(), tick);
    }
}
