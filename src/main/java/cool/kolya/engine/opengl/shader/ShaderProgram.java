package cool.kolya.engine.opengl.shader;

import cool.kolya.engine.DestructorProvider;
import cool.kolya.engine.opengl.uniform.Uniform;
import org.lwjgl.opengl.GL33;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Cleaner;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {

    private static final Logger log = LoggerFactory.getLogger(ShaderProgram.class);
    private final Map<String, Uniform<?>> uniformMap = new HashMap<>();
    private final EnumMap<Shader.Type, Shader> shaderMap = new EnumMap<>(Shader.Type.class);
    private final Cleaner.Cleanable destructor;

    private final int programId;

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        this.destructor = DestructorProvider.createDestructor(this, () -> glDeleteProgram(programId));
    }

    public int getProgramId() {
        return programId;
    }

    public void addShader(Shader shader) {
        shaderMap.put(shader.getType(), shader);
    }

    public <T> Uniform<T> createUniform(String name, BiFunction<String, Integer, Uniform<T>> defineFunc)
            throws Exception {
        int location = GL33.glGetUniformLocation(programId, name);
        if (location < GL33.GL_FALSE) {
            throw new Exception("Could not find uniform:" + name);
        }
        Uniform<T> uniform = defineFunc.apply(name, location);
        uniformMap.put(name, uniform);
        return uniform;
    }

    public <T> Uniform<T> getUniform(String name) {
        if (!uniformMap.containsKey(name)) {
            log.warn("Attemption to get not created unifrom \"{}\" from program with id {}", name, programId);
        }
        //noinspection unchecked
        return (Uniform<T>) uniformMap.get(name);
    }

    public void link() throws Exception {
        Collection<Shader> shaders = shaderMap.values();

        for (Shader shader : shaders) {
            shader.attach(programId);
        }

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        for (Shader shader : shaders) {
            shader.detach(programId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
            log.error("Error validating Shader code: {}", glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void free() {
        destructor.clean();
    }
}
