package cool.kolya.engine.data;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public record Resolution(int width, int height) {

    public static Resolution getResolution() {
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        //noinspection DataFlowIssue
        return new Resolution(vidMode.width(), vidMode.height());
    }
}
