package cool.kolya.engine.opengl.texture;

import org.lwjgl.opengl.GL33;

public class Texture2DImpl implements Texture2D {

    private final int id;
    private final String name;

    public Texture2DImpl(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void bind() {
        GL33.glActiveTexture(GL33.GL_TEXTURE0);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, id);
    }

    @Override
    public void unbind() {
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
    }
}
