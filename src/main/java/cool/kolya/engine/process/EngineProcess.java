package cool.kolya.engine.process;

import cool.kolya.engine.Engine;
import cool.kolya.engine.event.ProcessStartEvent;
import cool.kolya.engine.event.ProcessTerminateEvent;
import cool.kolya.engine.event.bus.EventBus;
import cool.kolya.engine.process.loop.DefaultProcessLoopProvider;
import cool.kolya.engine.process.loop.ProcessLoop;
import cool.kolya.engine.callback.listener.WindowCallbackListener;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class EngineProcess {

    private final long windowPtr;
    private final ProcessSettings settings;
    private final EventBus eventBus;
    private ProcessLoop processLoop;

    public EngineProcess(long windowPtr, EventBus eventBus, WindowCallbackListener windowCallbackListener) {
        this.windowPtr = windowPtr;
        this.eventBus = eventBus;
        this.settings = new ProcessSettings(windowPtr);
        ContextInputEventsManager.createProcessQueue(windowPtr);
        ContextInputEventsManager.linkCallbacksWithProcessQueue(windowCallbackListener);
    }

    public void start() {
        try {
            GLFW.glfwMakeContextCurrent(windowPtr);
            GL.createCapabilities();

            ProcessManager.setActiveContextProcess(this);
            eventBus.dispatch(new ProcessStartEvent());

            Engine.getMainThreadTaskScheduler().addTask(() -> GLFW.glfwShowWindow(windowPtr));

            if (processLoop == null) {
                processLoop = DefaultProcessLoopProvider.newProcessLoop(this);
            }
            processLoop.run();
        } catch (Exception ex) {
            ex.printStackTrace(); //TODO
        } finally {
            eventBus.dispatch(new ProcessTerminateEvent());
            ProcessManager.removeActiveContextProcess();
            Engine.getMainThreadTaskScheduler().addTask(() -> {
                GLFW.glfwDestroyWindow(windowPtr);
                ContextInputEventsManager.removeProcessQueue(windowPtr);
            });
        }
    }

    public long getWindowPtr() {
        return windowPtr;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public ProcessSettings getSettings() {
        return settings;
    }

    public int currentTick() {
        return processLoop.currentTick();
    }

    public void setProcessLoop(ProcessLoop processLoop) {
        if (processLoop != null && processLoop.isRunning()) {
            throw new IllegalStateException("Attempt to change process loop since it's already started");
        }
        this.processLoop = processLoop;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EngineProcess process = (EngineProcess) o;
        return windowPtr == process.windowPtr;
    }

    @Override
    public int hashCode() {
        return (int) (windowPtr ^ (windowPtr >>> 32));
    }
}
