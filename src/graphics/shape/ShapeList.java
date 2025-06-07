package graphics.shape;

public final class ShapeList {
    public static final Shape BORDER_LEFT = new Shape("borders/borderLeft/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape BORDER_RIGHT = new Shape("borders/borderRight/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape CUBE = new Shape("cube/","mask-left.png","mask-right.png","mask-top.png",true);
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
    public static final Shape STAIR1 = new Shape("stairs/left/","mask-left.png","mask-right.png","mask-top.png",false);
    public static final Shape STAIR2 = new Shape("stairs/right/","mask-left.png","mask-right.png","mask-top.png",false);
    

    private ShapeList() {}
}
