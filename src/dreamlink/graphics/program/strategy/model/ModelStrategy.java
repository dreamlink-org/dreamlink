package dreamlink.graphics.program.strategy.model;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ModelStrategy {

    private static Matrix4f matrixBuffer = new Matrix4f();

    private IModelStrategyProvider provider;

    public ModelStrategy(IModelStrategyProvider provider) {
        this.provider = provider;
    }

    public void setModel(Vector3f position) {
        ModelStrategy.matrixBuffer
            .identity()
            .translate(position);

        this.provider
            .getModelMatrixUniform()
            .setValue(ModelStrategy.matrixBuffer);
    }
    
}
