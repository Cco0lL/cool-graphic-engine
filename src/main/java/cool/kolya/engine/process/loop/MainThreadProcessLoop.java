package cool.kolya.engine.process.loop;

import cool.kolya.engine.process.EngineProcess;
import org.lwjgl.glfw.GLFW;

public class MainThreadProcessLoop extends ProcessLoopImpl {

    public MainThreadProcessLoop(EngineProcess process) {
        super(process);
    }

    @Override
    public void run() {
        super.run();
        GLFW.glfwTerminate();
    }

    @Override
    public void pollInputEvents() {
        GLFW.glfwPollEvents();
        super.pollInputEvents();
    }
}
