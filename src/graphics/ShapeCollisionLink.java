package graphics;

import java.util.HashMap;
import graphics.shape.Shape;
import graphics.shape.ShapeList;
import objects.collision.Collision;
import objects.collision.CollisionList;

public final class ShapeCollisionLink {
    
    private static final HashMap<Shape, Collision> shapeToCollision = new HashMap<>();

    static {
        shapeToCollision.put(ShapeList.BORDER_LEFT, CollisionList.BARRIER_LEFT);
        shapeToCollision.put(ShapeList.BORDER_RIGHT, CollisionList.BARRIER_RIGHT);
        shapeToCollision.put(ShapeList.CUBE, CollisionList.CUBE);

        shapeToCollision.put(ShapeList.TRANSPARENT_CUBE, CollisionList.CUBE);

        shapeToCollision.put(ShapeList.RECTANGLE1, CollisionList.RECTANGLE1);
        shapeToCollision.put(ShapeList.RECTANGLE2, CollisionList.RECTANGLE2);
        shapeToCollision.put(ShapeList.RECTANGLE3, CollisionList.RECTANGLE3);
        shapeToCollision.put(ShapeList.SLAB_TOP, CollisionList.SLAB_TOP);
        shapeToCollision.put(ShapeList.SLAB_VERTICAL1, CollisionList.SLAB_VERTICAL1);
        shapeToCollision.put(ShapeList.SLAB_VERTICAL2, CollisionList.SLAB_VERTICAL2);
        shapeToCollision.put(ShapeList.SLAB_VERTICAL3, CollisionList.SLAB_VERTICAL3);
        shapeToCollision.put(ShapeList.SLAB_VERTICAL4, CollisionList.SLAB_VERTICAL4);
        shapeToCollision.put(ShapeList.SLOPE1, CollisionList.STAIR1);
        shapeToCollision.put(ShapeList.SLOPE2, CollisionList.STAIR2);
        shapeToCollision.put(ShapeList.SLOPE3, CollisionList.STAIR3);
        shapeToCollision.put(ShapeList.SLOPE4, CollisionList.STAIR4);
        shapeToCollision.put(ShapeList.SLOPE5, CollisionList.STAIR5);
        shapeToCollision.put(ShapeList.SLOPE6, CollisionList.STAIR6);
        shapeToCollision.put(ShapeList.SLOPE7, CollisionList.STAIR7);
        shapeToCollision.put(ShapeList.SLOPE8, CollisionList.STAIR8);
        shapeToCollision.put(ShapeList.SLOPE_ANGLE1, CollisionList.STAIR_ANGLE1);
        shapeToCollision.put(ShapeList.SLOPE_ANGLE2, CollisionList.STAIR_ANGLE2);
        shapeToCollision.put(ShapeList.SLOPE_ANGLE3, CollisionList.STAIR_ANGLE3);
        shapeToCollision.put(ShapeList.SLOPE_ANGLE4, CollisionList.STAIR_ANGLE4);
        shapeToCollision.put(ShapeList.SLOPE_ANGLE5, CollisionList.STAIR_ANGLE5);
        shapeToCollision.put(ShapeList.SLOPE_ANGLE6, CollisionList.STAIR_ANGLE6);
        shapeToCollision.put(ShapeList.SLOPE_ANGLE7, CollisionList.STAIR_ANGLE7);
        shapeToCollision.put(ShapeList.SLOPE_ANGLE8, CollisionList.STAIR_ANGLE8);
        shapeToCollision.put(ShapeList.SLOPE_HALF1, CollisionList.STAIR_HALF1);
        shapeToCollision.put(ShapeList.SLOPE_HALF2, CollisionList.STAIR_HALF2);
        shapeToCollision.put(ShapeList.SLOPE_HALF3, CollisionList.STAIR_HALF3);
        shapeToCollision.put(ShapeList.SLOPE_HALF4, CollisionList.STAIR_HALF4);
        shapeToCollision.put(ShapeList.STAIR1, CollisionList.STAIR1);
        shapeToCollision.put(ShapeList.STAIR2, CollisionList.STAIR2);
        shapeToCollision.put(ShapeList.STAIR3, CollisionList.STAIR3);
        shapeToCollision.put(ShapeList.STAIR4, CollisionList.STAIR4);
        shapeToCollision.put(ShapeList.STAIR_ANGLE1, CollisionList.STAIR_ANGLE1);
        shapeToCollision.put(ShapeList.STAIR_ANGLE2, CollisionList.STAIR_ANGLE2);
        shapeToCollision.put(ShapeList.STAIR_ANGLE3, CollisionList.STAIR_ANGLE3);
        shapeToCollision.put(ShapeList.STAIR_ANGLE4, CollisionList.STAIR_ANGLE4);
        shapeToCollision.put(ShapeList.STAIR_ANGLE5, CollisionList.STAIR_ANGLE5);
        shapeToCollision.put(ShapeList.STAIR_ANGLE6, CollisionList.STAIR_ANGLE6);
        shapeToCollision.put(ShapeList.STAIR_ANGLE7, CollisionList.STAIR_ANGLE7);
        shapeToCollision.put(ShapeList.STAIR_ANGLE8, CollisionList.STAIR_ANGLE8);

        shapeToCollision.put(ShapeList.BORDER_ANGLE1, CollisionList.BARRIER_ANGLE1);
        shapeToCollision.put(ShapeList.BORDER_ANGLE2, CollisionList.BARRIER_ANGLE2);
        shapeToCollision.put(ShapeList.BORDER_ANGLE3, CollisionList.BARRIER_ANGLE3);
        shapeToCollision.put(ShapeList.BORDER_ANGLE4, CollisionList.BARRIER_ANGLE4);
        shapeToCollision.put(ShapeList.BORDER_HORIZONTAL, CollisionList.BARRIER_HORIZONTAL);
        shapeToCollision.put(ShapeList.HOLDER, CollisionList.POLE);
        shapeToCollision.put(ShapeList.HOLDER_ROPE1, CollisionList.POLE);
        shapeToCollision.put(ShapeList.HOLDER_ROPE2, CollisionList.POLE);
        shapeToCollision.put(ShapeList.HOLDER_ROPE3, CollisionList.POLE);
        shapeToCollision.put(ShapeList.BARRIER1, CollisionList.POLE);
        shapeToCollision.put(ShapeList.BARRIER2, CollisionList.POLE);
        shapeToCollision.put(ShapeList.BARRIER3, CollisionList.POLE);
        shapeToCollision.put(ShapeList.COLUMN, CollisionList.COLUMN);
    }

    public static Collision getCollision(Shape shape) {
        if (!shapeToCollision.containsKey(shape)) {
            System.out.println(shape);
            System.out.println("Alert : No collision for the shape");
            return null;
        }
        return shapeToCollision.get(shape);
    }
}
