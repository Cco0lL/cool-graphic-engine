package cool.kolya.engine.util;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class IconUtil {

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
        ByteBuffer buf = memStack.malloc(width * height * MemUtil.RGBA_PIXEL_SIZE);
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
