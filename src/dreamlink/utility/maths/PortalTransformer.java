package dreamlink.utility.maths;

import org.joml.Vector3f;

public class PortalTransformer {

    public Vector3f entryPortalPosition;
    public float entryPortalYaw;

    public Vector3f exitPortalPosition;
    public float exitPortalYaw;

    public PortalTransformer() {
        this.entryPortalPosition = new Vector3f();
        this.exitPortalPosition = new Vector3f();
    }

    public PortalTransformer(
        Vector3f entryPortalPosition,
        float entryPortalYaw,
        Vector3f exitPortalPosition,
        float exitPortalYaw
    ) {
        this.entryPortalPosition = new Vector3f();
        this.exitPortalPosition = new Vector3f();

        this.set(
            entryPortalPosition,
            entryPortalYaw,
            exitPortalPosition,
            exitPortalYaw
        );
    }

    public PortalTransformer set(
        Vector3f entryPortalPosition, 
        float entryPortalYaw,
        Vector3f exitPortalPosition,
        float exitPortalYaw
    ) {
        this.entryPortalPosition.set(entryPortalPosition);
        this.entryPortalYaw = entryPortalYaw;
        this.exitPortalPosition.set(exitPortalPosition);
        this.exitPortalYaw = exitPortalYaw;
        return this;
    }

    private float getDeltaYaw() {
        var deltaYaw = (float)Math.PI;
        deltaYaw -= this.entryPortalYaw;
        deltaYaw += this.exitPortalYaw;
        return deltaYaw;
    }

    public Vector3f getTransformedPosition(Vector3f position) {
        return position
            .sub(this.entryPortalPosition)
            .rotateY(this.getDeltaYaw())
            .add(this.exitPortalPosition);
    }

    public Vector3f getTransformedRotation(Vector3f rotation) {
        rotation.y += this.getDeltaYaw();
        return rotation;
    }

    public Vector3f getTransformedVelocity(Vector3f velocity) {
        return velocity.rotateY(this.getDeltaYaw());
    }
    
}
