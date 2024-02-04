package cool.kolya.old;

import org.joml.Matrix4f;

public class Projection {

    //private static final Logger log = LoggerFactory.getLogger(Projection.class);
    private final Matrix4f projectionMatrix = new Matrix4f();
    private float aspectRatio;
    private float fov = (float) Math.toRadians(60d);
    private float zFar = 1000f;
    private float zNear = 0.01f;

    Projection() {
        //Resolution resolution = new Resolution(0, 0);
        //Resolution resolution = Engine.getPrimaryMonitorResolution();
        //aspectRatio = (float) resolution.width() / resolution.height();
        //updateProjectionMatrix();
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        updateProjectionMatrix();
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = (float) Math.toRadians(fov);
        updateProjectionMatrix();
    }

    public float getZFar() {
        return zFar;
    }

    public void setZFar(float zFar) {
        this.zFar = zFar;
        updateProjectionMatrix();
    }

    public float getZNear() {
        return zNear;
    }

    public void setZNear(float zNear) {
        this.zNear = zNear;
        updateProjectionMatrix();
    }

    public Matrix4f getMatrix() {
        return projectionMatrix;
    }

    void updateProjectionMatrix() {
        projectionMatrix.identity().perspective(fov, aspectRatio, zNear, zFar);
    }
}
