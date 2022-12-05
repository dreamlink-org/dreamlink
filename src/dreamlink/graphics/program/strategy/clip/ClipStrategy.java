package dreamlink.graphics.program.strategy.clip;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class ClipStrategy {

    private static Vector4f clipBuffer = new Vector4f();

    private IClipStrategyProvider provider;

    public ClipStrategy(IClipStrategyProvider provider) {
        this.provider = provider;
    }

    public void setClip(Vector3f position, Vector3f normal) {
        ClipStrategy.clipBuffer
            .set(
                normal.x,
                normal.y,
                normal.z,
                - position.dot(normal)
            );
        this.provider
            .getClipUniform()
            .setValue(ClipStrategy.clipBuffer);
    }
    
}
