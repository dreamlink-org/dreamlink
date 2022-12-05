package dreamlink.audio.buffer.cache;

import java.util.HashMap;
import java.util.Map;

import dreamlink.audio.SoundData;

public class SoundBufferCache {
    
    private class InternalSoundBufferCacheDirectory implements ISoundBufferCacheDirectory {

        public void removeFromCache(String hash) {
            SoundBufferCache.this.soundBufferCache.remove(hash);
        }
        
    }

    public static SoundBufferCache instance = new SoundBufferCache();

    private Map<String, SoundBufferRefCounter> soundBufferCache = new HashMap<>();
    private ISoundBufferCacheDirectory directory;

    public SoundBufferCache() {
        this.directory = new InternalSoundBufferCacheDirectory();
    }

    public ISoundBufferRef getSoundBufferRef(String soundHash, SoundData soundData) {
        if(!this.soundBufferCache.containsKey(soundHash)) {
            this.soundBufferCache.put(
                soundHash, 
                new SoundBufferRefCounter(
                    this.directory, 
                    soundHash, 
                    soundData, 
                    true
                )
            );
        }

        return this.soundBufferCache.get(soundHash).getReference();
    }
    
}
