package objects.block.blockBehavior;

import audio.SoundManager;
import audio.SoundType;
import objects.block.Block;
import tools.Vector;
import world.World;

public class BlockBehaviorActivableSound extends BlockBehaviorConnected {

    private final static String SOUND_OFF = "soundOff";
    private final static String SOUND_ON = "soundOn";

    private final String soundOff;
    private final String soundOn;

    public BlockBehaviorActivableSound(SoundType soundOff, SoundType soundOn) {
        if (soundOff == null)
            this.soundOff = null;
        else
            this.soundOff = soundOff.getPath();
        if (soundOn == null)
            this.soundOn = null;
        else
            this.soundOn = soundOn.getPath();
    }

    @Override
    public void onAttachTo(Block block) {
        super.onAttachTo(block);
        block.setState(SOUND_OFF, soundOff);
        block.setState(SOUND_ON, soundOn);
    }

    @Override
    public void onActivated(World world, Block block, Vector position, int network) {
        if (getNetwork(block) == network) {
            String soundOn = getSoundOn(block);
            if (soundOn != null)
                SoundManager.playSoundFromCoordinates(SoundType.getSoundType(soundOn), position.x, position.y, position.z);
        }
    }

    @Override
    public void onDesactivated(World world, Block block, Vector position, int network) {
        if (getNetwork(block) == network) {
            String soundOff = getSoundOff(block);
            if (soundOff != null)
                SoundManager.playSoundFromCoordinates(SoundType.getSoundType(soundOff), position.x, position.y, position.z);
        }
    }

    public String getSoundOff(Block block) {
        Object state = block.getState(SOUND_OFF);
        if (state != null && state instanceof String)
            return (String)state;
        return null;
    }

    public String getSoundOn(Block block) {
        Object state = block.getState(SOUND_ON);
        if (state != null && state instanceof String)
            return (String)state;
        return null;
    }

}
