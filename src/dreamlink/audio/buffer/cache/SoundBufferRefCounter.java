package dreamlink.audio.buffer.cache;

import dreamlink.audio.SoundData;
import dreamlink.audio.buffer.SoundBuffer;

public class SoundBufferRefCounter {

    private class InternalSoundBuffeRef implements ISoundBufferRef {

        @Override
        public SoundBuffer getSoundBuffer() {
            return SoundBufferRefCounter.this.soundBuffer;
        }

        @Override
        public void release() {
            SoundBufferRefCounter.this.refCount -= 1;
            if(SoundBufferRefCounter.this.refCount <= 0) {
                if(SoundBufferRefCounter.this.isSetup) {
                    SoundBufferRefCounter.this.soundBuffer.destroy();
                }
                SoundBufferRefCounter.this.directory.removeFromCache(SoundBufferRefCounter.this.hash);
            }
        }

        @Override
        public void setup() {
            if(!SoundBufferRefCounter.this.isSetup) {
                SoundBufferRefCounter.this.soundBuffer.setup();
                SoundBufferRefCounter.this.soundBuffer.bufferData(SoundBufferRefCounter.this.soundData);
                SoundBufferRefCounter.this.isSetup = true;
            }
        }
        
    }

    private ISoundBufferCacheDirectory directory;
    private String hash;
    private SoundData soundData;
    private SoundBuffer soundBuffer;
    private int refCount;
    private boolean isSetup;

    public SoundBufferRefCounter(
        ISoundBufferCacheDirectory directory, 
        String hash,
        SoundData soundData, 
        boolean isLooping
    ) {
        this.directory = directory;
        this.hash = hash;
        this.soundData = soundData;
        this.soundBuffer = new SoundBuffer(isLooping);
    }

    public void setup() {
    }

    public ISoundBufferRef getReference() {
        this.refCount += 1;
        return new InternalSoundBuffeRef();
    }


    
}
