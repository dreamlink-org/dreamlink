package dreamlink.zone.speaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;
import org.joml.Vector3i;

import dreamlink.audio.buffer.cache.ISoundBufferRef;
import dreamlink.audio.buffer.emitter.ISoundEmitterProvider;
import dreamlink.audio.buffer.emitter.SoundEmitter;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.utility.maths.Orientation;
import dreamlink.zone.IStamp;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.terrain.TerrainBlockData;

public class Speaker implements IStamp {

    private class InternalSpeakerEmitterProvider implements ISoundEmitterProvider {

        private Vector3f position;

        public InternalSpeakerEmitterProvider(Vector3i position) {
            this.position = new Vector3f(position);
        }

        @Override
        public Vector3f getPosition() {
            return this.position;
        }

        @Override
        public String getLocationKey() {
            return Speaker.this.directory.getName();
        }

        @Override
        public float getBaseGain() {
            return Speaker.this.gain;
        }

        @Override
        public float getMinRadius() {
            return Speaker.this.minRadius;
        }

        @Override
        public float getMaxRadius() {
            return Speaker.this.maxRadius;
        }
    }

    private static String stampType = "Speaker";
    
    private IZoneDirectory directory;
    private ISoundBufferRef soundBufferRef;
    public String name;
    public float gain;  
    public float minRadius;
    public float maxRadius;
    public TextureSample textureSample;
    private Map<Vector3i, SoundEmitter> emitterLookup;
    private List<Vector3i> positions;

    public float gainMultiplier;

    public Speaker(
        IZoneDirectory directory,
        ISoundBufferRef soundBufferRef,
        String name,
        float gain, 
        float minRadius, 
        float maxRadius,
        TextureSample textureSample
    ) {
        this.emitterLookup = new HashMap<>();
        this.positions = new ArrayList<>();
        this.directory = directory;
        this.soundBufferRef = soundBufferRef;
        this.name = name;
        this.gain = gain;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.textureSample = textureSample;
    }

    public Iterable<Vector3i> getSpeakerPositions() {
        return this.positions;
    }

    @Override
    public String getStampName() {
        return this.name;
    }

    @Override
    public String getStampType() {
        return Speaker.stampType;
    }

    @Override
    public ISpriteTemplate getStampSprite() {
        return this.textureSample != null
            ? this.textureSample
            : EntityTextureSample.missing;

    }

    @Override
    public void applyStamp(Vector3i position, Orientation orientation) {
        var speakerBlock = this.directory.getBlockSystem().speakerBlock;
        var blockData = new TerrainBlockData().set(
            speakerBlock.getBlockID(),
            orientation.orientationID,
            0, 0
        );

        this.directory.getTerrainSystem().setBlockData(position, blockData);
        this.directory.getSpeakerSystem().addSpeakerPosition(position, this);
        this.addEmitter(position);
    }

    public void addEmitter(Vector3i position) {
        position = new Vector3i(position);
        var provider = new InternalSpeakerEmitterProvider(position);
        var emitter = new SoundEmitter(provider);

        this.positions.add(position);
        this.emitterLookup.put(position, emitter);
        this.soundBufferRef.getSoundBuffer().addSoundEmitter(emitter);
    }

    public void destroy() {
        for(var position : this.positions) {
            var emitter = this.emitterLookup.remove(position);
            this.soundBufferRef.getSoundBuffer().removeSoundEmitter(emitter);
        }
    }

    public void removeEmitter(Vector3i position) {
        this.positions.remove(position);
        var emitter = this.emitterLookup.remove(position);
        this.soundBufferRef.getSoundBuffer().removeSoundEmitter(emitter);
    }

    public void pickup(Vector3i position) {
        this.removeEmitter(position);
        this.directory.getSpeakerSystem().removeSpeakerPosition(position);
        this.directory.getTerrainSystem().setBlockData(position, new TerrainBlockData());
    }

    @Override
    public String toString() {
        return String.format("Speaker(%s)", this.name);
    }
    
}
