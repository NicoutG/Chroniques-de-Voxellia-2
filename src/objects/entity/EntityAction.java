package objects.entity;

public enum EntityAction {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
    ABOVE,
    BELOW,
    JUMP,
    INTERACT;

    public String toString() {
        switch(this) {
            case LEFT: return "left";
            case RIGHT: return "right";
            case TOP: return "top";
            case BOTTOM: return "bottom";
            case ABOVE: return "above";
            case BELOW: return "below";
            case JUMP: return "jump";
            case INTERACT: return "interact";
        }
        return "unknown";
    }
}
