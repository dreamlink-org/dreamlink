package dreamlink.graphics.mesh.strategy.color;

import org.joml.Vector4f;

import dreamlink.utility.maths.Vector4fMaths;

public class MeshColorQuadStrategy {

    private IMeshColorStrategyProvider provider;

    public MeshColorQuadStrategy(IMeshColorStrategyProvider provider) {
        this.provider = provider;
    }

    public void add(Vector4f color) {
        var packedColor = Vector4fMaths.toHex(color);
        for(var ix = 0; ix < 4; ix += 1) {
            this.provider.getColorBuffer().add(packedColor); 
        }
    }
    
}
