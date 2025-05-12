package block.blockBehavior;

import block.Block;
import entity.Entity;
import game.Game;
import tools.*;;

public abstract class BlockBehavior {

    public abstract void onAttachToBlock(Block block);

    public abstract BlockBehavior clone();

    public void onUpdate(Game game, Block block) {
    }

    public void onInteraction(Game game, Block block, Vector position, Entity entity) {
    }

    public void onPush(Game game, Block block, Vector position, int depX, int depY, int depZ) {
    }

    public void onActivated(Game game, Block block, Vector position, int network) {
    }

    public void onDesactivated(Game game, Block block, Vector position, int network) {
    }

    public void onEntityIn(Game game, Block block, Vector position, Entity entity) {
    }

    public void onEntityCollision(Game game, Block block, Vector position, Entity entity) {
    }


}
