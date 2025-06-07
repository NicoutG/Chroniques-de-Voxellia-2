package objects.collision;

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
    public static final ComplexCollision SLOPE1 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, -0.5, 0, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, 0, 0.17, 0.5));
    public static final ComplexCollision SLOPE2 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, 0, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, 0, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, 0, -0.5, 0.17, 0.5));
    public static final ComplexCollision SLOPE3 = new ComplexCollision(new BoundingCollision(-0.5, 0, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, 0, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision SLOPE4 = new ComplexCollision(new BoundingCollision(0, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(0, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(0, 0.5, -0.5, -0.17, 0.17, 0.5));
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

    private CollisionList() {}
}
