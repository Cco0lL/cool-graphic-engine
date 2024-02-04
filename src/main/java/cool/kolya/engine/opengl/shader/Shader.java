package cool.kolya.engine.opengl.shader;

import org.lwjgl.opengl.GL33;

import java.lang.ref.Cleaner;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private final int id;
    private final Type type;
    private final Cleaner.Cleanable destructor;

    public Shader(String code, Type type, Cleaner cleaner) throws Exception {
        this.type = type;
        id = GL33.glCreateShader(type.getGlCode());
        if (id == GL33.GL_ZERO) {
            throw new Exception("Error creating shader. Type: " + type.getGlCode());
        }

        glShaderSource(id, code);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(id, 1024));
        }
        //noinspection CapturingCleaner
        destructor = cleaner.register(this, () -> {
            GL33.glDeleteShader(id);
        });
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void attach(int programId) {
        GL33.glAttachShader(programId, id);
    }

    public void detach(int programId) {
        GL33.glDetachShader(programId, id);
    }

    public void free() {
        destructor.clean();
    }

    public enum Type {
        VERTEX(GL33.GL_VERTEX_SHADER),
        FRAGMENT(GL33.GL_FRAGMENT_SHADER),
        GEOMETRY(GL33.GL_GEOMETRY_SHADER);

        private final int glCode;

        Type(int glCode) {
            this.glCode = glCode;
        }

        public int getGlCode() {
            return glCode;
        }
    }
}
