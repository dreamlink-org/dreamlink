package dreamlink.audio.mixer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import dreamlink.Config;
import dreamlink.audio.SoundSource;
import dreamlink.audio.buffer.SoundBuffer;

public class SoundMixer {

    private class InternalModulatedSoundBuffer implements ISoundMixerMembership {
        public SoundBuffer soundBuffer;
        public float calculatedGain;

        public InternalModulatedSoundBuffer(SoundBuffer soundBuffer) {
            this.soundBuffer = soundBuffer;
            this.calculatedGain = 0f;
        }

        public float getGain() {
            return this.calculatedGain;
        }

        public void calculateGain() {
            var gain = 0f;
            for(var receiver : SoundMixer.this.soundReceivers) {
                for(var emitter : soundBuffer.getSoundEmitters()) {
                    var emitterGain = emitter.getGain(receiver);
                    gain = Math.max(gain, emitterGain);
                }
            }
            this.calculatedGain = gain;
        }

        @Override
        public void unregister() {
            SoundMixer.this.allSoundBuffers.remove(this);
            if(SoundMixer.this.activeSoundBuffers.containsKey(this)) {
                var soundSource = SoundMixer.this.activeSoundBuffers.get(this);
                soundSource.stop();
                SoundMixer.this.activeSoundBuffers.remove(this);
                SoundMixer.this.freeSoundSources.add(soundSource);
            }
        }
    }

    private static Comparator<InternalModulatedSoundBuffer> gainComparator = Comparator.comparing(InternalModulatedSoundBuffer::getGain);

    public static SoundMixer instance = new SoundMixer();

    private List<SoundSource> allSoundSources;
    private Queue<SoundSource> freeSoundSources;
    private List<InternalModulatedSoundBuffer> allSoundBuffers;
    private Map<InternalModulatedSoundBuffer, SoundSource> activeSoundBuffers;
    private List<SoundMixerReceiver> soundReceivers;

    public SoundMixer() {
        this.allSoundSources = new ArrayList<>();
        this.freeSoundSources = new ArrayDeque<>();
        this.allSoundBuffers = new ArrayList<>();
        this.activeSoundBuffers = new HashMap<>();
        this.soundReceivers = new ArrayList<>();

        for(var ix = 0; ix < Config.instance.maxAudioSources; ix += 1) {
            var soundSource = new SoundSource();
            this.allSoundSources.add(soundSource);
            this.freeSoundSources.add(soundSource);
        }
    }
    
    public void addSoundReceiver(SoundMixerReceiver receiver) {
        this.soundReceivers.add(receiver);
    }

    public void setup() {
        for(var soundSource : this.allSoundSources) {
            soundSource.setup();
        }
    }

    public ISoundMixerMembership addSoundBuffer(SoundBuffer sound) { 
        var modulatedSoundBuffer = new InternalModulatedSoundBuffer(sound);
        this.allSoundBuffers.add(modulatedSoundBuffer);
        return modulatedSoundBuffer;
    }

    public void update() {
        this.allSoundBuffers.sort(SoundMixer.gainComparator);

        for(var soundBuffer : this.allSoundBuffers) {
            soundBuffer.calculateGain();
        }

        var count = 0;
        for(var soundBuffer : this.allSoundBuffers) {

            var isActiveSoundBuffer = count < Config.instance.maxAudioSources;
            count += 1;

            // If the sound is not active, yet is still in possession of a sound source, 
            // we should release that sound source so an active sound can use it.
            // N.B. As the sounds are sorted by gain, we will do all the freeing before
            // we need to start claiming sound sources, thus we should always have sound
            // sources available for the active sounds.
            if(!isActiveSoundBuffer) {
                if(this.activeSoundBuffers.containsKey(soundBuffer)) {
                    var soundSource = this.activeSoundBuffers.get(soundBuffer);
                    soundSource.stop();
                    this.activeSoundBuffers.remove(soundBuffer);
                    this.freeSoundSources.add(soundSource);
                }
                continue;
            }

            // If we have an active sound that hasn't yet possessed a sound source,
            // then we need to grab a free source and set it to start playing.
            if (!this.activeSoundBuffers.containsKey(soundBuffer)) {
                var source = this.freeSoundSources.remove();
                source.play(soundBuffer.soundBuffer);
                this.activeSoundBuffers.put(soundBuffer, source);
            }

            var source = this.activeSoundBuffers.get(soundBuffer);
            source.setGain(soundBuffer.getGain());
        }
    }

    public void destroy() {
        for(var soundSource : this.allSoundSources) {
            soundSource.destroy();
        }
    }

    
}
