package dreamlink.graphics.mesh.vertexbuffer;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL42;
import org.lwjgl.system.MemoryUtil;

public class MeshFloatBuffer implements IMeshBuffer {

    private static int maxSize = 500_000;
    private static FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(MeshFloatBuffer.maxSize);

    private int vertexBufferID;
    private int location;
    private int size;
    private List<Float> data;

    public MeshFloatBuffer(int location, int size) {
        this.location = location;
        this.size = size;
        this.data = new ArrayList<>();
    }

    public int getDataCount() {
        return this.data.size();
    }

    @Override
    public void setup() {
        this.vertexBufferID = GL42.glGenBuffers();
        GL42.glBindBuffer(GL42.GL_ARRAY_BUFFER, this.vertexBufferID);
        GL42.glVertexAttribPointer(this.location, this.size, GL42.GL_FLOAT, false, 0, 0);
        GL42.glEnableVertexAttribArray(this.location);
    }

    @Override
    public void clear() {
        this.data.clear();
    }

    public void add(float value) {
        this.data.add(value);
    }

    @Override
    public void buffer() {
        MeshFloatBuffer.floatBuffer.clear();
        for(var value : this.data) {
            MeshFloatBuffer.floatBuffer.put(value);
        }
        MeshFloatBuffer.floatBuffer.flip();
        GL42.glBindBuffer(GL42.GL_ARRAY_BUFFER, this.vertexBufferID);
        GL42.glBufferData(GL42.GL_ARRAY_BUFFER, MeshFloatBuffer.floatBuffer, GL42.GL_DYNAMIC_DRAW);
    }

    @Override
    public void destroy() {
        GL42.glDeleteBuffers(this.vertexBufferID);
    }
    
}
