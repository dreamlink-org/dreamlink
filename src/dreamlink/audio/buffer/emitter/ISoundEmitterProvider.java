package dreamlink.audio.buffer.emitter;

import org.joml.Vector3f;

public interface ISoundEmitterProvider {

    public Vector3f getPosition();

    public String getLocationKey();

    public float getBaseGain();

    public float getMinRadius();

    public float getMaxRadius();
    
}
