package dreamlink.zone;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import dreamlink.audio.SoundData;
import dreamlink.audio.buffer.cache.ISoundBufferRef;
import dreamlink.audio.buffer.cache.SoundBufferCache;
import dreamlink.logger.Logger;
import dreamlink.utility.FileFns;
import dreamlink.utility.HashFns;

public class ZoneSoundSystem {

    private static Path soundsDirectory = Paths.get("sounds");
    private static String soundSuffix = ".ogg";

    private Map<String, ISoundBufferRef> soundBufferRefLookup;

    public ZoneSoundSystem() {
        this.soundBufferRefLookup = new HashMap<>();
    }

    public ISoundBufferRef getSoundBufferRefByName(String name) {
        return this.soundBufferRefLookup.get(name);
    }

    public void loadData(Path zonePath) {
        var soundDirectoryFile = zonePath
            .resolve(ZoneSoundSystem.soundsDirectory)
            .toFile();

        if(!soundDirectoryFile.exists() || !soundDirectoryFile.isDirectory()) {
            return;
        }

        for(var soundFile : soundDirectoryFile.listFiles(File::isFile)) {
            var fileName = soundFile.getName();
            if(!fileName.endsWith(ZoneSoundSystem.soundSuffix)) {
                continue;
            }

            Logger.instance.debug(String.format("Loading sound: %s", fileName));
            var soundBytes = FileFns.readBytesFromFile(soundFile);
            var soundData = SoundData.fromBytes(soundBytes);
            var hash = HashFns.generateHash(soundBytes);

            var soundBufferRef = SoundBufferCache.instance.getSoundBufferRef(
                hash,
                soundData
            );

            this.soundBufferRefLookup.put(fileName, soundBufferRef);
        }
    }

    public void setup() {
        for(var soundBufferRef : this.soundBufferRefLookup.values()) {
            soundBufferRef.setup();
        }
    }

    public void destroy() {
        for(var soundBufferRef : this.soundBufferRefLookup.values()) {
            soundBufferRef.release();
        }
    }
    
}
