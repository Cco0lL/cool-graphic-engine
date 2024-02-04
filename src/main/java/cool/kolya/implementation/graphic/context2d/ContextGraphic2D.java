package cool.kolya.implementation.graphic.context2d;

import cool.kolya.engine.opengl.shader.Shader;
import cool.kolya.engine.opengl.shader.ShaderProgram;
import cool.kolya.engine.opengl.uniform.Matrix4fUniform;
import cool.kolya.engine.opengl.uniform.Vector4fUniform;
import cool.kolya.engine.util.ResourceUtil;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.lang.ref.Cleaner;

public class ContextGraphic2D {

    private final ShaderProgram shaderProgram;

    public ContextGraphic2D(Cleaner cleaner) {
        try {
            shaderProgram = new ShaderProgram(cleaner);
            loadShadersAndUniforms(cleaner);
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred on 2d graphic creation", e);
        }
    }

    public void enable() {
        shaderProgram.bind();
        clearUniforms();
    }

    public void disable() {
        shaderProgram.unbind();
    }

    public void scale(float x, float y) {
        shaderProgram.getUniform("scale").set(new Matrix4f().scale(x, y, 1f));
    }

    public void translate(float x, float y) {
        shaderProgram.getUniform("translate").set(new Matrix4f().translate(x, y, 0f));
    }

    public void rotate(float angle) {
        shaderProgram.getUniform("rotate").set(new Matrix4f().rotateAffineXYZ(0, angle, 0));
    }

    public void color(float r, float g, float b, float alpha) {
        shaderProgram.getUniform("incolor").set(new Vector4f(r, g, b, alpha));
    }

    public void clearUniforms() {
        scale(1f, 1f);
        translate(1f, 1f);
        rotate(0);
        color(0f,0f,0f,0f);
    }

    private void loadShadersAndUniforms(Cleaner cleaner) throws Exception {
        Shader vertexShader = new Shader(ResourceUtil.readResource("cool/kolya/2d_vertex_shader.vs"),
                Shader.Type.VERTEX, cleaner);
        Shader fragmentShader = new Shader(ResourceUtil.readResource("cool/kolya/fragment_shader.fs"),
                Shader.Type.FRAGMENT, cleaner);

        shaderProgram.addShader(vertexShader);
        shaderProgram.addShader(fragmentShader);

        shaderProgram.link();

        shaderProgram.createUniform("incolor", Vector4fUniform::new);
        shaderProgram.createUniform("scale", Matrix4fUniform::new);
        shaderProgram.createUniform("translate", Matrix4fUniform::new);
        shaderProgram.createUniform("rotate", Matrix4fUniform::new);
    }
}
