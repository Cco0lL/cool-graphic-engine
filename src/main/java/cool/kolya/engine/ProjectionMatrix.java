package cool.kolya.engine;

import com.typesafe.config.Config;
import cool.kolya.engine.config.DefaultConfiguration;
import org.joml.Matrix4f;

public class ProjectionMatrix {

    private static float FOV = (float) Math.toRadians(60.0);
    private static float Z_FAR = 1.0f;
    private static float Z_NEAR = 0.01f;
    private static final Matrix4f PROJECTION_MATRIX = new Matrix4f();

    static {
        Config config = DefaultConfiguration.get();
        //TODO runtime setters of these with matrix update
        if (config.hasPath("fov")) {
            FOV = config.getInt("fov");
        }
        if (config.hasPath("z_far")) {
            Z_FAR = (float) config.getDouble("z_far");
        }
        if (config.hasPath("z_near")) {
            Z_NEAR = (float) config.getDouble("z_near");
        }
    }

    public static Matrix4f get() {
        return PROJECTION_MATRIX;
    }

    static void update(float aspectRatio) {
        PROJECTION_MATRIX.identity().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
}
