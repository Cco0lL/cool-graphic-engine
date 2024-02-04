package cool.kolya.engine.util;

import cool.kolya.engine.Engine;
import cool.kolya.engine.data.CursorPosition;
import cool.kolya.engine.data.FrameBufferSize;
import cool.kolya.engine.data.WindowPosition;
import cool.kolya.engine.data.WindowSize;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

public class GLFWUtil {

    public static CursorPosition getCursorPosition(long windowPtr) {
        return Engine.getMainThreadTaskScheduler().blockingReturningRequest(() ->
                getCursorPositionUnsafe(windowPtr));
    }

    public static CompletableFuture<CursorPosition> getCursorPositionAsync(long windowPtr) {
        return Engine.getMainThreadTaskScheduler().returningRequest(() ->
                getCursorPositionUnsafe(windowPtr));
    }

    public static CursorPosition getCursorPositionUnsafe(long windowPtr) {
        double[] x = new double[1];
        double[] y = new double[1];
        GLFW.glfwGetCursorPos(windowPtr, x, y);
        return new CursorPosition(x[0], y[0]);
    }

    public static void setCursorPosition(long windowPtr, CursorPosition cursorPosition) {
        Engine.getMainThreadTaskScheduler().addTask(() ->
                GLFW.glfwSetCursorPos(windowPtr, cursorPosition.xPos(), cursorPosition.yPos()));
    }

    public static WindowPosition getWindowPosition(long windowPtr) {
        return Engine.getMainThreadTaskScheduler().blockingReturningRequest(() ->
                getWindowPositionUnsafe(windowPtr));
    }

    public static CompletableFuture<WindowPosition> getWindowPositionAsync(long windowPtr) {
        return Engine.getMainThreadTaskScheduler().returningRequest(() ->
                getWindowPosition(windowPtr));
    }

    public static WindowPosition getWindowPositionUnsafe(long windowPtr) {
        int[] x = new int[1];
        int[] y = new int[1];
        GLFW.glfwGetWindowPos(windowPtr, x, y);
        return new WindowPosition(x[0], y[0]);
    }

    public static void setWindowPosition(long windowPtr, WindowPosition windowPosition) {
        Engine.getMainThreadTaskScheduler().addTask(() ->
                GLFW.glfwSetWindowPos(windowPtr, windowPosition.xPos(), windowPosition.yPos()));
    }

    public static WindowSize getWindowSize(long windowPtr) {
        return Engine.getMainThreadTaskScheduler().blockingReturningRequest(() ->
                getWindowSizeUnsafe(windowPtr));
    }

    public static CompletableFuture<WindowSize> getWindowSizeAsync(long windowPtr) {
        return Engine.getMainThreadTaskScheduler().returningRequest(() ->
                getWindowSizeUnsafe(windowPtr));
    }

    public static WindowSize getWindowSizeUnsafe(long windowPtr) {
        int[] width = new int[1];
        int[] height = new int[1];
        GLFW.glfwGetWindowSize(windowPtr, width, height);
        return new WindowSize(width[0], height[0]);
    }

    public static void setWindowSize(long windowPtr, WindowSize windowSize) {
        Engine.getMainThreadTaskScheduler().addTask(() ->
                GLFW.glfwSetWindowSize(windowPtr, windowSize.width(), windowSize.height()));
    }

    public static FrameBufferSize getFrameBufferSize(long windowPtr) {
        return Engine.getMainThreadTaskScheduler().blockingReturningRequest(() ->
                getFrameBufferSizeUnsafe(windowPtr));
    }

    public static CompletableFuture<FrameBufferSize> getFrameBufferSizeAsync(long windowPtr) {
        return Engine.getMainThreadTaskScheduler().returningRequest(() ->
                getFrameBufferSizeUnsafe(windowPtr));
    }

    public static FrameBufferSize getFrameBufferSizeUnsafe(long windowPtr) {
        int[] width = new int[1];
        int[] height = new int[1];
        GLFW.glfwGetWindowSize(windowPtr, width, height);
        return new FrameBufferSize(width[0], height[0]);
    }

