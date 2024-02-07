package cool.kolya.implementation;

import cool.kolya.api.resource.DataSource;
import cool.kolya.api.util.GLFWUtil;
import cool.kolya.api.util.ResourceUtil;
import cool.kolya.engine.opengl.texture.Texture2D;
import cool.kolya.engine.opengl.texture.Texture2DImpl;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Texture2DManager {

    private static final ThreadLocal<ContextTexture2DManager> TEXTURE_MANAGER_THREAD_LOCAL = new ThreadLocal<>();

    public static void createContext() {
        TEXTURE_MANAGER_THREAD_LOCAL.set(new ContextTexture2DManager());
    }

    public static void loadTexture(DataSource textureSource) {
        getContext().loadTexture(textureSource);
    }

    public static Texture2D getTexture(String name) {
        Texture2D tex = getContext().getTexture(name);
        if (tex == null) {
            throw new IllegalArgumentException("texture with name " + name + " not loaded");
        }
        return tex;
    }

    private static ContextTexture2DManager getContext() {
        return TEXTURE_MANAGER_THREAD_LOCAL.get();
    }

    private static class ContextTexture2DManager {

        private final Map<String, Texture2D> textureMap = new HashMap<>();

        public Texture2D getTexture(String name) {
            return textureMap.get(name);
        }

        /**
         * Supports png only
         */
        public void loadTexture(DataSource textureSource) {
            if (!textureSource.name().endsWith(".png")) {
                throw new IllegalStateException(textureSource.name() + " isn't png");
            }
            ResourceUtil.loadResourceAndCompute(textureSource, (is) -> {
                try (MemoryStack memoryStack = MemoryStack.stackPush()) {
                    BufferedImage bufImage = ImageIO.read(is);
                    ByteBuffer imageBuf = GLFWUtil.imageToRGBABuf(bufImage, memoryStack);

                    int texId = GL33.glGenTextures();
                    GL33.glBindTexture(GL33.GL_TEXTURE_2D, texId);

                    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
                    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);

                    GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, bufImage.getWidth(),
                            bufImage.getHeight(), 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, imageBuf);

                    GL33.glBindTexture(GL11.GL_TEXTURE_2D, 0);

                    String name = textureSource.name().replaceAll(".png", "");
                    textureMap.put(name, new Texture2DImpl(texId, name));
                }
            });
        }
    }
}
