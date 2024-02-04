package cool.kolya.engine.opengl.uniform;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class Vector4fUniform extends AbstractUniform<Vector4f> {

    public Vector4fUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void set(Vector4f value) {
        super.set(value);
        int vectorSize = 4;
        try (MemoryStack stack = MemoryStack.create(Float.BYTES * vectorSize)) {
            stack.push();
            FloatBuffer buf = stack.mallocFloat(vectorSize);
            value.get(buf);
            GL33.glUniform4fv(location, buf);
        }
    }
}
