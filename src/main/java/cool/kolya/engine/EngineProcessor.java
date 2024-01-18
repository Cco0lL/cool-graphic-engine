package cool.kolya.engine;

import cool.kolya.engine.event.EventBus;
import cool.kolya.engine.event.RenderEvent;
import cool.kolya.engine.event.UpdateEvent;
import cool.kolya.implementation.Camera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.*;

public class EngineProcessor {

    protected static final Logger log = LoggerFactory.getLogger(EngineProcessor.class);
    private final Mouse mouse;
    private final Window window;
    private final Camera camera;
    private final DestructorProvider destructorProvider;

    private int fps = 60,
            ups = 60;

    private Updater updater;
    private Renderer renderer;

    public EngineProcessor() {
        mouse = new Mouse();
        window = new Window();
        camera = new Camera();
        destructorProvider = new DestructorProvider();

        int swapInterval = fps > 0 ? 0 : 1;
        glfwSwapInterval(swapInterval);
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

    public Updater getUpdater() {
        return updater;
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public DestructorProvider getDestructorProvider() {
        return destructorProvider;
    }

    public void process() {
        window.show();
        try {
            loop();
        } catch (Exception ex) {
            log.error("error: ", ex);
        } finally {
            terminate();
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

            final long timeU = 1000 / ups;
            bus.dispatch(new UpdateEvent());
            for (; steps >= timeU; steps -= timeU) {
                updater.update();
            }

            renderer.clearAndRender();
            bus.dispatch(new RenderEvent());

            final long frameTime = fps > 0 ? 1000 / fps : 0;
            final long endFrameTime = startFrameTime + frameTime;

            //noinspection StatementWithEmptyBody
            while (System.currentTimeMillis() < endFrameTime) ;
        }
    }

    private void terminate() {
        glfwTerminate();
    }
}
