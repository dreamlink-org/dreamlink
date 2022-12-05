package dreamlink.graphics.mesh.strategy.texture;

import org.joml.Vector2f;

import dreamlink.graphics.texture.sample.TextureSample;

public class MeshTextureQuadStrategy {

    public static int animationFramePeriod = 20;

    public static int getAnimationFrame() {
        return (int)((System.currentTimeMillis() / MeshTextureQuadStrategy.animationFramePeriod) % Integer.MAX_VALUE);
    }

    private static int textureChannelIDOffset = 0;
    private static int animationTotalFramesOffset = 4;
    private static int animationRowFramesOffset = 12;
    private static int animationStartFrameOffset = 20;
    private static int animationSpeedOffset = 28;
    
    private static int textureChannelIDMask = 0xF;
    private static int animationFrameMask = 0xFF;
    private static int animationSpeedMask = 0xF;

    private IMeshTextureStrategyProvider provider;

    public MeshTextureQuadStrategy(IMeshTextureStrategyProvider provider) {
        this.provider = provider;
    }

    public void add(
        int textureChannelID,
        int windingOffset,
        TextureSample textureSample
    ) {
        var offsetBuffer = this.provider.getTextureOffsetBuffer();
        var metaDataBuffer = this.provider.getTextureMetaDataBuffer();
        var textureDataBuffer = new Vector2f();

        var packedData = 0;
        packedData |= (textureChannelID & MeshTextureQuadStrategy.textureChannelIDMask) << MeshTextureQuadStrategy.textureChannelIDOffset;
        packedData |= (textureSample.animationTotalFrames & MeshTextureQuadStrategy.animationFrameMask) << MeshTextureQuadStrategy.animationTotalFramesOffset;
        packedData |= (textureSample.animationRowFrames & MeshTextureQuadStrategy.animationFrameMask) << MeshTextureQuadStrategy.animationRowFramesOffset;
        packedData |= (textureSample.animationStartFrame & MeshTextureQuadStrategy.animationFrameMask) << MeshTextureQuadStrategy.animationStartFrameOffset;
        packedData |= (textureSample.animationSpeed & MeshTextureQuadStrategy.animationSpeedMask) << MeshTextureQuadStrategy.animationSpeedOffset;

        textureSample.getAnimationStride(textureDataBuffer);
        var animStrideX = textureDataBuffer.x;
        var animStrideY = textureDataBuffer.y;

        for(var ix = 0; ix < 4; ix += 1) {
            var offsetIndex = (ix + 4 - windingOffset) % 4;
            textureSample.getTextureOffset(offsetIndex, textureDataBuffer);
            offsetBuffer.add(textureDataBuffer.x);
            offsetBuffer.add(textureDataBuffer.y);
            offsetBuffer.add(animStrideX);
            offsetBuffer.add(animStrideY);
            metaDataBuffer.add(packedData);
        }
    }
    
}
