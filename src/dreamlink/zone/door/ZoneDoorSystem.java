package dreamlink.zone.door;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3i;
import org.json.JSONObject;

import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.logger.Logger;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.FileFns;
import dreamlink.utility.JSONFns;
import dreamlink.utility.maths.Orientation;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.ZoneLoadException;

public class ZoneDoorSystem {

    private static Path doorsDirectory = Paths.get("doors");
    private static String doorConfigSuffix = ".json";
    private static String mainDoor = "main";

    private static Path generatedDoorConfigPath = Paths.get(".gen/doors.json");

    private List<Door> doors;
    private Map<String, Door> doorLookup;
    private Map<Vector3i, Door> doorPositionLookup;

    private IZoneDirectory directory;
    public Door openDoor;

    public ZoneDoorSystem(IZoneDirectory directory) {
        this.directory = directory;
        this.doors = new ArrayList<>();
        this.doorLookup = new HashMap<>();
        this.doorPositionLookup = new HashMap<>();
    }

    public Door getDoorByName(String name) {
        return this.doorLookup.get(name);
    }

    public Door getDoorByPosition(Vector3i position) {
        return this.doorPositionLookup.get(position);
    }

    public int getDoorCount() {
        return this.doors.size();
    }

    public Door getDoorByIndex(int index) {
        return index < this.doors.size() && index >= 0
            ? this.doors.get(index)
            : null;
    }

    public Iterable<Door> getDoors() {
        return this.doors;
    }

    public void addDoorPosition(Vector3i position, Door door) {
        this.doorPositionLookup.put(new Vector3i(position), door);
    }

    public void removeDoorPosition(Vector3i position) {
        this.doorPositionLookup.remove(position);
    }

    public void loadData(Path zonePath) {
        var doorsDirectory = zonePath
            .resolve(ZoneDoorSystem.doorsDirectory)
            .toFile();

        if(!doorsDirectory.exists() || !doorsDirectory.isDirectory()) {
            return;
        }

        var textureSystem = this.directory.getTextureSystem();
        for(var file : doorsDirectory.listFiles(File::isFile)) {
            var fileName = file.getName();
            if(!fileName.endsWith(ZoneDoorSystem.doorConfigSuffix)) {
                continue;
            }

            var doorName = fileName.substring(0, fileName.length() - ZoneDoorSystem.doorConfigSuffix.length());

            Logger.instance.debug(String.format("Loading door config: %s", fileName));
            var doorConfig = FileFns.readJSONFromFile(file);
            var textureEntryTopName = doorConfig.optString("texture.sample.top", null);
            var textureEntryTop = textureEntryTopName == null
                ? EntityTextureSample.doorTop
                : textureSystem.getTextureSample(textureEntryTopName);

            if(textureEntryTop == null) {
                var msg = String.format("Missing top texture sample: %s", textureEntryTopName);
                throw new ZoneLoadException(msg);
            }

            var textureEntryBottomName = doorConfig.optString("texture.sample.bottom", null);
            var textureEntryBottom = textureEntryBottomName == null
                ? EntityTextureSample.doorBottom
                : textureSystem.getTextureSample(textureEntryBottomName);

            if(textureEntryBottom == null) {
                var msg = String.format("Missing bottom texture sample: %s", textureEntryTopName);
                throw new ZoneLoadException(msg);
            }

            var door = new Door(
                this.directory,
                doorName,
                textureEntryTop,
                textureEntryBottom,
                doorConfig.optString("target.zone", null),
                doorConfig.optString("target.door", null)
            );

            this.doorLookup.put(door.name, door);
            this.doors.add(door);
        }

        var dynamicDoorConfigFile = zonePath
            .resolve(ZoneDoorSystem.generatedDoorConfigPath)
            .toFile();

        if(dynamicDoorConfigFile.exists()) {
            Logger.instance.debug(String.format("Loading generated door config: %s", dynamicDoorConfigFile.getName()));
            var generatedDoorsConfig = FileFns.readJSONFromFile(dynamicDoorConfigFile);
            for(var doorName : generatedDoorsConfig.keySet()) {
                var generatedDoorConfig = generatedDoorsConfig.getJSONObject(doorName);
                var door = this.doorLookup.get(doorName);
                if(door == null) {
                    continue;
                }

                var doorPosition = JSONFns.getVector3iFromJSON(
                    new Vector3i(),
                    generatedDoorConfig.getJSONArray("position.position")
                );

                var orientation = Orientation.getOrientation(
                    generatedDoorConfig.getString("position.orientation")
                );

                door.setPlacement(doorPosition, orientation);
                var blockPosition = new Vector3i();
                for(var blockOffset : Door.doorBlockOffsets) {
                    blockPosition.set(doorPosition).add(blockOffset);
                    this.addDoorPosition(blockPosition, door);
                }
            }
        }

        if(Simulation.instance.simulationMode == SimulationMode.explore) {
            var door = this.doorLookup.get(ZoneDoorSystem.mainDoor);
            if(door == null || !door.isPlaced) {
                var msg = String.format("Missing main door: %s", ZoneDoorSystem.mainDoor);
                throw new ZoneLoadException(msg);
            }
        }
    }

    public void saveData(Path zonePath) {
        var generatedDoorConfig = new JSONObject();
        for(var door : this.doors) {
            if(!door.isPlaced) {
                continue;
            }
            var doorConfig = new JSONObject();
            doorConfig.put("position.position", JSONFns.getJSONFromVector3i(door.position));
            doorConfig.put("position.orientation", door.orientation.name);
            generatedDoorConfig.put(door.name, doorConfig);
        }

        var generatedDoorConfigFile = zonePath
            .resolve(ZoneDoorSystem.generatedDoorConfigPath)
            .toFile();

        generatedDoorConfigFile.getParentFile().mkdirs();
        FileFns.writeJSONToFile(generatedDoorConfigFile, generatedDoorConfig);
    }
    
}
