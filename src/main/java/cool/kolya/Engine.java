package cool.kolya;

import cool.kolya.implementation.Camera;
import cool.kolya.engine.EngineProcessor;
import cool.kolya.engine.scene.Scene;
import cool.kolya.engine.Mouse;
import cool.kolya.engine.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Engine {

    private static final Logger log = LoggerFactory.getLogger(Engine.class);
    private static EngineProcessor PROCESSOR;

    public static Window getWindow() {
        return PROCESSOR.getWindow();
    }

    public static Mouse getMouseState() {
        return PROCESSOR.getMouseState();
    }

    public static Camera getCamera() {
        return PROCESSOR.getCamera();
    }

    public static Scene getScene() {
        return PROCESSOR.getScene();
    }

    public static void initialize() {
        boolean init = GLFW.glfwInit();
        if (!init) {
            log.error("An error occurred on glfw initialization, shutting down app");
            System.exit(-1);
        }
        GLFW.glfwSetErrorCallback(((error, descriptionPointer) -> {
            String description = MemoryUtil.memUTF8(descriptionPointer);
            log.error("code: {}, description: {}", error, description);
        }));
        PROCESSOR = new EngineProcessor();
    }

    public static void start() {
        PROCESSOR.process();
    }
}
