package cool.kolya.engine.opengl.uniform;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class Vector2fUniform extends AbstractUniform<Vector2f> {

    public Vector2fUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void set(Vector2f value) {
        super.set(value);
        int vectorSize = 2;
        try (MemoryStack stack = MemoryStack.create(Float.BYTES * vectorSize)) {
            stack.push();
            FloatBuffer buf = stack.mallocFloat(vectorSize);
            value.get(buf);
            GL33.glUniform4fv(location, buf);
        }
    }
}
