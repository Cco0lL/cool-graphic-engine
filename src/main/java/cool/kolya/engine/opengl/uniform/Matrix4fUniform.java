package cool.kolya.engine.opengl.uniform;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class Matrix4fUniform extends AbstractUniform<Matrix4f> {

    public Matrix4fUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void set(Matrix4f value) {
        int matrixSize = 16;
        try (MemoryStack stack = MemoryStack.create(Float.BYTES * matrixSize)) {
            stack.push();
            FloatBuffer buffer = stack.mallocFloat(matrixSize);
            value.get(buffer);
            GL33.glUniformMatrix4fv(location, false, buffer);
        }
    }
}