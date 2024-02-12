package cool.kolya.implementation.graphic.element2d;

import cool.kolya.api.resource.ResourceSource;
import cool.kolya.engine.opengl.shader.Shader;
import cool.kolya.engine.opengl.shader.ShaderProgram;
import cool.kolya.engine.opengl.texture.Texture2D;
import cool.kolya.engine.opengl.uniform.IntUniform;
import cool.kolya.engine.opengl.uniform.Matrix4fUniform;
import cool.kolya.engine.opengl.uniform.Vector4fUniform;
import cool.kolya.api.util.ResourceUtil;
import cool.kolya.implementation.Texture2DManager;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.lang.ref.Cleaner;

public class ElementContextGraphic2D {

    private final ShaderProgram shaderProgram;
    private Texture2D bindTexture;

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
        if (bindTexture != null) {
            textureUnbind();
        }
    }

    public void windowMatrix(Matrix4f windowMatrix) {
        shaderProgram.getUniform("window").set(windowMatrix);
    }

    public void elementMatrix(Matrix4f elementMatrix) {
        shaderProgram.getUniform("element").set(elementMatrix);
    }

    public void color(Vector4f color) {
        shaderProgram.getUniform("incolor").set(color);
    }

    public void textureBind(String texture) {
        bindTexture = Texture2DManager.getTexture(texture);
        bindTexture.bind();
        shaderProgram.getUniform("sampler").set(0);
        shaderProgram.getUniform("texture").set(1);
    }

    public void textureUnbind() {
        bindTexture.unbind();
        shaderProgram.getUniform("texture").set(0);
        bindTexture = null;
    }

    private void loadShadersAndUniforms(Cleaner cleaner) throws Exception {
        Shader vertexShader = new Shader(
                ResourceUtil.readResource(new ResourceSource("cool/kolya/2d_element_vertex_shader.vs")),
                Shader.Type.VERTEX, cleaner);
        Shader fragmentShader = new Shader(
                ResourceUtil.readResource(new ResourceSource("cool/kolya/2d_element_fragment_shader.fs")),
                Shader.Type.FRAGMENT, cleaner);

        shaderProgram.addShader(vertexShader);
        shaderProgram.addShader(fragmentShader);

        shaderProgram.link();

        shaderProgram.createUniform("incolor", Vector4fUniform::new);
        shaderProgram.createUniform("element", Matrix4fUniform::new);
        shaderProgram.createUniform("window", Matrix4fUniform::new);
        shaderProgram.createUniform("texture", IntUniform::new);
        shaderProgram.createUniform("sampler", IntUniform::new);
    }
}
