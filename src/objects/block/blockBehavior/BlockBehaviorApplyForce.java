package objects.block.blockBehavior;

import objects.block.Block;
import objects.entity.Entity;
import tools.Vector;
import world.World;

public class BlockBehaviorApplyForce extends BlockBehavior {
    private static final String FORCE = "force";
    private double fx = 0;
    private double fy = 0;
    private double fz = 0;

    public BlockBehaviorApplyForce(double fx, double fy, double fz) {
        this.fx = fx;
        this.fy = fy;
        this.fz = fz;
    }

    public void onAttachTo(Block block) {
        block.setState(FORCE, new double[] {fx, fy, fz});
    }

    @Override
    public void onEntityClose(World world, Block block, Vector position, Entity entity) {
        if (block.collision(position, entity, entity.getPosition())) {
            double[] force = getForce(block);
            double percent = block.getCollision().getPourcentage2In1(position, entity.getCollision(), entity.getPosition());
            if (force != null)
                entity.addVelocity(percent * force[0], percent * force[1], percent * force[2]);
        }
    }

    public double[] getForce(Block block) {
        Object state = block.getState(FORCE);
        if (state != null && state instanceof double[] && ((double[])state).length == 3)
            return (double[])state;
        return null;
    }
}
