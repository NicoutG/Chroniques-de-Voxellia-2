package objects;

public class ObjectAnimation {
    private int texture;
    private int tickDuration = 0;
    private int priority = 0;
    private boolean keepIfSamePriority = false;

    public ObjectAnimation(int texture, int tickDuration, int priority, boolean keepIfSamePriority) {
        this.texture = texture;
        this.tickDuration = tickDuration;
        this.priority = priority;
        this.keepIfSamePriority = keepIfSamePriority;
    }

    public ObjectAnimation(int texture, int tickDuration, int priority) {
        this(texture,tickDuration,priority,false);
    }

    public ObjectAnimation(int texture, int tickDuration) {
        this(texture,tickDuration,0,false);
    }

    public ObjectAnimation(int texture) {
        this(texture,0,0,false);
    }

    public int getTexture() {
        return texture;
    }

    public int getTickDuration() {
        return tickDuration;
    }

    public int getPriority() {
        return priority;
    }

    public boolean keepIfSamePriority() {
        return keepIfSamePriority;
    }
}
