package cool.kolya.implementation;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private final Vector3f position = new Vector3f(0f);
    private final Vector2f rotation = new Vector2f(0f);
    private final Vector3f dirVector = new Vector3f(0f, 0f, -1f);
    private final Matrix4f positionMatrix = new Matrix4f();
    private final Matrix4f rotationMatrix = new Matrix4f();
    private final Matrix4f cameraMatrix = new Matrix4f();

    public Camera() {
        updatePositionMatrix();
        updateRotationMatrix();
    }

    public Matrix4f getCameraMatrix() {
        return cameraMatrix;
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public Matrix4f getPositionMatrix() {
        return positionMatrix;
    }

    public Matrix4f getRotationMatrix() {
        return rotationMatrix;
    }

    public Vector2f getRotation() {
        return new Vector2f(rotation);
    }

    public Vector3f getDirVector() {
        return dirVector;
    }

    public void movePosition(Vector3f moveVec) {
        float rotYRads = (float) Math.toRadians(rotation.y);
        float rotYMinusNineteenRads = (float) Math.toRadians(rotation.y - 90);
        if (moveVec.z != 0) {
            position.x += -Math.sin(rotYRads) * moveVec.z;
            position.z += Math.cos(rotYRads) * moveVec.z;
        }
        if (moveVec.x != 0) {
            position.x += -Math.sin(rotYMinusNineteenRads) * moveVec.x;
            position.z += Math.cos(rotYMinusNineteenRads) * moveVec.x;
        }
        if (moveVec.y != 0) {
            position.y += Math.sin(rotYRads) * moveVec.y;
        }
        updatePositionMatrix();
        updateCameraMatrix();
    }

    public void moveRotation(float pitch, float yaw) {
        rotation.x += yaw;
        rotation.y += pitch;

        //dir vec
        float newYawRads = (float) Math.toRadians(rotation.x);
        float newPitchRads = (float) Math.toRadians(rotation.y);

        float cZ = (float) Math.cos(newYawRads);
        float sZ = (float) Math.sin(newYawRads);
        float cY = (float) Math.cos(newPitchRads);
        float sY = (float) Math.sin(newPitchRads);

        dirVector.x = sZ;
        dirVector.y = -(sY * cZ);
        dirVector.z = -(cY * cZ);

        updateRotationMatrix();
        updateCameraMatrix();
    }

    private void updateRotationMatrix() {
        rotationMatrix.identity().rotateAffineXYZ(rotation.x, rotation.y, 0f);
    }

    private void updatePositionMatrix() {
        positionMatrix.identity().translate(-position.x, -position.y, -position.z);
    }

    public void updateCameraMatrix() {
        cameraMatrix.identity()
                .mul(positionMatrix)
                .mul(rotationMatrix);
    }
}
