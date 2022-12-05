package dreamlink.window;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;

public class WindowALCapabilitiesModule {

    private long openALDeviceID;
    private long openALContextID;

    public void setup() {
        this.openALDeviceID = ALC10.alcOpenDevice((ByteBuffer)null);
        this.openALContextID = ALC10.alcCreateContext(
            this.openALDeviceID, 
            (IntBuffer)null
        );
        ALC10.alcMakeContextCurrent(this.openALContextID);
        
        var alcCapabilities = ALC.createCapabilities(this.openALDeviceID);
        AL.createCapabilities(alcCapabilities);
    }

    public void destroy() {
        ALC10.alcDestroyContext(this.openALContextID);
        ALC10.alcCloseDevice(this.openALDeviceID);
    }
    
}
