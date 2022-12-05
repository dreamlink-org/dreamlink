package dreamlink.graphics.glstate;

import org.lwjgl.opengl.GL42;

public class PolygonOffsetState implements AutoCloseable {

    private static float factor;
    private static float units;

    private static void setPolygonOffset(float factor, float units) {
        PolygonOffsetState.factor = factor;
        PolygonOffsetState.units = units;
        GL42.glPolygonOffset(factor, units);
    }

    public static void initialize() {
        PolygonOffsetState.setPolygonOffset(0.0f, 0.0f);
    }

    private float previousFactor;
    private float previousUnits;

    public PolygonOffsetState() {
        this.previousFactor = PolygonOffsetState.factor;
        this.previousUnits = PolygonOffsetState.units;
    }

    public void setState(float factor, float units) {
        if(PolygonOffsetState.factor != factor || PolygonOffsetState.units != units) {
            PolygonOffsetState.setPolygonOffset(factor, units);
        }
    }

    @Override
    public void close() {
        this.setState(this.previousFactor, this.previousUnits);
    }

    
}
