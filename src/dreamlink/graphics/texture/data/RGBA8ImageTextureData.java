package dreamlink.graphics.texture.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.joml.Vector2i;
import org.lwjgl.opengl.GL42;
import org.lwjgl.system.MemoryUtil;

import de.matthiasmann.twl.utils.PNGDecoder;

public class RGBA8ImageTextureData implements ITextureData {

    private static int chunkBufferSize = 1024 * 1024;
    private static int maxBufferSize = 4096 * 4096 * 4;
    private static ByteBuffer inputBuffer = MemoryUtil.memAlloc(maxBufferSize);

    public byte[] textureData;
    public Vector2i dimensions;

    public RGBA8ImageTextureData(byte[] textureData, Vector2i dimensions) {
        this.textureData = textureData;
        this.dimensions = new Vector2i(dimensions);
    }

    public static RGBA8ImageTextureData fromStream(InputStream stream) throws IOException {
        var decoder = new PNGDecoder(stream);
        var dimensions = new Vector2i(decoder.getWidth(), decoder.getHeight());
        var textureData = new byte[dimensions.x * dimensions.y * 4];
        var buffer = ByteBuffer.wrap(textureData);
        decoder.decodeFlipped(buffer, dimensions.x * 4, PNGDecoder.Format.RGBA);
        return new RGBA8ImageTextureData(textureData, dimensions);
    }

    public static RGBA8ImageTextureData fromFile(File file) {
        try(
            var rawStream = new FileInputStream(file);
            var stream = new BufferedInputStream(rawStream, RGBA8ImageTextureData.chunkBufferSize);
        ) {
            return RGBA8ImageTextureData.fromStream(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void bufferData() {
        RGBA8ImageTextureData.inputBuffer.clear();
        RGBA8ImageTextureData.inputBuffer.put(this.textureData);
        RGBA8ImageTextureData.inputBuffer.flip();

        GL42.glTexImage2D(
            GL42.GL_TEXTURE_2D, 
            0, 
            GL42.GL_RGBA, 
            this.dimensions.x,
            this.dimensions.y,
            0, 
            GL42.GL_RGBA, 
            GL42.GL_UNSIGNED_BYTE, 
            RGBA8ImageTextureData.inputBuffer
        );
    }

    
}
