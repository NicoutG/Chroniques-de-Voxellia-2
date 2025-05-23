package objects.block.blockBehavior;

import java.util.ArrayList;
import java.util.Random;

import objects.block.Block;
import tools.Vector;
import world.World;

public class BlockBehaviorLiquid extends BlockBehavior {
    private int tick;
    private final int MAX_LIQUID;
    private final int VISCOSITY;

    protected final static String LIQUID = "liquid";
    protected final static String LIQUID_NEXT = "liquidNext";

    public BlockBehaviorLiquid(int maxLiquid, int viscosity) {
        MAX_LIQUID = maxLiquid;
        VISCOSITY = viscosity;
        Random random = new Random();
        tick = random.nextInt(VISCOSITY);
    }

    @Override
    public void onStart(World world, Block block, Vector position) {
        setCollisionTexture(world, block, (int)position.x, (int)position.y, (int)position.z, getLiquid(block));
    }

    @Override
    public void onAttachTo(Block block) {
        block.setState(LIQUID, MAX_LIQUID);
        block.setState(LIQUID_NEXT, 0);
    }

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        int x = (int)position.x;
        int y = (int)position.y;
        int z = (int)position.z; 
        if (tick % VISCOSITY == 0) {
            propageLiquid(world, block, x, y, z);
            endPropageLiquid(world, block, x, y, z);
        }
        tick++;
    }

    private void propageLiquid(World world, Block block, int x, int y, int z) {
        Block[][][] blocks = world.getBlocks();
        int liquid = getLiquid(block);
        // falling liquid
        if (z > 0) {
            Block blockDown = world.getBlock(x, y, z - 1);
            if (blockDown == null) {
                Block newBlock = world.getBlock(block.getName());
                blocks[x][y][z - 1] = newBlock;
                newBlock.setState(LIQUID, Math.min(liquid,MAX_LIQUID));
                block.setState(LIQUID, 0);
                newBlock.onStart(world, new Vector(x,y,z - 1));
                return;
            }
            else if (blockDown.areSameType(block)) {
                int liquidDown = getLiquid(blockDown);
                if (liquidDown < MAX_LIQUID) {
                    int maxLiquidDif = Math.min(liquid,MAX_LIQUID - liquidDown);
                    blockDown.setState(LIQUID, liquidDown + maxLiquidDif);
                    block.setState(LIQUID, liquid - maxLiquidDif);
                    return;
                }
            }
        }
        // on the side
        if (1 < liquid) {
            ArrayList<int[]> possibleFlows = getPossibleFlows(blocks, block, x, y, z, liquid);
            if (0 < possibleFlows.size()) {
                Random random = new Random();
                int selectedFlow = random.nextInt(possibleFlows.size());
                int[] flow = possibleFlows.get(selectedFlow);
                int xFlow = flow[0];
                int yFlow = flow[1];
                Block blockFlow = blocks[xFlow][yFlow][z];
                boolean isNewBlock = false;
                if (blockFlow == null) {
                    isNewBlock = true;
                    blockFlow = world.getBlock(block.getName());
                    blockFlow.setState(LIQUID, 0);
                    blocks[xFlow][yFlow][z] = blockFlow;
                }
                if (x < xFlow || y < yFlow)
                    blockFlow.setState(LIQUID_NEXT, getLiquidNext(blockFlow) + 1);
                else {
                    int liquidFlow = getLiquid(blockFlow) + 1;
                    blockFlow.setState(LIQUID, liquidFlow);
                    setCollisionTexture(world, blockFlow, xFlow, yFlow, z, liquidFlow);
                }
                if (isNewBlock)
                    blockFlow.onStart(world, new Vector(xFlow, yFlow, z));
                block.setState(LIQUID, liquid - 1);
            }
        }
    }

    private ArrayList<int[]> getPossibleFlows(Block[][][] blocks, Block block, int x, int y, int z, int liquid) {
        ArrayList<int[]> possibleFlows = new ArrayList<>();
        int xSide,ySide;
        xSide = x;
        ySide = y - 1;
        if (isPossibleFlow(blocks, block, xSide, ySide, z, liquid))
            possibleFlows.add(new int[] {xSide,ySide});
        xSide = x - 1;
        ySide = y;
        if (isPossibleFlow(blocks, block, xSide, ySide, z, liquid))
            possibleFlows.add(new int[] {xSide,ySide});
        xSide = x + 1;
        ySide = y;
        if (isPossibleFlow(blocks, block, xSide, ySide, z, liquid))
            possibleFlows.add(new int[] {xSide,ySide});
        xSide = x;
        ySide = y + 1;
        if (isPossibleFlow(blocks, block, xSide, ySide, z, liquid))
            possibleFlows.add(new int[] {xSide,ySide});
        return possibleFlows;
    }

    private boolean isPossibleFlow(Block[][][] blocks, Block block, int xSide, int ySide, int z, int liquid) {
        if (0 <= xSide && xSide < blocks.length && 0 <= ySide && ySide < blocks[xSide].length) {
            Block blockSide = blocks[xSide][ySide][z];
            if (blockSide == null)
                return true;
            else
                if(blockSide.areSameType(block)) {
                    int liquidSide = getLiquid(blockSide) + getLiquidNext(blockSide);
                    if (liquidSide < liquid)
                        return true;
                }
        }
        return false;
    }

    private void endPropageLiquid(World world, Block block, int x, int y, int z) {
        int liquidNext = getLiquidNext(block);
        // add the liquid added by previous update
        int liquid = Math.min(getLiquid(block) + liquidNext, MAX_LIQUID);
        if (liquidNext > 0) {
            block.setState(LIQUID, liquid);
            block.setState(LIQUID_NEXT, 0);
        }
        if (liquid <= 0) {
            world.getBlocks()[x][y][z] = null;
            return;
        }
        setCollisionTexture(world, block, x, y, z, liquid);
        if(0 < z) {
            Block blockDown = world.getBlock(x,y,z - 1);
            if (blockDown != null && blockDown.areSameType(block))
                setCollisionTexture(world, blockDown, x, y, z - 1, getLiquid(blockDown));
        }
    }

    private void setCollisionTexture(World world, Block block, int x, int y, int z, int liquid) {
        Block blockUp = world.getBlock(x, y, z + 1);
        if (blockUp != null && blockUp.areSameType(block)) {
            block.setIndexCollision(MAX_LIQUID - 1);
            block.setIndexTexture(MAX_LIQUID - 1);
        }
        else {
            block.setIndexCollision(liquid - 1);
            block.setIndexTexture(liquid - 1);
        }
    }

    public int getLiquid(Block block) {
        Object state = block.getState(LIQUID);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }

    public int getLiquidNext(Block block) {
        Object state = block.getState(LIQUID_NEXT);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }
    
}
