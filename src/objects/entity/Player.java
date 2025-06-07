package objects.entity;

import audio.SoundManager;
import audio.SoundType;
import world.World;

public class Player extends Entity {

    public Player(EntityType type, double x, double y, double z) {
        super(type, x, y, z);
    }

    @Override
    public void onDeath(World world) {
        SoundManager.playSound(SoundType.DEATH);
        super.onDeath(world);
    }
}
