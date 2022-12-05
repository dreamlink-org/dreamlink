package dreamlink.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class SoundData {

    private static int maxBufferSize = 1024 * 1024 * 64;
    private static ByteBuffer inputBuffer = MemoryUtil.memAlloc(maxBufferSize);

    public int channels;
    public int sampleRate;
    public short[] pcmData;

    public SoundData(short[] pcmData, int channels, int sampleRate) {
        this.pcmData = pcmData;
        this.channels = channels;
        this.sampleRate = sampleRate;
    }

    public static SoundData fromBytes(byte[] bytes) {
        try(var stack = MemoryStack.stackPush()) {
            var channelsBuffer = stack.mallocInt(1);
            var sampleRateBuffer = stack.mallocInt(1);
            ShortBuffer pcmBuffer = null;
            try {
                synchronized(SoundData.inputBuffer) {
                    SoundData.inputBuffer.clear();
                    SoundData.inputBuffer.put(bytes);
                    SoundData.inputBuffer.flip();

                    pcmBuffer = STBVorbis.stb_vorbis_decode_memory(
                        SoundData.inputBuffer,
                        channelsBuffer,
                        sampleRateBuffer
                    );
                }
                var pcmData = new short[pcmBuffer.remaining()];
                pcmBuffer.get(pcmData);
                pcmBuffer.flip();
                var channels = channelsBuffer.get();
                var sampleRate = sampleRateBuffer.get();
                return new SoundData(pcmData, channels, sampleRate);
            } finally {
                if(pcmBuffer != null) {
                    MemoryUtil.memFree(pcmBuffer);
                }
            }
        }

    }

    public static SoundData fromFile(File file) {
        try(var rawStream = new FileInputStream(file)) {
            var bytes = rawStream.readAllBytes();
            return SoundData.fromBytes(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
