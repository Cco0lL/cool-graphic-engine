package cool.kolya.implementation;

import cool.kolya.api.resource.DataSource;
import cool.kolya.api.util.ResourceUtil;
import cool.kolya.engine.opengl.text.Character;
import cool.kolya.engine.opengl.text.Font;
import cool.kolya.engine.opengl.texture.Texture2D;
import cool.kolya.engine.opengl.texture.Texture2DImpl;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class FontManager {

    private static final ThreadLocal<ContextFontManager> FONT_MANAGER_THREAD_LOCAL = new ThreadLocal<>();

    public static void createContext() {
        FONT_MANAGER_THREAD_LOCAL.set(new ContextFontManager());
    }

    public static Font getActiveFont() {
        return getContext().getActiveFont();
    }

    public static void setActiveFont(String name) {
        getContext().setActiveFont(name);
    }

    public static void loadFont(DataSource fontSource) {
        getContext().loadFont(fontSource);
    }

    public static Font getFont(String name) {
        return getContext().getFont(name);
    }

    private static ContextFontManager getContext() {
        return FONT_MANAGER_THREAD_LOCAL.get();
    }

    private static class ContextFontManager {

        private Font activeFont;
        private final Map<String, Font> fontMap = new HashMap<>();

        void setActiveFont(String name) {
            activeFont = getFont(name);
            //TODO font change event
        }

        Font getActiveFont() {
            return activeFont;
        }

        Font getFont(String name) {
            Font font = fontMap.get(name);
            if (font == null) {
                throw new NullPointerException("font with name " + name + " isn't loaded");
            }
            return font;
        }

        /**
         * currently supports load from 32 to 126 ASCII chars, 32 pixels height, ttf format
         */
        void loadFont(DataSource fontSource) {
            if (!fontSource.name().endsWith(".ttf")) {
                throw new IllegalStateException(fontSource.name() + " isn't ttf");
            }
            ResourceUtil.loadResourceAndCompute(fontSource, is -> {
                try (MemoryStack memoryStack = MemoryStack.create((1 << 20) * 2)) {
                    memoryStack.push();
                    ByteBuffer ttfbuf = memoryStack.malloc(is.available());
                    ttfbuf.put(is.readAllBytes());
                    ttfbuf.flip();

                    STBTTFontinfo info = STBTTFontinfo.create();
                    if (!STBTruetype.stbtt_InitFont(info, ttfbuf)) {
                        throw new IllegalStateException("Failed to initialize font information.");
                    }


                    ByteBuffer temp_bitmap = memoryStack.malloc(512 * 512);
                    STBTTBakedChar.Buffer stbchars = STBTTBakedChar.calloc(96);

                    STBTruetype.stbtt_BakeFontBitmap(ttfbuf, 32f, temp_bitmap,
                            512, 512, 32, stbchars);

                    //formatting alpha to rgba white color with alpha
                    ByteBuffer rgba = memoryStack.calloc(4 * 512 * 512);
                    while (temp_bitmap.hasRemaining()) {
                        rgba.putInt(0xFFFFFF | temp_bitmap.get());
                    }
                    rgba.flip();

                    //loading bitmap tex
                    int texId = GL33.glGenTextures();
                    GL33.glBindTexture(GL11.GL_TEXTURE_2D, texId);
                    GL33.glPixelStorei(GL33.GL_UNPACK_SWAP_BYTES, 1);

                    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_S, GL33.GL_CLAMP_TO_EDGE);
                    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_T, GL33.GL_CLAMP_TO_EDGE);
                    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
                    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);

                    GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, 512, 512, 0,
                            GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, rgba);

                    GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);

                    //loading char bounding boxes with additional glyph's data
                    Character[] chars = new Character[96];
                    for (int i = 32; i < 128; i++) {
                        int index = i - 32;

                        STBTTAlignedQuad alignedQuad = STBTTAlignedQuad.calloc(memoryStack);
                        FloatBuffer xPos = memoryStack.callocFloat(1),
                                yPos = memoryStack.callocFloat(1);
                        IntBuffer ascentWidth = memoryStack.callocInt(1),
                                xBearing = memoryStack.callocInt(1);

                        STBTruetype.stbtt_GetCodepointHMetrics(info, index, ascentWidth, xBearing);
                        STBTruetype.stbtt_GetBakedQuad(stbchars, 512, 512,
                                index, xPos, yPos, alignedQuad, true);

                        STBTTBakedChar stbchar = stbchars.get(index);
                        Character character = new Character((char) i, alignedQuad, stbchar.xadvance());
                        chars[index] = character;
                    }

                    //creating and caching font instance
                    String name = fontSource.name().replaceAll(".ttf", "");
                    Texture2D fontBitmapTex = new Texture2DImpl(texId, name);
                    Font font = new Font(name, fontBitmapTex, chars);

                    fontMap.put(name, font);
                    Texture2DManager.addLoadedTexture(name, fontBitmapTex);

                    //free resources
                    stbchars.free();
                    // info.free();
                }
            });
        }
    }
}

