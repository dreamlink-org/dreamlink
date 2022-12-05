package dreamlink.graphics.mesh.vertexbuffer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL42;

public class MeshIndexBuffer implements IMeshBuffer {

    private int vertexBufferID;
    private List<Integer> data;
    private int indicesCount;

    public MeshIndexBuffer() {
        this.data = new ArrayList<>();
    }

    public int getIndicesCount() {
        return this.indicesCount;
    }

    public int getDataCount() {
        return this.data.size();
    }

    @Override
    public void setup() {
        this.vertexBufferID = GL42.glGenBuffers();
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
        this.indicesCount = this.data.size();
        for(var value : this.data) {
            MeshIntegerBuffer.intBuffer.put(value);
        }

        MeshIntegerBuffer.intBuffer.flip();
        GL42.glBindBuffer(GL42.GL_ELEMENT_ARRAY_BUFFER, this.vertexBufferID);
        GL42.glBufferData(GL42.GL_ELEMENT_ARRAY_BUFFER, MeshIntegerBuffer.intBuffer, GL42.GL_DYNAMIC_DRAW);
    }
    
    @Override
    public void destroy() {
        GL42.glDeleteBuffers(this.vertexBufferID);
    }
}
