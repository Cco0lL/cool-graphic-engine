package cool.kolya.engine.scene;

import cool.kolya.engine.opengl.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.Arrays;

public abstract class AbstractElement {

    private final Matrix4f[] matrices = new Matrix4f[Matrices.LENGTH];
    private final boolean[] matricesDirtyMarks = new boolean[Matrices.LENGTH];
    private boolean dirty = false;
    protected final ShaderProgram program;
    protected final Matrix4f elementMatrix = new Matrix4f();
    protected final ProxiedVector3f offset = new ProxiedVector3f(this, Matrices.OFFSET_MATRIX, 0f);
    protected final ProxiedVector3f scale = new ProxiedVector3f(this, Matrices.SCALE_MATRIX, 1f);
    protected final ProxiedVector3f rotation = new ProxiedVector3f(this, Matrices.ROTATION_MATRIX, 0f);
    protected Vector4f color = new Vector4f(1f);
    protected boolean enabled = true;

    public AbstractElement(ShaderProgram program) {
        this.program = program;
        for (int i = 0; i < matrices.length; i++) {
            matrices[i] = new Matrix4f();
        }
        Arrays.fill(matricesDirtyMarks, false);
    }

    public ShaderProgram getProgram() {
        return program;
    }

    public ProxiedVector3f getOffset() {
        return offset;
    }

    public ProxiedVector3f getRotation() {
        return rotation;
    }

    public ProxiedVector3f getScale() {
        return scale;
    }

    public Vector4f getColor() {
        return color;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void update() {
        if (dirty) {
            cleanMatrices();
            elementMatrix.identity();
            for (Matrix4f matrix : matrices) {
                elementMatrix.mul(matrix);
            }
        }
    }

    public abstract void render();

    public abstract void free();

    protected void markMatrixDirty(int matrixIndex) {
        matricesDirtyMarks[matrixIndex] = true;
        dirty = true;
    }

    protected void cleanMatrices() {
        for (int i = 0; i < matrices.length; i++) {
            if (matricesDirtyMarks[i]) {
                switch(i) {
                    case (Matrices.OFFSET_MATRIX) -> matrices[i].identity()
                                .translate(-offset.x(), -offset.y(), -offset.z());
                    case (Matrices.ROTATION_MATRIX) -> matrices[i].identity()
                            .rotateAffineXYZ(
                                    (float) Math.toRadians(rotation.x()),
                                    (float) Math.toRadians(rotation.y()),
                                    (float) Math.toRadians(rotation.z()));
                    case (Matrices.SCALE_MATRIX) -> matrices[i].identity()
                            .scale(scale.toVector3f());
                }
            }
        }
    }
}
