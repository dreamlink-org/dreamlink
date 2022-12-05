package dreamlink.graphics.mesh;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.mesh.vertexbuffer.IMeshBuffer;

public abstract class Mesh {

    private static Mesh currentlyBoundMesh;

    public static Mesh getCurrentlyBoundMesh() {
        return Mesh.currentlyBoundMesh;
    }

    public static void unbind() {
        GL42.glBindVertexArray(0);
        Mesh.currentlyBoundMesh = null;
    }

    private List<IMeshBuffer> meshBuffers;
    private int vertexArrayID;

    public Mesh() {
        this.meshBuffers = new ArrayList<>();
    }

    protected abstract int getIndicesCount();

    protected void addMeshBuffer(IMeshBuffer meshBuffer) {
        this.meshBuffers.add(meshBuffer);
    }

    public void setup() {
        this.vertexArrayID = GL42.glGenVertexArrays();
        var previouslyBoundMesh = Mesh.getCurrentlyBoundMesh();
        this.bind();
        
        for(var meshBuffer : this.meshBuffers) {
            meshBuffer.setup();
        }

        if(previouslyBoundMesh != null) {
            previouslyBoundMesh.bind();
        } else {
            Mesh.unbind();
        }
    }

    private void bind() {
        if(Mesh.currentlyBoundMesh != this) {
            GL42.glBindVertexArray(this.vertexArrayID);
            Mesh.currentlyBoundMesh = this;
        }
    }

    public void clear() {
        for(var meshBuffer : this.meshBuffers) {
            meshBuffer.clear();
        }
    }

    public void buffer() {
        var previouslyBoundMesh = Mesh.getCurrentlyBoundMesh();
        this.bind();

        for(var meshBuffer : this.meshBuffers) {
            meshBuffer.buffer();
        }

        if(previouslyBoundMesh != null) {
            previouslyBoundMesh.bind();
        } else {
            Mesh.unbind();
        }
    }

    public void render() {
        var previouslyBoundMesh = Mesh.getCurrentlyBoundMesh();
        this.bind();

        GL42.glDrawElements(GL42.GL_TRIANGLES, this.getIndicesCount(), GL42.GL_UNSIGNED_INT, 0);

        if(previouslyBoundMesh != null) {
            previouslyBoundMesh.bind();
        } else {
            Mesh.unbind();
        }
    }

    public void destroy() {
        var previouslyBoundMesh = Mesh.getCurrentlyBoundMesh();
        this.bind();

        for(var meshBuffer : this.meshBuffers) {
            meshBuffer.destroy();
        }

        GL42.glDeleteVertexArrays(this.vertexArrayID);

        if(previouslyBoundMesh != null) {
            previouslyBoundMesh.bind();
        } else {
            Mesh.unbind();
        }

    }
    
}
