package objects.block.blockBehavior.Logic;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import objects.block.Block;
import objects.block.blockBehavior.BlockBehavior;
import tools.Vector;
import world.World;

public class BlockBehaviorDelay extends BlockBehaviorConnectedExit {
    private static final String DELAY = "delay";
    private int tick = 0;
    private ArrayList<Map.Entry<Integer, Boolean>> delays = new ArrayList<>();

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(DELAY, 1);
    }

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        tick++;
        while(!delays.isEmpty() && delays.get(0).getKey() <= tick) {
            if (delays.get(0).getValue())
                world.activate(getNetworkExit(block));
            else
                world.desactivate(getNetworkExit(block));
            delays.remove(0);
        }
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        int delay = getDelay(block);
        delays.add(new AbstractMap.SimpleEntry<>(tick + delay, true));
    }

    @Override
    public void onDesactivated(World world, Block block, Vector position, int network) {
        int delay = getDelay(block);
        delays.add(new AbstractMap.SimpleEntry<>(tick + delay, false));
    }

    @Override
    public BlockBehavior clone() {
        BlockBehaviorDelay behavior = (BlockBehaviorDelay)super.clone();
        behavior.delays = (ArrayList)delays.clone();
        return behavior;
    }

    public int getDelay(Block block) {
        Object state = block.getState(DELAY);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
}
