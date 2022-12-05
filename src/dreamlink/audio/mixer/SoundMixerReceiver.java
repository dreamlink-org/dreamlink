package dreamlink.audio.mixer;

import org.joml.Vector3f;

public class SoundMixerReceiver {

    public Vector3f position;
    public String locationKey;
    public float gainMultiplier;

    public SoundMixerReceiver() {
        this.position = new Vector3f();
    }

    public void setup() {
        SoundMixer.instance.addSoundReceiver(this);
    }
    
}
