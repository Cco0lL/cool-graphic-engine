package cool.kolya.engine;

import cool.kolya.Engine;
import org.lwjgl.opengl.GL33;

public interface Renderer {

    void render();

    default void clearAndRender() {
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
        render();
        Engine.getProcessor().getWindow().refresh();
    }
}
