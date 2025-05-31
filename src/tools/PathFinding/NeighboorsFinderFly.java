package tools.PathFinding;

import java.util.ArrayList;

import objects.entity.Entity;
import tools.Vector;
import world.World;

public class NeighboorsFinderFly extends NeighboorsFinder {

    public ArrayList<Vector> getNeighboors(World world, Entity entity, Vector position, Vector destination) {
        ArrayList<Vector> neighboors = new ArrayList<>();
        final double x = position.x;
        final double y = position.y;
        final double z = position.z;
        Vector[] positions = new Vector[] {
            new Vector(x,y,z-1),
            new Vector(x,y,z+1),
            new Vector(x,y-1,z),
            new Vector(x,y+1,z),
            new Vector(x-1,y,z),
            new Vector(x+1,y,z),
        };
        for (Vector posNeigh : positions) {
            if (posNeigh.equals(destination) || isValidNeighboor(world, entity, posNeigh))
                neighboors.add(posNeigh);
        }
        return neighboors;
    }
}
