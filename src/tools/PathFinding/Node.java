package tools.PathFinding;

import tools.Vector;

public class Node {
    public Vector position;
    public int made;
    public double leftEstimation;
    public double totalEstimation;
    public Node parent;

    public Node(Vector position, int made, double leftEstimation, Node parent) {
        this.position = position;
        this.made = made;
        this.leftEstimation = leftEstimation;
        this.totalEstimation = made + leftEstimation;
        this.parent = parent;
    }
}
