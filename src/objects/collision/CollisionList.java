package objects.collision;

public final class CollisionList {
    public static final Collision CUBE = new Collision();
    public static final BoundingCollision BLOCK_ENTITY = new BoundingCollision(-0.4, 0.4, -0.4, 0.4, -0.4, 0.4);
    public static final BoundingCollision ON_TOP_ENTITY = new BoundingCollision(-0.4, 0.4, -0.4, 0.4, 0.5, 0.5);
    public static final BoundingCollision ON_TOP_BLOCK = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, 0.6, 0.6);
    public static final BoundingCollision SLAB_TOP = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, 0, 0.5);
    public static final BoundingCollision SLAB_BOTTOM = new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, 0);
    public static final BoundingCollision SLAB_VERTICAL1 = new BoundingCollision(-0.5,0.5,-0.5,0,-0.5,0.5);
    public static final BoundingCollision SLAB_VERTICAL2 = new BoundingCollision(-0.5,0,-0.5,0.5,-0.5,0.5);
    public static final BoundingCollision SLAB_VERTICAL3 = new BoundingCollision(-0.5,0.5,0,0.5,-0.5,0.5);
    public static final BoundingCollision SLAB_VERTICAL4 = new BoundingCollision(0,0.5,-0.5,0.5,-0.5,0.5);
    public static final ComplexCollision STEP_LEFT = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, -0.5, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, 0.5, 0.17, 0.5));
    public static final ComplexCollision STEP_RIGHT = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, 0.5, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision SLOPE1 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, -0.5, 0, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, -0.5, 0, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, -0.5, 0, 0.17, 0.5));
    public static final ComplexCollision SLOPE2 = new ComplexCollision(new BoundingCollision(-0.5, 0.5, 0, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0.17, 0, 0.5, -0.17, 0.17), new BoundingCollision(-0.5, -0.17, 0, -0.5, 0.17, 0.5));
    public static final ComplexCollision SLOPE3 = new ComplexCollision(new BoundingCollision(-0.5, 0, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(-0.5, 0, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(-0.5, 0, -0.5, -0.17, 0.17, 0.5));
    public static final ComplexCollision SLOPE4 = new ComplexCollision(new BoundingCollision(0, 0.5, -0.5, 0.5, -0.5, -0.17), new BoundingCollision(0, 0.5, -0.5, 0.17, -0.17, 0.17), new BoundingCollision(0, 0.5, -0.5, -0.17, 0.17, 0.5));

    private CollisionList() {}
}
