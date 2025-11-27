package graphics.shape;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

public final class ShapeList {
    public static final Shape BORDER_LEFT = new Shape("borders/borderLeft/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BORDER_RIGHT = new Shape("borders/borderRight/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape CUBE = new Shape("cube/","mask-left.png","mask-right.png","mask-top.png",true);
    public static final Shape COMPLETE = new Shape("complete/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape TRANSPARENT_CUBE = new Shape("cube/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape FLYING_SLIME = new Shape("flyingSlime/","mask-left.png","mask-right.png","mask-top.png",true);
    public static final Shape LEVER_ON = new Shape("lever/true/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape LEVER_OFF = new Shape("lever/false/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape PLAYER = new Shape("player/","mask-left.png","mask-right.png","mask-top.png",true);
    public static final Shape RECTANGLE1 = new Shape("rectangle/rectangle1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape RECTANGLE2 = new Shape("rectangle/rectangle2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape RECTANGLE3 = new Shape("rectangle/rectangle3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLAB_BOTTOM = new Shape("slab/horizontal/bottom/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLAB_TOP = new Shape("slab/horizontal/top/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLAB_VERTICAL1 = new Shape("slab/vertical/1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLAB_VERTICAL2 = new Shape("slab/vertical/2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLAB_VERTICAL3 = new Shape("slab/vertical/3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLAB_VERTICAL4 = new Shape("slab/vertical/4/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE1 = new Shape("slope/1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE2 = new Shape("slope/2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE3 = new Shape("slope/3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE4 = new Shape("slope/4/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE5 = new Shape("slope/5/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE6 = new Shape("slope/6/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE7 = new Shape("slope/7/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE8 = new Shape("slope/8/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_ANGLE1 = new Shape("slope/angle1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_ANGLE2 = new Shape("slope/angle2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_ANGLE3 = new Shape("slope/angle3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_ANGLE4 = new Shape("slope/angle4/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_ANGLE5 = new Shape("slope/angle5/","mask-left.png","mask-right.png","mask-top.png",true);
    public static final Shape SLOPE_ANGLE6 = new Shape("slope/angle6/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_ANGLE7 = new Shape("slope/angle7/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_ANGLE8 = new Shape("slope/angle8/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_HALF1 = new Shape("slope/half1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_HALF2 = new Shape("slope/half2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_HALF3 = new Shape("slope/half3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SLOPE_HALF4 = new Shape("slope/half4/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR1 = new Shape("stairs/1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR2 = new Shape("stairs/2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR3 = new Shape("stairs/3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR4 = new Shape("stairs/4/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR_ANGLE1 = new Shape("stairs/angle1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR_ANGLE2 = new Shape("stairs/angle2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR_ANGLE3 = new Shape("stairs/angle3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR_ANGLE4 = new Shape("stairs/angle4/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR_ANGLE5 = new Shape("stairs/angle5/","mask-left.png","mask-right.png","mask-top.png",true);
    public static final Shape STAIR_ANGLE6 = new Shape("stairs/angle6/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR_ANGLE7 = new Shape("stairs/angle7/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR_ANGLE8 = new Shape("stairs/angle8/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SIGN_LEFT = new Shape("signs/left/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape SIGN_RIGHT = new Shape("signs/right/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape GRASS_BIG = new Shape("herbs/grass-big/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape GRASS_SMALL = new Shape("herbs/grass-small/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape FLOWERS = new Shape("herbs/flowers/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BORDER_ANGLE1 = new Shape("borders/borderAngle1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BORDER_ANGLE2 = new Shape("borders/borderAngle2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BORDER_ANGLE3 = new Shape("borders/borderAngle3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BORDER_ANGLE4 = new Shape("borders/borderAngle4/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BORDER_HORIZONTAL = new Shape("borders/borderHorizontal/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape HOLDER = new Shape("holders/holder/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape HOLDER_ROPE1 = new Shape("holders/holder-rope1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape HOLDER_ROPE2 = new Shape("holders/holder-rope2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape HOLDER_ROPE3 = new Shape("holders/holder-rope3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BARRIER1 = new Shape("barriers/barrier1/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BARRIER2 = new Shape("barriers/barrier2/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BARRIER3 = new Shape("barriers/barrier3/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape COLUMN = new Shape("column/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape INSIDE = new Shape("inside/","mask-left.png","mask-right.png","mask-top.png",false);
    

    private ShapeList() {}

    public static List<Shape> getAllShapes() {
        List<Shape> shapes = new ArrayList<>();
        Field[] fields = ShapeList.class.getDeclaredFields();

        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                && field.getType() == Shape.class) {
                try {
                    shapes.add((Shape) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return shapes;
    }

    public static List<String> getAllShapeNames() {
        List<String> names = new ArrayList<>();
        Field[] fields = ShapeList.class.getDeclaredFields();

        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && field.getType() == Shape.class) {
                names.add(field.getName());
            }
        }

        return names;
    }
}
