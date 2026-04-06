package tools.PetriNet;

import objects.ObjectAnimation;
import objects.entity.Entity;
import objects.entity.EntityAction;

public interface IPetriNetExecuteActions {
    public EntityAction[] getCurrentActions();

    public final static int MAX_TICK_WITHOUT_ACTION = 7;
    
    public static void decideTextureDefault(Entity entity, EntityAction[] actions, int restTexture, int leftTexture, int rightTexture, int topTexture, int bottomTexture, int aboveTexture, int bellowTexture, int jumpTexture, int interactTexture) {
        if (actions.length <= 0)
            entity.playAnimation(new ObjectAnimation(restTexture, 0, 0));
        for (EntityAction action : actions) {
            switch (action) {
                case LEFT: {
                    if (0 <= leftTexture)
                        entity.playAnimation(new ObjectAnimation(leftTexture, MAX_TICK_WITHOUT_ACTION, 1));
                };break;
                case RIGHT: {
                    if (0 <= rightTexture)
                        entity.playAnimation(new ObjectAnimation(rightTexture, MAX_TICK_WITHOUT_ACTION, 1));
                };break;
                case TOP:  {
                    if (0 <= topTexture)
                        entity.playAnimation(new ObjectAnimation(topTexture, MAX_TICK_WITHOUT_ACTION, 1));
                };break;
                case BOTTOM: {
                    if (0 <= bottomTexture)
                        entity.playAnimation(new ObjectAnimation(bottomTexture, MAX_TICK_WITHOUT_ACTION, 1));
                };break;
                case ABOVE:  {
                    if (0 <= aboveTexture)
                        entity.playAnimation(new ObjectAnimation(aboveTexture, MAX_TICK_WITHOUT_ACTION, 1));
                };break;
                case BELOW:  {
                    if (0 <= bellowTexture)
                        entity.playAnimation(new ObjectAnimation(bellowTexture, MAX_TICK_WITHOUT_ACTION, 1));
                };break;
                case JUMP:  {
                    if (0 <= jumpTexture)
                        entity.playAnimation(new ObjectAnimation(jumpTexture, MAX_TICK_WITHOUT_ACTION, 2));
                };break;
                case INTERACT:  {
                    if (0 <= interactTexture)
                        entity.playAnimation(new ObjectAnimation(interactTexture, MAX_TICK_WITHOUT_ACTION, 2));
                };break;
                default:break;
            }
        }
    }
}
