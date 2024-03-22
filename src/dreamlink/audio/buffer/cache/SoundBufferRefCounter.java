package dreamlink.audio.buffer.cache;

import dreamlink.audio.SoundData;
import dreamlink.audio.buffer.SoundBuffer;
import dreamlink.logger.Logger;

public class SoundBufferRefCounter {

    private class InternalSoundBuffeRef implements ISoundBufferRef {

        @Override
        public SoundBuffer getSoundBuffer() {
            return SoundBufferRefCounter.this.soundBuffer;
        }

        @Override
        public void release() {
            SoundBufferRefCounter.this.refCount -= 1;
            var msg = String.format("Releasing reference for: %s", this);
            Logger.instance.debug(msg);

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
                var msg = String.format("Setting up sound buffer for: %s", this);
                Logger.instance.debug(msg);
                SoundBufferRefCounter.this.soundBuffer.setup();
                SoundBufferRefCounter.this.soundBuffer.bufferData(SoundBufferRefCounter.this.soundData);
                SoundBufferRefCounter.this.isSetup = true;
            }
        }

        @Override
        public String toString() {
            var counterHash = Integer.toHexString(SoundBufferRefCounter.this.hashCode());
            var hash = Integer.toHexString(this.hashCode());
            return String.format("SoundBufferRef(%s, %s)", counterHash, hash);
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

    public ISoundBufferRef getReference() {
        var msg = String.format("Creating new reference for: %s", SoundBufferRefCounter.this);
        Logger.instance.debug(msg);
        this.refCount += 1;
        return new InternalSoundBuffeRef();
    }

    public String toString() {
        var hash = Integer.toHexString(this.hashCode());
        return String.format("SoundBufferRefCounter(%s)", hash);
    }


    
}