    /*public static boolean isWindowShouldClose(long windowPtr) {
        if (ThreadScopeUtil.isCurrentThreadMain()) {
            return isWindowShouldCloseUnsafe(windowPtr);
        } else {
            return isWindowShouldCloseAsync(windowPtr).join();
        }
    }

    public static boolean isWindowShouldCloseUnsafe(long windowPtr) {
        return GLFW.glfwWindowShouldClose(windowPtr);
    }

    public static CompletableFuture<Boolean> isWindowShouldCloseAsync(long windowPtr) {
        CompletableFuture<Boolean> cf = new CompletableFuture<>();
        Engine.getMainThreadTaskScheduler().offerInTaskQueue(() ->
                cf.complete(isWindowShouldCloseUnsafe(windowPtr)));
        return cf;
    }

    public static void setWindowShouldClose(long windowPtr, boolean statement) {
        Engine.getMainThreadTaskScheduler().addTask(() ->
                GLFW.glfwSetWindowShouldClose(windowPtr, statement));
    }*/

    public static void setTitle(long windowPtr, String titile) {
        Engine.getMainThreadTaskScheduler().addTask(() ->
                GLFW.glfwSetWindowTitle(windowPtr, titile));
    }

    public static void setIcon(long windowPtr, Path icon) {
        Engine.getMainThreadTaskScheduler().addTask(() -> {
            try (MemoryStack memStack = MemoryStack.stackPush();
                 InputStream is = Files.newInputStream(icon, StandardOpenOption.READ)) {
                BufferedImage iconImage = ImageIO.read(is);

                GLFWImage[] icons;
                String OS = System.getProperty("os.name");
                if (OS.contains("WIN")) {
                    icons = new GLFWImage[2];
                    icons[0] = imageToGLFWImage(convertToIcon(iconImage, 16), memStack);
                    icons[1] = imageToGLFWImage(convertToIcon(iconImage, 32), memStack);
                } else if (OS.contains("MAC")) {
                    icons = new GLFWImage[1];
                    icons[0] = imageToGLFWImage(convertToIcon(iconImage, 128), memStack);
                } else {
                    icons = new GLFWImage[1];
                    icons[0] = imageToGLFWImage(convertToIcon(iconImage, 32), memStack);
                }

                GLFWImage.Buffer iconsBuf = GLFWImage.malloc(icons.length, memStack);

                for (GLFWImage glfwIcon : icons)
                    iconsBuf.put(glfwIcon);

                GLFW.glfwSetWindowIcon(windowPtr, iconsBuf);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static GLFWImage imageToGLFWImage(BufferedImage image, MemoryStack memStack) {
        return GLFWImage.malloc(memStack)
                .set(image.getWidth(), image.getHeight(), imageToRGBABuf(image, memStack));
    }

    public static BufferedImage convertToIcon(BufferedImage image, int dimension) {
        BufferedImage iconImage = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graphics = iconImage.createGraphics();
        double scale = getIconScale(image, iconImage);
        double scaledWidth = image.getWidth() * scale;
        double scaledHeight = image.getHeight() * scale;
        graphics.drawImage(image, (int) ((iconImage.getWidth() - scaledWidth) / 2),
                (int) ((iconImage.getHeight() - scaledHeight) / 2), (int) (scaledWidth),
                (int) (scaledHeight), null);
        graphics.dispose();
        return iconImage;
    }

    public static double getIconScale(BufferedImage src, BufferedImage icon) {
        double widthScale = src.getWidth() > icon.getWidth() ? (double) src.getWidth() / icon.getWidth()
                : (double) icon.getWidth() / src.getWidth();
        double heightScale = src.getHeight() > icon.getHeight() ? (double) src.getHeight() / icon.getHeight()
                : (double) icon.getHeight() / src.getHeight();
        return Math.min(heightScale, widthScale);
    }

    public static ByteBuffer imageToRGBABuf(BufferedImage image, MemoryStack memStack) {
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer buf = memStack.malloc(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = image.getRGB(j, i);
                //RGBA from premultiplied ARGB
                buf.put((byte) ((color << 8) >> 24));
                buf.put((byte) ((color << 16) >> 24));
                buf.put((byte) ((color << 24) >> 24));
                buf.put((byte) (color >> 24));
            }
        }
        buf.flip();
        return buf;
    }
}
