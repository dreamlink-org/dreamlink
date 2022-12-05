package dreamlink.audio;

import org.lwjgl.openal.AL10;

import dreamlink.audio.buffer.SoundBuffer;

public class SoundSource {

    public int soundSourceID;

    public void setup() {
        this.soundSourceID = AL10.alGenSources();
        AL10.alSourcei(this.soundSourceID, AL10.AL_LOOPING, AL10.AL_TRUE);
    }

    public void play(SoundBuffer soundBuffer) {
        AL10.alSourcei(this.soundSourceID, AL10.AL_BUFFER, soundBuffer.soundBufferID);
        AL10.alSourcePlay(this.soundSourceID);
    }

    public void stop() {
        AL10.alSourceStop(this.soundSourceID);
    }

    public void setGain(float gain) {
        AL10.alSourcef(this.soundSourceID, AL10.AL_GAIN, gain);
    }

    public void destroy() {
        AL10.alDeleteSources(this.soundSourceID);
    }
    
}
