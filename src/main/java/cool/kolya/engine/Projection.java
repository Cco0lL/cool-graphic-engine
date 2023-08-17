package cool.kolya.engine;

import com.typesafe.config.Config;
import cool.kolya.Engine;
import cool.kolya.engine.data.Resolution;
import cool.kolya.engine.util.ConfigUtil;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Projection {

    private static final Logger log = LoggerFactory.getLogger(Projection.class);
    private final Matrix4f projectionMatrix = new Matrix4f();
    private float aspectRatio;
    private float fov;
    private float zFar;
    private float zNear;

    Projection() {
        Resolution resolution = Engine.getPrimaryMonitorResolution();
        aspectRatio = (float) resolution.width() / resolution.height();

        Config config = Engine.getConfig();

        ConfigUtil.readSafe(() -> {
            if (config.hasPath("fov")) {
                fov = (float) Math.toRadians(config.getInt("fov"));
            }
            if (config.hasPath("zFar")) {
                zFar = (float) config.getDouble("zFar");
            }
            if (config.hasPath("zNear")) {
                zNear = (float) config.getDouble("zNear");
            }
        }, (ex) -> {
            log.error("An error occurred, setting default fov, zNear, zFar values", ex);
            fov = (float) Math.toRadians(60.00);
            zFar = 1000.0f;
            zNear = 0.01f;
        });

        updateProjectionMatrix();
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
