package cool.kolya.engine.opengl.uniform;

import cool.kolya.engine.util.MemUtil;
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
        int matrixRank = 4;
        int matrixSize = matrixRank * matrixRank;
        try (MemoryStack stack = MemoryStack.create(MemUtil.FLOAT_SIZE * matrixSize)) {
            stack.push();
            FloatBuffer buffer = stack.mallocFloat(matrixSize);
            value.get(buffer);
            GL33.glUniformMatrix4fv(location, false, buffer);
        }
    }
}
