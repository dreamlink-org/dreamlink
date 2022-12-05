package dreamlink.graphics.mesh;

import org.joml.Vector2f;

import dreamlink.graphics.mesh.strategy.index.MeshIndexQuadStrategy;
import dreamlink.graphics.mesh.strategy.position.MeshScreenPositionQuadStrategy;
import dreamlink.graphics.mesh.vertexbuffer.MeshFloatBuffer;
import dreamlink.graphics.mesh.vertexbuffer.MeshIndexBuffer;

public class CompositeMesh extends Mesh {

    private static int positionLocation = 0;
    private static int positionSize = 2;

    public static CompositeMesh instance = new CompositeMesh();

    private MeshIndexBuffer indexBuffer;
    private MeshFloatBuffer positionBuffer;

    private MeshIndexQuadStrategy indexStrategy;
    private MeshScreenPositionQuadStrategy positionStrategy;

    public CompositeMesh() {
        this.indexBuffer = new MeshIndexBuffer();
        this.positionBuffer = new MeshFloatBuffer(CompositeMesh.positionLocation, CompositeMesh.positionSize);

        this.indexStrategy = new MeshIndexQuadStrategy(this::getIndexBuffer);
        this.positionStrategy = new MeshScreenPositionQuadStrategy(this::getPositionBuffer);

        this.addMeshBuffer(this.indexBuffer);
        this.addMeshBuffer(this.positionBuffer);
    }

    @Override
    public void setup() {
        super.setup();

        this.indexStrategy.add();
        this.positionStrategy.add(
            new Vector2f(-1f),
            new Vector2f(2f)
        );

        this.buffer();
        //this.clear();
    }

    @Override
    protected int getIndicesCount() {
        return this.indexBuffer.getIndicesCount();
    }

    private MeshIndexBuffer getIndexBuffer() {
        return this.indexBuffer;
    }

    private MeshFloatBuffer getPositionBuffer() {
        return this.positionBuffer;
    }

    
}
