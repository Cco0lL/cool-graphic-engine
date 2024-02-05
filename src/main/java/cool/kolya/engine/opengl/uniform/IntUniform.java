package cool.kolya.engine.opengl.uniform;

import org.lwjgl.opengl.GL33;

public class IntUniform extends AbstractUniform<Integer> {

    public IntUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void set(Integer value) {
        GL33.glUniform1i(location, value);
    }
}
