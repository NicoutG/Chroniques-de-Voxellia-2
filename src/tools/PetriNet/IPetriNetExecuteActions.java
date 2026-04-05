package tools.PetriNet;

import objects.entity.Entity;
import objects.entity.EntityAction;
import tools.PetriNet.States.PetriNetBase;
import world.World;

public interface IPetriNetExecuteActions {
    public EntityAction[] getCurrentActions();

    public int getLastActionTick();

    public final static int MAX_TICK_WITHOUT_ACTION = 7;

    public static int getTickWithoutAction(PetriNetBase petriNet) {
        if (petriNet instanceof IPetriNetExecuteActions state)
            return World.getTick() - state.getLastActionTick();
        return Integer.MAX_VALUE;
    }
    
    public static void decideTextureDefault(Entity entity, EntityAction[] actions, int tickWithoutAction, int restTexture, int leftTexture, int rightTexture, int topTexture, int bottomTexture, int aboveTexture, int bellowTexture, int jumpTexture, int interactTexture) {
        if (actions.length <= 0 && 0 <= restTexture && tickWithoutAction >= MAX_TICK_WITHOUT_ACTION)
            entity.setIndexTexture(restTexture);
        for (EntityAction action : actions) {
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
}
