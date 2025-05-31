package tools.PathFinding;

import tools.Vector;

public class Path {
    public Vector position;
    public int made;
    public double leftEstimation;
    public double totalEstimation;
    public Path parent;

    public Path(Vector position, int made, double leftEstimation, Path parent) {
        this.position = position;
        this.made = made;
        this.leftEstimation = leftEstimation;
        this.totalEstimation = made + leftEstimation;
        this.parent = parent;
    }
}
