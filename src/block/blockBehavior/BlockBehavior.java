package block.blockBehavior;

import block.Block;
import entity.Entity;
import game.Game;

public abstract class BlockBehavior {

    public abstract void onAttachToBlock(Block block);

    public abstract BlockBehavior clone();

    public void onUpdate(Game game, Block block) {
    }

    public void onInteraction(Game game, Block block, Entity entity) {
    }

    public void onPush(Game game, Block block, int x, int y, int z) {
    }

    public void onActivated(Game game, Block block, int network) {
    }

    public void onDesactivated(Game game, Block block, int network) {
    }

    public void onEntityIn(Game game, Block block, Entity entity) {
    }

    public void onEntityCollision(Game game, Block block, Entity entity) {
    }


}
