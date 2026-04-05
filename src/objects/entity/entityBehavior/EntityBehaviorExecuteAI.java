package objects.entity.entityBehavior;

import objects.entity.Entity;
import objects.entity.entityBehavior.AI.utils.AI;
import objects.entity.entityBehavior.AI.utils.AICreator;
import world.World;

public class EntityBehaviorExecuteAI extends EntityBehavior {
    private final AICreator<? extends AI> aiCreator;
    private AI ai = null;

    public final static String STATE = "state";

    public EntityBehaviorExecuteAI(AICreator<? extends AI> aiCreator) {
        this.aiCreator = aiCreator;
    }

    @Override
    public void onAttachTo(Entity entity) {
        super.onAttachTo(entity);
        entity.setState(STATE, 0);
    }

    @Override
    public void onStart(World world,Entity entity) {
        ai = aiCreator.create(world, entity);
    }
    
    @Override
    public void onUpdate(World world, Entity entity) {
        if (ai != null) {
            ai.tick();
            entity.setState(STATE, ai.getCurrentState());
        }
    }

    public int getCurrentState(Entity entity) {
        Object state = entity.getState(STATE);
        if (state instanceof Integer)
            return (Integer) state;
        return 0;
    }
}
