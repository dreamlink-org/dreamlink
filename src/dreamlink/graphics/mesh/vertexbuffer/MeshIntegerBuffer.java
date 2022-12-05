package dreamlink.graphics.mesh.vertexbuffer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL42;
import org.lwjgl.system.MemoryUtil;

public class MeshIntegerBuffer implements IMeshBuffer {

    private static int maxSize = 500_000;
    public static IntBuffer intBuffer = MemoryUtil.memAllocInt(MeshIntegerBuffer.maxSize);

    private int vertexBufferID;
    private int location;
    private int size;
    private List<Integer> data;

    public MeshIntegerBuffer(int location, int size) {
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
        GL42.glVertexAttribIPointer(this.location, this.size, GL42.GL_INT, 0, 0);
        GL42.glEnableVertexAttribArray(this.location);
    }

    @Override
    public void clear() {
        this.data.clear();
    }

    public void add(int value) {
        this.data.add(value);
    }

    @Override
    public void buffer() {
        MeshIntegerBuffer.intBuffer.clear();
        for(var value : this.data) {
            MeshIntegerBuffer.intBuffer.put(value);
        }
        MeshIntegerBuffer.intBuffer.flip();
        GL42.glBindBuffer(GL42.GL_ARRAY_BUFFER, this.vertexBufferID);
        GL42.glBufferData(GL42.GL_ARRAY_BUFFER, MeshIntegerBuffer.intBuffer, GL42.GL_DYNAMIC_DRAW);
    }

    @Override
    public void destroy() {
        GL42.glDeleteBuffers(this.vertexBufferID);
    }
    
}
