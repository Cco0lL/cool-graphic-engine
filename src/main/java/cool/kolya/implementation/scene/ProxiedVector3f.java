package cool.kolya.implementation.scene;

import org.joml.Vector3f;

public class ProxiedVector3f {

    private final AbstractElement element;
    private final int dependentMatrixIndex;
    private float x, y, z;

    public ProxiedVector3f(AbstractElement element, int dependentMatrixIndex, float fill) {
        this.element = element;
        this.dependentMatrixIndex = dependentMatrixIndex;
        x = y = z = fill;
    }

    public float x() {
        return x;
    }

    public void x(float x) {
        this.x = x;
        element.markMatrixDirty(dependentMatrixIndex);
    }

    public float y() {
        return y;
    }

    public void y(float y) {
        this.y = y;
        element.markMatrixDirty(dependentMatrixIndex);
    }

    public float z() {
        return z;
    }

    public void z(float z) {
        this.z = z;
        element.markMatrixDirty(dependentMatrixIndex);
    }

    public void set(Vector3f vec) {
        x = vec.x();
        y = vec.y();
        z = vec.z();
        element.markMatrixDirty(dependentMatrixIndex);
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        element.markMatrixDirty(dependentMatrixIndex);
    }

    public Vector3f toVector3f() {
        return new Vector3f(x, y, z);
    }
}
