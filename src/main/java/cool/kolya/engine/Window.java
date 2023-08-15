package cool.kolya.engine;

import com.typesafe.config.Config;
import cool.kolya.Engine;
import cool.kolya.engine.config.DefaultConfiguration;
import cool.kolya.engine.event.*;
import cool.kolya.engine.util.MemUtil;
import cool.kolya.engine.window.*;
import cool.kolya.engine.window.callback.Callbacks;
import cool.kolya.engine.window.callback.WindowCallbackListener;
import cool.kolya.engine.event.bus.EventBus;
import cool.kolya.engine.util.IconUtil;
import cool.kolya.engine.window.callback.WindowCallbackListenerImpl;
import org.apache.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Window {

    private final Logger log = Logger.getLogger(Window.class);
    private final long windowPointer;
    private final WindowCallbackListener listener;
    private boolean cursorEntered = true;

    Window() {
        setupHints();

        //initialization
        Resolution resolution = Resolution.getResolution();
        windowPointer = GLFW.glfwCreateWindow(resolution.width(), resolution.height(),
                "Untitled", MemoryUtil.NULL, MemoryUtil.NULL);
        //there's an issue with window position on full screen, resize through maximize
        GLFW.glfwMaximizeWindow(windowPointer);

        applyDefaultConfiguration();

        FrameBufferSize frameBufferSize = getFrameBufferSize();
        ProjectionMatrix.update((float) frameBufferSize.width() / frameBufferSize.height());

        //init opengl context
        GLFW.glfwMakeContextCurrent(windowPointer);
        GL.createCapabilities();

        //definition and init callbacks listener
        listener = new WindowCallbackListenerImpl();
        Callbacks.initialize(listener, windowPointer);

        initCallbackHandlers();
    }

    public void show() {
        GLFW.glfwShowWindow(windowPointer);
    }

    public void refresh() {
        GLFW.glfwSwapBuffers(windowPointer);
    }

    public boolean isShouldBeClose() {
        return GLFW.glfwWindowShouldClose(windowPointer);
    }

    public void markAsShouldBeClose() {
        GLFW.glfwSetWindowShouldClose(windowPointer, true);
    }

    public WindowSize getSize() {
        WindowSize size;
        try (MemoryStack mem = MemoryStack.create(MemUtil.INT_SIZE * 2)) {
            mem.push();
            IntBuffer pWidth = mem.mallocInt(1);
            IntBuffer pHeight = mem.mallocInt(1);
            GLFW.glfwGetWindowSize(windowPointer, pWidth, pHeight);
            size = new WindowSize(pWidth.get(), pHeight.get());
        }
        return size;
    }

    public void setSize(int width, int height) {
        GLFW.glfwSetWindowSize(windowPointer, width, height);
    }

    public FrameBufferSize getFrameBufferSize() {
        FrameBufferSize size;
        try (MemoryStack mem = MemoryStack.create(MemUtil.INT_SIZE * 2)) {
            mem.push();
            IntBuffer width = mem.mallocInt(1);
            IntBuffer height = mem.mallocInt(1);
            GLFW.glfwGetFramebufferSize(windowPointer, width, height);
            size = new FrameBufferSize(width.get(), height.get());
        }
        return size;
    }

    public WindowPosition getPosition() {
        WindowPosition position;
        try (MemoryStack mem = MemoryStack.create(MemUtil.INT_SIZE * 2)) {
            mem.push();
            IntBuffer xPosP = mem.mallocInt(1);
            IntBuffer yPosP = mem.mallocInt(1);
            GLFW.glfwGetWindowPos(windowPointer, xPosP, yPosP);
            position = new WindowPosition(xPosP.get(), yPosP.get());
        }
        return position;
    }

    public void setPosition(int xPos, int yPos) {
        GLFW.glfwSetWindowPos(windowPointer, xPos, yPos);
    }

    public CursorPosition getCursorPosition() {
        CursorPosition position;
        try (MemoryStack mem = MemoryStack.create(MemUtil.DOUBLE_SIZE * 2)) {
            mem.push();
            DoubleBuffer xPosP = mem.mallocDouble(1);
            DoubleBuffer yPosY = mem.mallocDouble(1);
            GLFW.glfwGetCursorPos(windowPointer, xPosP, yPosY);
            position = new CursorPosition(xPosP.get(), yPosY.get());
        }
        return position;
    }

    public void setCursorPosition(double xPos, double yPos) {
        GLFW.glfwSetCursorPos(windowPointer, xPos, yPos);
    }

    public boolean isCursorEntered() {
        return cursorEntered;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(windowPointer, title);
    }

    public void setIcon(Path icon) {
        try (MemoryStack memStack = MemoryStack.stackPush();
             InputStream is = Files.newInputStream(icon, StandardOpenOption.READ)) {
            BufferedImage iconImage = ImageIO.read(is);

            GLFWImage[] icons;
            String OS = System.getProperty("os.name");
            if (OS.contains("WIN")) {
                icons = new GLFWImage[2];
                icons[0] = IconUtil.imageToGLFWImage(IconUtil.convertToIcon(iconImage, 16), memStack);
                icons[1] = IconUtil.imageToGLFWImage(IconUtil.convertToIcon(iconImage, 32), memStack);
            } else if (OS.contains("MAC")) {
                icons = new GLFWImage[1];
                icons[0] = IconUtil.imageToGLFWImage(IconUtil.convertToIcon(iconImage, 128), memStack);
            } else {
                icons = new GLFWImage[1];
                icons[0] = IconUtil.imageToGLFWImage(IconUtil.convertToIcon(iconImage, 32), memStack);
            }

            GLFWImage.Buffer iconsBuf = GLFWImage.malloc(icons.length, memStack);

            for (GLFWImage glfwIcon : icons)
                iconsBuf.put(glfwIcon);

            GLFW.glfwSetWindowIcon(windowPointer, iconsBuf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupHints() {
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
    }

    private void applyDefaultConfiguration() {
        Config config = DefaultConfiguration.get();
        if (config.hasPath("width") && config.hasPath("height")) {
            //TODO width and height validation
            int defWidth = config.getInt("width");
            int defHeight = config.getInt("height");
            setSize(defWidth, defHeight);
        }
        if (config.hasPath("xPos") && config.hasPath("yPos")) {
            //TODO xPos and yPos validation
            int defPosX = config.getInt("xPos");
            int defPosY = config.getInt("yPos");
            setPosition(defPosX, defPosY);
        }
    }

    private void initCallbackHandlers() {
        EventBus bus = EventBus.getInstance();
        listener.setCallbackHandler(Callbacks.MouseButton, (window, button, action, mods) -> {
            Engine.getMouseState().changeState(button, action);
            bus.dispatch(new ClickEvent(button, action, mods));
        }).setCallbackHandler(Callbacks.CursorEnter, (window, entered) -> {
            cursorEntered = entered;
            bus.dispatch(new CursorEnterEvent(entered));
        }).setCallbackHandler(Callbacks.Key, (window, key, scancode, action, mods) -> {
            bus.dispatch(new KeyTypeEvent(key, action, mods));
        }).setCallbackHandler(Callbacks.FramebufferSize, ((window, width, height) -> {
            ProjectionMatrix.update((float) width / height);
            GL11.glViewport(0, 0, width, height);
            bus.dispatch(new FrameBufferResizeEvent(width, height));
        })).setCallbackHandler(Callbacks.WindowSize, ((window, width, height) -> {
            bus.dispatch(new WindowResizeEvent(width, height));
        })).setCallbackHandler(Callbacks.CursorPos, ((window, xpos, ypos) -> {
            new CursorMoveEvent(xpos, ypos);
        }));
    }
}
