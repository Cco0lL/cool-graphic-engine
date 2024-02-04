package cool.kolya.implementation.graphic.element2d;

import cool.kolya.engine.opengl.shader.Shader;
import cool.kolya.engine.opengl.shader.ShaderProgram;
import cool.kolya.engine.opengl.uniform.Matrix4fUniform;
import cool.kolya.engine.opengl.uniform.Vector4fUniform;
import cool.kolya.engine.util.ResourceUtil;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.lang.ref.Cleaner;

public class ElementContextGraphic2D {

    private final ShaderProgram shaderProgram;

    public ElementContextGraphic2D(Cleaner cleaner) {
        try {
            shaderProgram = new ShaderProgram(cleaner);
            loadShadersAndUniforms(cleaner);
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred on 2d graphic creation", e);
        }
    }

    public void enable() {
        shaderProgram.bind();
    }

    public void disable() {
        shaderProgram.unbind();
    }

    public void elementMatrix(Matrix4f elementMatrix) {
        shaderProgram.getUniform("element").set(elementMatrix);
    }

    public void color(Vector4f color) {
        shaderProgram.getUniform("incolor").set(color);
    }

    private void loadShadersAndUniforms(Cleaner cleaner) throws Exception {
        Shader vertexShader = new Shader(ResourceUtil.readResource("cool/kolya/2d_element_vertex_shader.vs"),
                Shader.Type.VERTEX, cleaner);
        Shader fragmentShader = new Shader(ResourceUtil.readResource("cool/kolya/fragment_shader.fs"),
                Shader.Type.FRAGMENT, cleaner);

        shaderProgram.addShader(vertexShader);
        shaderProgram.addShader(fragmentShader);

        shaderProgram.link();

        shaderProgram.createUniform("incolor", Vector4fUniform::new);
        shaderProgram.createUniform("element", Matrix4fUniform::new);
    }
}
