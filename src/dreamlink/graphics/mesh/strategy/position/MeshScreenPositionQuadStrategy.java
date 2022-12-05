package dreamlink.graphics.mesh.strategy.position;

import org.joml.Vector2f;

public class MeshScreenPositionQuadStrategy {

    private static Vector2f[] windings = new Vector2f[] {
        new Vector2f(0f, 1f),
        new Vector2f(0f, 0f),
        new Vector2f(1f, 0f),
        new Vector2f(1f, 1f),
    };

    private IMeshPositionStrategyProvider provider;

    public MeshScreenPositionQuadStrategy(IMeshPositionStrategyProvider provider) {
        this.provider = provider;
    }

    public void add(
        Vector2f position,
        Vector2f dimensions
    ) {
        var buffer = this.provider.getPositionBuffer();
        for(var winding : MeshScreenPositionQuadStrategy.windings) {
            buffer.add(position.x + dimensions.x * winding.x);
            buffer.add(position.y + dimensions.y * winding.y);
        }
    }
    
}
