package tools.PathFinding;

import java.util.ArrayList;

import objects.entity.Entity;
import tools.Vector;
import world.World;

public class NeighboorsFinderFalling extends NeighboorsFinder {

    public ArrayList<Vector> getNeighboors(World world, Entity entity, Vector position, Vector destination) {
        ArrayList<Vector> neighboors = new ArrayList<>();
        final double x = position.x;
        final double y = position.y;
        final double z = position.z;
        Vector posBelow = new Vector(x,y,z-1);
        boolean valid = isValidNeighboor(world, entity, posBelow);
        boolean hasFloor = !valid && (0 < posBelow.z);
        if (posBelow.equals(destination) || valid)
            neighboors.add(posBelow);
        Vector posBelowBelow = new Vector(posBelow.x, posBelow.y, posBelow.z - 1);
        Vector[] positionsSide = new Vector[] {
            new Vector(x,y-1,z),
            new Vector(x,y+1,z),
            new Vector(x-1,y,z),
            new Vector(x+1,y,z),
        };
        for (Vector posSide : positionsSide)
            if ((posSide.equals(destination) || isValidNeighboor(world, entity, posSide)) && 
                    (hasFloor || (!isValidNeighboor(world, entity, new Vector(posSide.x, posSide.y, posSide.z - 1)) && 2 < posSide.z && !isValidNeighboor(world, entity, posBelowBelow))))
                neighboors.add(posSide);
        if (hasFloor) {
            Vector posAbove = new Vector(x,y,z+1);
            if (posAbove.equals(destination) || isValidNeighboor(world, entity, posAbove))
                neighboors.add(posAbove);
        }
        return neighboors;
    }
}
