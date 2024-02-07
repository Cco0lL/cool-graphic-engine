package cool.kolya.engine;

import cool.kolya.engine.callback.Callbacks;
import cool.kolya.engine.callback.listener.WindowCallbackListener;
import cool.kolya.engine.callback.listener.WindowCallbackListenerImpl;
import cool.kolya.engine.event.bus.EventBus;
import cool.kolya.engine.event.bus.EventBusImpl;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.engine.process.ProcessManager;
import cool.kolya.api.util.ThreadScopeUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.lang.ref.Cleaner;

public class Engine {

    public static final Cleaner CLEANER = Cleaner.create();
    private static final MainThreadTaskScheduler MAIN_THREAD_TASK_SCHEDULER = new MainThreadTaskScheduler();
    private static boolean LISTING_INPUTS;

    public static void initialize() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("GLFW init error");
        }

        ThreadScopeUtil.defineMainThread();

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        GLFW.glfwSetErrorCallback(((error, descriptionPointer) -> {
            String description = MemoryUtil.memUTF8(descriptionPointer);
            System.out.println("code: " + error + ", description: " + description);
        }));
    }

    public static EngineProcess newProcess() {
        long windowPtr = GLFW.glfwCreateWindow(600, 400, "Untitled",
                MemoryUtil.NULL, MemoryUtil.NULL);

        WindowCallbackListener windowCallbackListener = new WindowCallbackListenerImpl();
        Callbacks.initialize(windowCallbackListener, windowPtr);
        EventBus eventBus = new EventBusImpl();

        return new EngineProcess(windowPtr, eventBus, windowCallbackListener);
    }

    public static EngineProcess getContextProcess() {
        return ProcessManager.getContextProcess();
    }

    public static MainThreadTaskScheduler getMainThreadTaskScheduler() {
        return MAIN_THREAD_TASK_SCHEDULER;
    }

    public static boolean isListingInputs() {
        return LISTING_INPUTS;
    }

    public static void listenInputEventsAndActions() {
        ThreadScopeUtil.assertMainThread();
        LISTING_INPUTS = true;
        while (!ProcessManager.isNotActiveProcessors()) {
            try {
                getMainThreadTaskScheduler().computeTasks();
                GLFW.glfwPollEvents();
                Thread.sleep(1);
            } catch (Exception ex) {
                ex.printStackTrace(); //TODO
            }
        }
        GLFW.glfwTerminate();
        System.exit(0);
    }
}