package dreamlink.audio.buffer;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;
import org.lwjgl.system.MemoryUtil;

import dreamlink.audio.SoundData;
import dreamlink.audio.buffer.emitter.SoundEmitter;
import dreamlink.audio.mixer.ISoundMixerMembership;
import dreamlink.audio.mixer.SoundMixer;
import dreamlink.logger.Logger;

public class SoundBuffer {

    private static int maxBufferSize = 1024 * 1024 * 64;
    private static ShortBuffer inputBuffer = MemoryUtil.memAllocShort(maxBufferSize);

    public int soundBufferID;
    public int sampleRate;
    public int capacity;
    public long duration;
    public boolean calculatedGain;

    private boolean isLooping;
    private ISoundMixerMembership membership;
    private long soundStartTime;
    private long soundEndTime;
    private List<SoundEmitter> soundEmitters;

    public SoundBuffer(boolean isLooping) {
        this.isLooping = isLooping;
        this.soundEmitters = new ArrayList<>();
    }

    public void addSoundEmitter(SoundEmitter emitter) {
        this.soundEmitters.add(emitter);
    }

    public Iterable<SoundEmitter> getSoundEmitters() {
        return this.soundEmitters;
    }

    public void removeSoundEmitter(SoundEmitter emitter) {
        this.soundEmitters.remove(emitter);
    }

    public void setup() {
        this.soundBufferID = AL10.alGenBuffers();
        this.membership = SoundMixer.instance.addSoundBuffer(this);
    }

    public boolean isPlaying() {
        return System.currentTimeMillis() < this.soundEndTime;
    }

    public long getPlayedTime() {
        return System.currentTimeMillis() - this.soundStartTime;
    }

    public void play() {
        this.soundStartTime = System.currentTimeMillis();
        this.soundEndTime = this.isLooping
            ? this.soundStartTime + this.duration
            : Long.MAX_VALUE;
    }

    public void bufferData(SoundData soundData) {
        Logger.instance.debug(String.format("Buffering data to sound buffer: %s", this));
        this.sampleRate = soundData.sampleRate;
        this.capacity = soundData.pcmData.length;
        this.duration = soundData.pcmData.length / soundData.sampleRate;

        SoundBuffer.inputBuffer.clear();
        SoundBuffer.inputBuffer.put(soundData.pcmData);
        SoundBuffer.inputBuffer.flip();

        AL10.alBufferData(
            this.soundBufferID, 
            soundData.channels == 1 
                ? AL10.AL_FORMAT_MONO16 
                : AL10.AL_FORMAT_STEREO16, 
            SoundBuffer.inputBuffer,
            soundData.sampleRate
        );
    }

    public void destroy() {
        Logger.instance.debug(String.format("Destroying sound buffer: %s", this));
        this.membership.unregister();
        AL10.alDeleteBuffers(this.soundBufferID);
    }

    @Override
    public String toString() {
        var hash = Integer.toHexString(this.hashCode());
        return String.format("SoundBuffer(%s)", hash);
    }
    
}
