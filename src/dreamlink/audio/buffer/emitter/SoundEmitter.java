package dreamlink.audio.buffer.emitter;

import dreamlink.audio.mixer.SoundMixerReceiver;

public class SoundEmitter {

    private static float epsilon = 0.001f;

    private ISoundEmitterProvider provider;

    public SoundEmitter(ISoundEmitterProvider provider) {
        this.provider = provider;
    }

    public float getGain(SoundMixerReceiver receiver) {
        if(!this.provider.getLocationKey().equals(receiver.locationKey)) {
            return 0f;
        }

        var distance = this.provider.getPosition().distance(receiver.position);
        var maxGain = this.provider.getBaseGain() * receiver.gainMultiplier;
        var minRadius = this.provider.getMinRadius();
        var maxRadius = this.provider.getMaxRadius();
        if(distance <= minRadius) {
            return maxGain;
        } else if(distance <= maxRadius) {
            var range = maxRadius - minRadius + SoundEmitter.epsilon;
            return (1f - (distance - minRadius) / range) * maxGain;
        } else {
            return 0f;
        }
    }
    
}
