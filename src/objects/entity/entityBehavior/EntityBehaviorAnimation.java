package objects.entity.entityBehavior;

import objects.entity.*;
import world.World;

public class EntityBehaviorAnimation extends EntityBehavior {

    private int restTexture = -1;
    private int leftTexture = -1;
    private int rightTexture = -1;
    private int topTexture = -1;
    private int bottomTexture = -1;
    private int aboveTexture = -1;
    private int bellowTexture = -1;
    private int jumpTexture = -1;
    private int interactTexture = -1;

    private int tickWithoutAction;
    private static int MAX_TICK_WITHOUT_ACTION = 7;

    public EntityBehaviorAnimation(int restTexture, int leftTexture, int rightTexture, int topTexture, int bottomTexture, int aboveTexture, int bellowTexture, int jumpTexture, int interactTexture) {
        this.restTexture = restTexture;
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
        this.topTexture = topTexture;
        this.bottomTexture = bottomTexture;
        this.aboveTexture = aboveTexture;
        this.bellowTexture = bellowTexture;
        this.jumpTexture = jumpTexture;
        this.interactTexture = interactTexture;
    }

    @Override
    public void onUpdate(World world, Entity entity) {
        tickWithoutAction++;
        if (MAX_TICK_WITHOUT_ACTION <= tickWithoutAction && 0 <= restTexture)
            entity.setIndexTexture(restTexture);
    }
    
    @Override
    public void onAction(World world, Entity entity, EntityAction action) {
        tickWithoutAction = 0;
        switch (action) {
            case LEFT: {
                if (0 <= leftTexture)
                    entity.setIndexTexture(leftTexture);
            };break;
            case RIGHT: {
                if (0 <= rightTexture)
                    entity.setIndexTexture(rightTexture);
            };break;
            case TOP:  {
                if (0 <= topTexture)
                    entity.setIndexTexture(topTexture);
            };break;
            case BOTTOM: {
                if (0 <= bottomTexture)
                    entity.setIndexTexture(bottomTexture);
            };break;
            case ABOVE:  {
                if (0 <= aboveTexture)
                    entity.setIndexTexture(aboveTexture);
            };break;
            case BELOW:  {
                if (0 <= bellowTexture)
                    entity.setIndexTexture(bellowTexture);
            };break;
            case JUMP:  {
                if (0 <= jumpTexture)
                    entity.setIndexTexture(jumpTexture);
            };break;
            case INTERACT:  {
                if (0 <= interactTexture)
                    entity.setIndexTexture(interactTexture);
            };break;
            default:break;
        }
    }
}
