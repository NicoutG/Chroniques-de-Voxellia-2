package objects.block.blockBehavior;

import model.world.World;
import objects.block.Block;
import tools.*;


public class BlockBehaviorActivableLight extends BlockBehaviorActivable {
    protected final static String LIGHT = "light";

    public void activate(World world, Block block, Vector position, int network) {
        block.setState(LIGHT, true);
    }

    public void desactivate(World world, Block block, Vector position, int network) {
        block.setState(LIGHT, false);
    }

    @Override
    public void onAttachToBlock(Block block) {
        super.onAttachToBlock(block);
        block.setState(LIGHT, false);
    }

    
}
