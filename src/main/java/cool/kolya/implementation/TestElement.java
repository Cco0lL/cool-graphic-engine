package cool.kolya.implementation;

import cool.kolya.Engine;
import cool.kolya.engine.DestructorProvider;
import cool.kolya.engine.ProjectionMatrix;
import cool.kolya.engine.Resolution;
import cool.kolya.engine.opengl.shader.ShaderProgram;
import cool.kolya.engine.scene.AbstractElement;
import org.lwjgl.opengl.GL33;

import java.lang.ref.Cleaner;

public class TestElement extends AbstractElement {

    //private ShaderProgram program;
    private final int vaoId;
    private final int vboId;

    private final Cleaner.Cleanable destructor;

    private final float[] vertices = new float[]{
            0.0f, 0.5f, -10.0f,
            -0.5f, -0.5f, -10.0f,
            0.5f, -0.5f, -10.0f
    };

    public TestElement(ShaderProgram program) {
        super(program);

        vaoId = GL33.glGenVertexArrays();
        GL33.glBindVertexArray(vaoId);

        vboId = GL33.glGenBuffers();

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);

        GL33.glBindVertexArray(0);

        destructor = DestructorProvider.createDestructor(this, () -> {
            GL33.glDeleteBuffers(vboId);
            GL33.glDeleteVertexArrays(vaoId);
        });
    }

    @Override
    public void render() {
        program.bind();
        program.getUniform("projectionMatrix").set(ProjectionMatrix.get());
        program.getUniform("incolor").set(color);
        program.getUniform("elementMatrix").set(elementMatrix);
        program.getUniform("cameraMatrix").set(Engine.getCamera().getCameraMatrix());

        GL33.glBindVertexArray(vaoId);
        GL33.glEnableVertexAttribArray(0);
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 3);
        GL33.glDisableVertexAttribArray(0);
        GL33.glBindVertexArray(0);
        program.unbind();
    }

    @Override
    public void free() {
        destructor.clean();
    }
}
