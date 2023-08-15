package cool.kolya.engine.opengl.uniform;

import cool.kolya.engine.util.MemUtil;
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
        int vectorDimension = 4;
        try (MemoryStack stack = MemoryStack.create(MemUtil.FLOAT_SIZE * vectorDimension)) {
            stack.push();
            FloatBuffer buf = stack.mallocFloat(vectorDimension);
            value.get(buf);
            GL33.glUniform4fv(location, buf);
        }
    }
}
