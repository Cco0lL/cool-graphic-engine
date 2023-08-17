package cool.kolya;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import cool.kolya.engine.EngineProcessor;
import cool.kolya.engine.data.Resolution;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Engine {

    private static final Logger log = LoggerFactory.getLogger(Engine.class);
    private static EngineProcessor PROCESSOR;
    private static Config CONFIG;

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
        /* TODO if config path is valid but there isn't a file, then copies default config from resources to given path*/
        String path = System.getProperty("engine.config-path");
        ConfigParseOptions READ_OPTS = ConfigParseOptions.defaults().setSyntax(ConfigSyntax.CONF);
        CONFIG = path == null ? ConfigFactory.parseResources("config.conf", READ_OPTS)
                : ConfigFactory.parseFile(new File(path), READ_OPTS);
        PROCESSOR = new EngineProcessor();
    }

    public static Config getConfig() {
        return CONFIG;
    }

    public static EngineProcessor getProcessor() {
        return PROCESSOR;
    }

    public static Resolution getPrimaryMonitorResolution() {
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        return new Resolution(vidMode.width(), vidMode.height());
    }

    public static void start() {
        PROCESSOR.process();
    }
}