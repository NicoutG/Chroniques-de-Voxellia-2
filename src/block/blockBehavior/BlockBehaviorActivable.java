package block.blockBehavior;

import block.Block;
import game.Game;
import tools.*;


public abstract class BlockBehaviorActivable extends BlockBehaviorConnected {

    public abstract void activate(Game game, Block block, Vector position, int network);

    public abstract void desactivate(Game game, Block block, Vector position, int network);

    @Override
    public void onActivated(Game game, Block block, Vector position, int network) {
        if (network == getNetwork(block)) {
            activate(game, block, position, network);
            block.setStateModification(ACTIVATION_STATE, true);
        }
    }

    @Override
    public void onDesactivated(Game game, Block block, Vector position, int network) {
        if (network == getNetwork(block)) {
            desactivate(game, block, position, network);
            block.setStateModification(ACTIVATION_STATE, false);
        }
    }

    
}
