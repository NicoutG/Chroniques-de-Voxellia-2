package objects.block.blockBehavior;

import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorText extends BlockBehavior {
    protected final static String TEXT = "text";
    protected final static String TEXTS = "texts";
    private int index = 0;

    @Override
    public void onAttachTo(Block block) {
        block.setState(TEXT, null);
        block.setState(TEXTS, new String[0]);
    }

    @Override
    public void onInteraction(World world, Block block, Vector position, Entity entity) {
        String[] texts = getTexts(block);
        if (index >= texts.length) {
            block.setState(TEXT, null);
            index = 0;
        } else {
            block.setState(TEXT, texts[index]);
            index++;
        }
    }

    public String getText(Block block) {
        Object state = block.getState(TEXT);
        if (state != null && state instanceof String)
            return (String) state;
        return null;
    }

    public String[] getTexts(Block block) {
        Object state = block.getState(TEXTS);
        if (state != null && state instanceof String[])
            return (String[]) state;
        return null;
    }
}
