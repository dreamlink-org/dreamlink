package dreamlink.graphics.mesh.strategy.texture;

import dreamlink.graphics.mesh.vertexbuffer.MeshFloatBuffer;
import dreamlink.graphics.mesh.vertexbuffer.MeshIntegerBuffer;

public interface IMeshTextureStrategyProvider {

    public MeshFloatBuffer getTextureOffsetBuffer();

    public MeshIntegerBuffer getTextureMetaDataBuffer();

}
