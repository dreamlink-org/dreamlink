package dreamlink.graphics.mesh.strategy.position;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dreamlink.utility.maths.CubeFace;

public class MeshWorldPositionQuadStrategy {

    private static Vector2f[] windings = new Vector2f[] {
        new Vector2f(0f, 1f),
        new Vector2f(0f, 0f),
        new Vector2f(1f, 0f),
        new Vector2f(1f, 1f),
    };

    private IMeshPositionStrategyProvider provider;

    public MeshWorldPositionQuadStrategy(IMeshPositionStrategyProvider provider) {
        this.provider = provider;
    }

    public void add(
        Vector3f position,
        Vector3f dimensions,
        CubeFace cubeFace
    ) {
        var buffer = this.provider.getPositionBuffer();
        for(var winding : MeshWorldPositionQuadStrategy.windings) {
            if (cubeFace == CubeFace.front) {
                buffer.add(position.x + dimensions.x * winding.x);
                buffer.add(position.y + dimensions.y * winding.y);
                buffer.add(position.z + dimensions.z);
            } else if (cubeFace == CubeFace.back) {
                buffer.add(position.x + dimensions.x * (1f - winding.x));
                buffer.add(position.y + dimensions.y * winding.y);
                buffer.add(position.z);
            } else if (cubeFace == CubeFace.right) {
                buffer.add(position.x);
                buffer.add(position.y + dimensions.y * winding.y);
                buffer.add(position.z + dimensions.z * winding.x);
            } else if (cubeFace == CubeFace.left) {
                buffer.add(position.x + dimensions.x);
                buffer.add(position.y + dimensions.y * winding.y);
                buffer.add(position.z + dimensions.z * (1f - winding.x));
            } else if (cubeFace == CubeFace.top) {
                buffer.add(position.x + dimensions.x * winding.x);
                buffer.add(position.y + dimensions.y);
                buffer.add(position.z + dimensions.z * (1f - winding.y));
            } else if (cubeFace == CubeFace.bottom) {
                buffer.add(position.x + dimensions.x * winding.x);
                buffer.add(position.y);
                buffer.add(position.z + dimensions.z * winding.y);
            }
        }
    }

    
}
