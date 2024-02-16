package cool.kolya.engine.process.loop;

import cool.kolya.engine.event.Event;
import cool.kolya.engine.event.RenderEvent;
import cool.kolya.engine.event.UpdateEvent;
import cool.kolya.engine.event.bus.EventBus;
import cool.kolya.engine.process.ContextInputEventsManager;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.engine.process.ProcessSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

import java.util.Queue;

public class ProcessLoopImpl implements ProcessLoop {

    protected final EngineProcess process;

    protected final long windowPointer;
    protected final EventBus eventBus;
    protected final ProcessSettings settings;

    private long lastFrameStartTime;
    private long timeFromLastUpdate;
    private int currentTick;
    protected boolean running;

    public ProcessLoopImpl(EngineProcess process) {
        this.process = process;
        this.windowPointer = process.getWindowPtr();
        this.eventBus = process.getEventBus();
        this.settings = process.getSettings();
    }

    @Override
    public void run() {
        lastFrameStartTime = System.currentTimeMillis(); //FIXME global lock place
        running = true;
        while (keepRunning()) {
            try {
                frame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        running = false;
    }

    @Override
    public void pollInputEvents() {
        Queue<Event> polledEvents = ContextInputEventsManager.pollRoutedContextEvents();
        while (!polledEvents.isEmpty()) {
            eventBus.dispatch(polledEvents.poll());
        }
    }

    @Override
    public void frame() {
        pollInputEvents();

        long frameStartTime = System.currentTimeMillis();
        final int tickTime = process.getSettings().getTickTime();

        long prevFrameTimeElapsed = frameStartTime - lastFrameStartTime;
        lastFrameStartTime = frameStartTime;

        timeFromLastUpdate += prevFrameTimeElapsed;
        for (; timeFromLastUpdate >= tickTime; timeFromLastUpdate -= tickTime) {
            eventBus.dispatch(new UpdateEvent(++currentTick));
        }

        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
        eventBus.dispatch(new RenderEvent());
        GLFW.glfwSwapBuffers(windowPointer);

        int frameTime = settings.getFrameTime();
        if (frameTime == 0f) {
            return;
        }

        final long endFrameTime = frameStartTime + frameTime;
        //noinspection StatementWithEmptyBody
        while (System.currentTimeMillis() < endFrameTime) ;
    }

    @Override
    public boolean keepRunning() {
        return !settings.shouldClose();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int currentTick() {
        return currentTick;
    }
}
