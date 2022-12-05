package dreamlink.audio.buffer.cache;

import dreamlink.audio.buffer.SoundBuffer;

public interface ISoundBufferRef {

    public SoundBuffer getSoundBuffer();

    public void setup();

    public void release();
    
}
