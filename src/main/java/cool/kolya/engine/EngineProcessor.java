package cool.kolya.engine;

import cool.kolya.engine.event.RenderEvent;
import cool.kolya.engine.event.UpdateEvent;
import cool.kolya.engine.event.bus.EventBus;
import cool.kolya.implementation.Camera;
import cool.kolya.engine.scene.Scene;
import cool.kolya.engine.scene.SceneImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.*;

public class EngineProcessor {

    protected static final Logger log = LoggerFactory.getLogger(EngineProcessor.class);
    private final UPSProvider upsProvider = new UPSProvider();
    private final FPSProvider fpsProvider = new FPSProvider();

    private final Mouse mouse;
    private final Window window;
    private final Camera camera;
    private final Scene scene;

    public EngineProcessor() {
        mouse = new Mouse();
        window = new Window();
        camera = new Camera();
        scene = new SceneImpl();
        Runtime.getRuntime().addShutdownHook(new Thread(this::freeAndTerminate));
    }

    public Mouse getMouseState() {
        return mouse;
    }

    public Window getWindow() {
        return window;
    }

    public Camera getCamera() {
        return camera;
    }

    public Scene getScene() {
        return scene;
    }

    public void process() {
        window.show();
        try {
            loop();
        } catch (Exception ex) {
            log.error("error: ", ex);
        } finally {
            freeAndTerminate();
        }
    }

    public void loop() {
        long lastFrameElapsedTime = System.currentTimeMillis();
        long steps = 0;

        EventBus bus = EventBus.getInstance();

        while (!window.isShouldBeClose()) {
            glfwPollEvents();
            final long startFrameTime = System.currentTimeMillis();
            long elapsed = startFrameTime - lastFrameElapsedTime;
            lastFrameElapsedTime = startFrameTime;
            steps += elapsed;

            final long timeU = 1000 / upsProvider.ups;
            bus.dispatch(new UpdateEvent());
            for (; steps >= timeU; steps -= timeU) {
                scene.update();
            }

            scene.render();
            bus.dispatch(new RenderEvent());

            final long frameTime = fpsProvider.fps > 0 ? 1000 / fpsProvider.fps : 0;
            final long endFrameTime = startFrameTime + frameTime;

            //noinspection StatementWithEmptyBody
            while (System.currentTimeMillis() < endFrameTime) ;
        }
    }

    private void freeAndTerminate() {
        glfwTerminate();
    }

    static class UPSProvider {

        private int ups = 60;

        public void setUps(int ups) {
            if (ups <= 0) {
                //log.error("ups can't be less or equal than zero value, value: {}", ups);
                return;
            }
            this.ups = ups;
        }

        public float getUps() {
            return ups;
        }
    }

    static class FPSProvider {

        private int fps;

        public FPSProvider(int fps) {
            this.fps = fps;
            glfwSwapInterval(fps);
        }

        public FPSProvider() {
            this(0);
        }

        public void setFps(int fps) {
            int swapInterval = fps > 0 ? 0 : 1;
            glfwSwapInterval(swapInterval);
            this.fps = fps;
        }

        public int getFps() {
            return fps;
        }

        public boolean isVsync() {
            return fps == 1;
        }
    }
}
