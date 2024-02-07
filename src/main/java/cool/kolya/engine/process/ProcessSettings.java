package cool.kolya.engine.process;

import cool.kolya.api.util.GLFWUtil;
import cool.kolya.engine.data.CursorPosition;
import cool.kolya.engine.data.FrameBufferSize;
import cool.kolya.engine.data.WindowPosition;
import cool.kolya.engine.data.WindowSize;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;

public class ProcessSettings {

    private final long ptr;
    //private float frameTime = 1000f / 60;
    private int tickTime = 1000 / 20;

    public ProcessSettings(long ptr) {
        this.ptr = ptr;
    }

    public synchronized void setShouldClose() {
        GLFW.glfwSetWindowShouldClose(ptr, true);
    }

    public synchronized boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(ptr);
    }

    /*public void setFrameRate(int frameRate) {
        if (frameRate == 0) {
            frameTime = 0;
            GLFW.glfwSwapInterval(1);
        } else {
            frameTime = 1000f / frameRate;
            glfwSwapInterval(0);
        }
    }*/

//    public float getFrameTime() {
//        return frameTime;
//    }

    public void setTickRate(int tickRate) {
        tickTime = 1000 / tickRate;
    }

    public int getTickTime() {
        return tickTime;
    }

    public void setTitle(String title) {
        GLFWUtil.setTitle(ptr, title);
    }

    public void setIcon(Path icon) {
        GLFWUtil.setIcon(ptr, icon);
    }

    public CursorPosition getCursorPosition() {
        return GLFWUtil.getCursorPosition(ptr);
    }

    public void setCursorPosition(CursorPosition cursorPosition) {
        GLFWUtil.setCursorPosition(ptr, cursorPosition);
    }

    public WindowPosition getWindowPosition() {
        return GLFWUtil.getWindowPosition(ptr);
    }

    public void setWindowPosition(WindowPosition windowPosition) {
        GLFWUtil.setWindowPosition(ptr, windowPosition);
    }

    public WindowSize getWindowSize() {
        return GLFWUtil.getWindowSize(ptr);
    }

    public void setWindowSize(WindowSize windowSize) {
        GLFWUtil.setWindowSize(ptr, windowSize);
    }

    public FrameBufferSize getFrameBufferSize() {
        return GLFWUtil.getFrameBufferSize(ptr);
    }
}
