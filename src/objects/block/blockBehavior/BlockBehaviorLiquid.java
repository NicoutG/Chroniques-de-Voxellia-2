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
    protected final static String CONFLICTS = "conflicts";

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
        block.setState(CONFLICTS, new ArrayList<BlockFlow>());
    }

    @Override
    public void onUpdate(World world, Block block, Vector position) {
        int x = (int)position.x;
        int y = (int)position.y;
        int z = (int)position.z; 
        if (tick % VISCOSITY == 0)
            propageLiquid(world, block, x, y, z);
        tick++;
    }

    private void propageLiquid(World world, Block block, int x, int y, int z) {
        Block[][][] blocks = world.getBlocks();
        int liquid = getLiquid(block);
        if (liquid <= 0)
            if (getConflicts(block).isEmpty()) {
                blocks[x][y][z] = null;
                updateLiquid(world, block, x, y, z, 0);
                return;
            }
        
        // falling liquid
        if (z > 0) {
            Block blockDown = world.getBlock(x, y, z - 1);
            if (blockDown == null) {
                blockDown = world.getBlock(block.getName());
                blocks[x][y][z - 1] = blockDown;
                blockDown.setState(LIQUID, 0);
                blockDown.onStart(world, new Vector(x,y,z - 1));
            }
            if (blockDown.areSameType(block)) {
                int liquidDown = getLiquid(blockDown);
                if (liquidDown < MAX_LIQUID) {
                    ArrayList<BlockFlow> conflicts = getConflicts(blockDown);
                    if (conflicts.isEmpty())
                        world.executeAfterUpdate(() -> solveConflicts(world, x, y , z-1));
                    conflicts.add(new BlockFlow(block, x, y, z));
                    return;
                }
            }
        }
        
        // on the side
        if (1 < liquid || (0 < z && blocks[x][y][z - 1] != null && block.areSameType(blocks[x][y][z - 1]))) {
            ArrayList<int[]> possibleFlows = getPossibleFlows(blocks, block, x, y, z, liquid);
            if (0 < possibleFlows.size()) {
                Random random = new Random();
                int selectedFlow = random.nextInt(possibleFlows.size());
                int[] flow = possibleFlows.get(selectedFlow);
                int xFlow = flow[0];
                int yFlow = flow[1];
                Block blockFlow = blocks[xFlow][yFlow][z];
                if (blockFlow == null) {
                    blockFlow = world.getBlock(block.getName());
                    blockFlow.setState(LIQUID, 0);
                    blocks[xFlow][yFlow][z] = blockFlow;
                }
                ArrayList<BlockFlow> conflicts = getConflicts(blockFlow);
                if (conflicts.isEmpty())
                    world.executeAfterUpdate(() -> solveConflicts(world, xFlow, yFlow , z));
                conflicts.add(new BlockFlow(block, x, y, z));
            }
        }
    }

    private ArrayList<int[]> getPossibleFlows(Block[][][] blocks, Block block, int x, int y, int z, int liquid) {
        ArrayList<int[]> possibleFlows = new ArrayList<>();
        int xSide,ySide;
        xSide = x;
        ySide = y - 1;
        addPossibleFlow(blocks, block, xSide, ySide, z, liquid, possibleFlows);
        xSide = x - 1;
        ySide = y;
        addPossibleFlow(blocks, block, xSide, ySide, z, liquid, possibleFlows);
        xSide = x + 1;
        ySide = y;
        addPossibleFlow(blocks, block, xSide, ySide, z, liquid, possibleFlows);
        xSide = x;
        ySide = y + 1;
        addPossibleFlow(blocks, block, xSide, ySide, z, liquid, possibleFlows);
        return possibleFlows;
    }

    private int getPossibleFlow(Block[][][] blocks, Block block, int xSide, int ySide, int z) {
        if (0 <= xSide && xSide < blocks.length && 0 <= ySide && ySide < blocks[xSide].length) {
            Block blockSide = blocks[xSide][ySide][z];
            if (blockSide == null)
                return 0;
            else
                if(blockSide.areSameType(block))
                    return getLiquid(blockSide);
        }
        return MAX_LIQUID;
    }

    private void addPossibleFlow(Block[][][] blocks, Block block, int xSide, int ySide, int z, int liquid, ArrayList<int[]> possibleFlows) {
        int liquidSide = getPossibleFlow(blocks, block, xSide, ySide, z);
        if (liquidSide < liquid) {
            if (possibleFlows.isEmpty())
                possibleFlows.add(new int[] {xSide,ySide,liquidSide});
            else {
                int minLiquid = possibleFlows.get(0)[2];
                if (liquidSide < minLiquid)
                    possibleFlows.clear();
                if (liquidSide <= minLiquid)
                    possibleFlows.add(new int[] {xSide,ySide,liquidSide});
            }
        }
    }

    private void updateLiquid(World world, Block block, int x, int y, int z, int liquid) {
        block.setState(LIQUID, liquid);
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

    private void solveConflicts(World world, int x, int y, int z) {
        Block block = world.getBlocks()[x][y][z];
        ArrayList<BlockFlow> conflicts = getConflicts(block);
        if (!conflicts.isEmpty()) {
            int liquid = getLiquid(block);
            for (BlockFlow confBlockFlow : conflicts) {
                if (z < confBlockFlow.z) {
                    Block confBlock = confBlockFlow.block;
                    int liquidConf = getLiquid(confBlock);
                    int diff = Math.min(liquidConf, MAX_LIQUID - liquid);
                    liquid += diff;
                    liquidConf = liquidConf - diff;
                    updateLiquid(world, confBlock, x, y, z + 1, liquidConf);
                    conflicts.remove(confBlockFlow);
                    break;
                }
            }
            if (!conflicts.isEmpty() && liquid < MAX_LIQUID) {
                Random random = new Random();
                int selectedFlow = random.nextInt(conflicts.size());
                BlockFlow confBlockFlow = conflicts.get(selectedFlow);
                Block confBlock = confBlockFlow.block;
                int liquidConf = getLiquid(confBlock);
                liquid += 1;
                liquidConf = liquidConf - 1;
                updateLiquid(world, confBlock, confBlockFlow.x, confBlockFlow.y, confBlockFlow.z, liquidConf);
            }

            updateLiquid(world, block, x, y, z, liquid);
            conflicts.clear();
        }
    }

    public int getLiquid(Block block) {
        Object state = block.getState(LIQUID);
        if (state != null && state instanceof Integer)
            return (int)state;
        return -1;
    }

    public ArrayList<BlockFlow> getConflicts(Block block) {
        Object state = block.getState(CONFLICTS);
        if (state != null && state instanceof ArrayList)
            return (ArrayList<BlockFlow>)state;
        return null;
    }
    
    public record BlockFlow(Block block, int x, int y, int z) {}
}
