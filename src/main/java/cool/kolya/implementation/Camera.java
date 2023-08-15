package cool.kolya.implementation;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private final Vector3f position = new Vector3f(0f);
    private final Vector3f rotation = new Vector3f(0f);
    private final Matrix4f positionMatrix = new Matrix4f();
    private final Matrix4f rotationMatrix = new Matrix4f();
    private final Matrix4f cameraMatrix = new Matrix4f();

    public Matrix4f getCameraMatrix() {
        return cameraMatrix;
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public Matrix4f getPositionMatrix() {
        return positionMatrix;
    }

    public Vector3f getRotation() {
        return new Vector3f(rotation);
    }

    public Matrix4f getRotationMatrix() {
        return rotationMatrix;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if (offsetX != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
        positionMatrix.identity()
                .translate(-position.x, -position.y, -position.z);
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
        rotationMatrix.identity()
                .rotateAffineXYZ(rotation.x, rotation.y, rotation.z);
    }

    public void updateCameraMatrix() {
        cameraMatrix.identity()
                .mul(positionMatrix)
                .mul(rotationMatrix);
    }
}
