package objects.collision;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class CollisionList {
    public static final Collision CUBE = new Collision();
    public static final BoundingCollision RECTANGLE3 = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, 0.25);
    public static final BoundingCollision RECTANGLE2 = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, 0);
    public static final BoundingCollision RECTANGLE1 = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.25);
    public static final BoundingCollision BLOCK_ENTITY = new BoundingCollision(-0.4, 0.4, -0.4, 0.4, -0.45, 0.45);
    public static final BoundingCollision ON_TOP_ENTITY = new BoundingCollision(-0.4, 0.4, -0.4, 0.4, 0.5, 0.5);
    public static final BoundingCollision ON_TOP_BLOCK = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, 0.6, 0.6);
    public static final BoundingCollision SLAB_TOP = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, 0, 0.5);
    public static final BoundingCollision SLAB_BOTTOM = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, 0);
    public static final BoundingCollision SLAB_VERTICAL1 = new BoundingCollision(-0.5,0.5,-0.5,0,-0.5,0.5);
    public static final BoundingCollision SLAB_VERTICAL2 = new BoundingCollision(-0.5,0,-0.5,0.5,-0.5,0.5);
    public static final BoundingCollision SLAB_VERTICAL3 = new BoundingCollision(-0.5,0.5,0,0.5,-0.5,0.5);
    public static final BoundingCollision SLAB_VERTICAL4 = new BoundingCollision(0,0.5,-0.5,0.5,-0.5,0.5);
    public static final ComplexCollision STAIR_HALF1 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, -0.5, 0, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, 0, 0.17, 0.5));
    public static final ComplexCollision STAIR_HALF2 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, 0, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, 0, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, 0, 0.5, 0.17, 0.5));
    public static final ComplexCollision STAIR_HALF3 = new ComplexCollision(new BoundingCollision(-0.5, 0, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, 0, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision STAIR_HALF4 = new ComplexCollision(new BoundingCollision(0, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(0, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(0, 0.5, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision STAIR1 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, 0.5, 0.17, 0.5));
    public static final ComplexCollision STAIR2 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision STAIR3 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.17, 0.5, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(0.17, 0.5, -0.5, 0.5, 0.17, 0.5));
    public static final ComplexCollision STAIR4 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.5, -0.17, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, 0.17, 0.5, 0.17, 0.5));
    public static final ComplexCollision STAIR5 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, 0.17, 0.5), new BoundingCollision(-0.5, 0.17, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, 0.5, -0.5, -0.17));
    public static final ComplexCollision STAIR6 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, 0.17, 0.5), new BoundingCollision(-0.5, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, -0.5, -0.17, -0.5, -0.17));
    public static final ComplexCollision STAIR7 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, 0.17, 0.5), new BoundingCollision(-0.17, 0.5, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(0.17, 0.5, -0.5, 0.5, -0.5, -0.17));
    public static final ComplexCollision STAIR8 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, 0.17, 0.5), new BoundingCollision(-0.5, 0.5, -0.17, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, 0.17, 0.5, -0.5, -0.17));
    public static final ComplexCollision STAIR_ANGLE1 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision STAIR_ANGLE2 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.17, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(0.17, 0.5, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision STAIR_ANGLE3 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.17, 0.5, -0.17, 0.5, -0.17, 0.17), new BoundingCollision(0.17, 0.5, 0.17, 0.5, 0.17, 0.5));
    public static final ComplexCollision STAIR_ANGLE4 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, -0.17, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, 0.17, 0.5, 0.17, 0.5));
    public static final ComplexCollision STAIR_ANGLE5 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, 0.5, 0.17, 0.5),new BoundingCollision(-0.5, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision STAIR_ANGLE6 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, -0.5, -0.17, 0.17, 0.5),new BoundingCollision(-0.17, 0.5, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(0.17, 0.5, -0.5, 0.5, 0.17, 0.5));
    public static final ComplexCollision STAIR_ANGLE7 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.17, 0.5, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(0.17, 0.5, -0.5, 0.5, 0.17, 0.5), new BoundingCollision(-0.5, 0.5, -0.17, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, 0.17, 0.5, 0.17, 0.5));
    public static final ComplexCollision STAIR_ANGLE8 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.5, -0.17, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, 0.17, 0.5, 0.17, 0.5), new BoundingCollision(-0.5, 0.17, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, 0.5, 0.17, 0.5));
    public static final BoundingCollision POLE = new BoundingCollision(-0.25, 0.25, -0.25, 0.25, -0.5, 0.5);
    public static final BoundingCollision BARRIER_RIGHT = new BoundingCollision(-0.5, 0.5, -0.25, 0.25, -0.5, 0.5);
    public static final BoundingCollision BARRIER_LEFT = new BoundingCollision(-0.25, 0.25, -0.5, 0.5, -0.5, 0.5);
    public static final ComplexCollision BARRIER_ANGLE1 = new ComplexCollision(new BoundingCollision(-0.25, 0.5, -0.25, 0.25, -0.5, 0.5), new BoundingCollision(-0.25, 0.25, -0.25, 0.5, -0.5, 0.5));
    public static final ComplexCollision BARRIER_ANGLE2 = new ComplexCollision(new BoundingCollision(-0.5, 0.25, -0.25, 0.25, -0.5, 0.5), new BoundingCollision(-0.25, 0.25, -0.25, 0.5, -0.5, 0.5));
    public static final ComplexCollision BARRIER_ANGLE3 = new ComplexCollision(new BoundingCollision(-0.5, 0.25, -0.25, 0.25, -0.5, 0.5), new BoundingCollision(-0.25, 0.25, -0.5, 0.25, -0.5, 0.5));
    public static final ComplexCollision BARRIER_ANGLE4 = new ComplexCollision(new BoundingCollision(-0.25, 0.5, -0.25, 0.25, -0.5, 0.5), new BoundingCollision(-0.25, 0.25, -0.5, 0.25, -0.5, 0.5));

    private CollisionList() {}

    public static List<Collision> getAllCollisions() {
        List<Collision> collisions = new ArrayList<>();
        Field[] fields = CollisionList.class.getDeclaredFields();

        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                && Collision.class.isAssignableFrom(field.getType())) {
                try {
                    collisions.add((Collision) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return collisions;
    }

    public static List<String> getAllCollisionNames() {
        List<String> names = new ArrayList<>();
        Field[] fields = CollisionList.class.getDeclaredFields();

        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && Collision.class.isAssignableFrom(field.getType())) {
                names.add(field.getName());
            }
        }

        return names;
    }
}
