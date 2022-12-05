package dreamlink.zone.speaker;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3i;
import org.json.JSONArray;
import org.json.JSONObject;

import dreamlink.logger.Logger;
import dreamlink.utility.FileFns;
import dreamlink.utility.JSONFns;
import dreamlink.utility.maths.FloatMaths;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.ZoneLoadException;

public class ZoneSpeakerSystem {

    private static Path speakersDirectory = Paths.get("speakers");
    private static String speakerConfigSuffix = ".json";

    private static Path generatedSpeakerConfigPath = Paths.get(".gen/speakers.json");

    private IZoneDirectory directory;
    private List<Speaker> speakers;
    private Map<String, Speaker> speakerLookup;
    private Map<Vector3i, Speaker> speakerPositionLookup;

    public ZoneSpeakerSystem(IZoneDirectory directory) {
        this.directory = directory;
        this.speakers = new ArrayList<>();
        this.speakerLookup = new HashMap<>();
        this.speakerPositionLookup = new HashMap<>();
    }

    public int getSpeakerCount() {
        return this.speakers.size();
    }

    public Speaker getSpeakerByIndex(int index) {
        return this.speakers.get(index);
    }

    public Speaker getSpeakerByPosition(Vector3i position) {
        return this.speakerPositionLookup.get(position);
    }

    public void addSpeakerPosition(Vector3i position, Speaker speaker) {
        this.speakerPositionLookup.put(new Vector3i(position), speaker);
    }

    public void removeSpeakerPosition(Vector3i position) {
        this.speakerPositionLookup.remove(position);
    }

    public void loadData(Path zonePath) {
        var speakersDirectory = zonePath
            .resolve(ZoneSpeakerSystem.speakersDirectory)
            .toFile();

        if(!speakersDirectory.exists() || !speakersDirectory.isDirectory()) {
            return;
        }

        for(var file : speakersDirectory.listFiles(File::isFile)) {
            var fileName = file.getName();
            if(!fileName.endsWith(ZoneSpeakerSystem.speakerConfigSuffix)) {
                continue;
            }

            var speakerName = fileName.substring(0, fileName.length() - ZoneSpeakerSystem.speakerConfigSuffix.length());
            var speakerConfig = FileFns.readJSONFromFile(file);
            Logger.instance.info(String.format("Loading speaker config: %s", fileName));

            var textureSampleName = speakerConfig.optString("texture.sample", null);
            var textureSample = this.directory.getTextureSystem().getTextureSample(textureSampleName);

            if(textureSampleName != null && textureSample == null) {
                var msg = String.format("Texture sample not found: %s", textureSampleName);
                throw new ZoneLoadException(msg);
            }

            var soundName = speakerConfig.getString("sound");
            var soundBufferRef = this.directory.getSoundSystem().getSoundBufferRefByName(soundName);
            var minRadius = Math.max(0f, speakerConfig.optFloat("radius.min"));
            var maxRadius = FloatMaths.clamp(speakerConfig.optFloat("radius.max"), minRadius, Float.MAX_VALUE);

            if(soundBufferRef == null) {
                var msg = String.format("Sound not found: %s", soundName);
                throw new ZoneLoadException(msg);
            }

            var speaker = new Speaker(
                this.directory,
                soundBufferRef,
                speakerName,
                speakerConfig.optFloat("gain"),
                minRadius,
                maxRadius,
                textureSample
            );

            Logger.instance.info(String.format("Registering speaker: %s with sound: %s", speaker, soundBufferRef));
            this.speakers.add(speaker);
            this.speakerLookup.put(speaker.name, speaker);
        }
        
        var generatedSpeakerConfigFile = zonePath
            .resolve(ZoneSpeakerSystem.generatedSpeakerConfigPath)
            .toFile();

        if(!generatedSpeakerConfigFile.exists()) {
            return;
        }

        Logger.instance.info(String.format("Loading generated speaker config: %s", generatedSpeakerConfigFile.getName()));
        var generatedSpeakersConfig = FileFns.readJSONFromFile(generatedSpeakerConfigFile); 
        for(var speakerName : generatedSpeakersConfig.keySet()) {
            var generatedSpeakerConfig = generatedSpeakersConfig.getJSONObject(speakerName);
            var speaker = this.speakerLookup.get(speakerName);
            if(speaker == null) {
                continue;
            }

            var positions = generatedSpeakerConfig.getJSONArray("positions");
            for(var ix = 0; ix < positions.length(); ix += 1) {
                var positionArray = positions.getJSONArray(ix);
                var speakerPosition = JSONFns.getVector3iFromJSON(new Vector3i(), positionArray);
                speaker.addEmitter(speakerPosition);
                this.addSpeakerPosition(speakerPosition, speaker);
            }
        }
    }

    public void saveData(Path zonePath) {
        var generatedSpeakerConfig = new JSONObject();
        for(var speaker : this.speakers) {
            var speakerName = speaker.name;
            var speakerConfig = new JSONObject();
            var speakerPositions = new JSONArray();
            for(var position : speaker.getSpeakerPositions()) {
                var positionJSON = JSONFns.getJSONFromVector3i(position);
                speakerPositions.put(positionJSON);
            }
            speakerConfig.put("positions", speakerPositions);
            generatedSpeakerConfig.put(speakerName, speakerConfig);
        }

        var generatedSpeakerConfigFile = zonePath
            .resolve(ZoneSpeakerSystem.generatedSpeakerConfigPath)
            .toFile();

        generatedSpeakerConfigFile.getParentFile().mkdirs();
        FileFns.writeJSONToFile(generatedSpeakerConfigFile, generatedSpeakerConfig);
    }

    public void destroy() {
        for(var speaker : this.speakers) {
            Logger.instance.info(String.format("Destroying speaker: %s", speaker));
            speaker.destroy();
        }
    }
    
}
