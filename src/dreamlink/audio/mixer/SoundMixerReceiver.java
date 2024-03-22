package dreamlink.audio.mixer;

import org.joml.Vector3f;

import dreamlink.logger.Logger;

public class SoundMixerReceiver {

    public Vector3f position;
    public String locationKey;
    public float gainMultiplier;

    public SoundMixerReceiver() {
        this.position = new Vector3f();
    }

    public void setup() {
        Logger.instance.debug(String.format("Setting up sound receiver: %s", this));
        SoundMixer.instance.addSoundReceiver(this);
    }

    @Override
    public String toString() {
        var hash = Integer.toHexString(this.hashCode());
        return String.format("SoundMixerReceiver(%s)", hash);
    }
    
}
