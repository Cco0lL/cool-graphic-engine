package cool.kolya.implementation.scene.element.general;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class PropertyVector3f extends PropertyVector2f implements IPropertyVector3f {

    protected float z;

    public PropertyVector3f(PropertyVector3f other, int propertyOrdinal) {
        this(other.x(), other.y(), other.z(), other.properties, propertyOrdinal);
    }

    public PropertyVector3f(float d, DrawableProperties properties, int propertyOrdinal) {
        this(d, d, d, properties, propertyOrdinal);
    }

    public PropertyVector3f(float x, float y, float z, DrawableProperties properties, int propertyOrdinal) {
        super(x, y, properties, propertyOrdinal);
        this.z = z;
    }

    @Override
    public float z() {
        return z;
    }

    @Override
    public void z(float z) {
        this.z = z;
        markDirty();
    }

    @Override
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        markDirty();
    }

    @Override
    public void set(IPropertyVector3f other) {
        set(other.x(), other.y(), other.z());
    }

    @Override
    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        markDirty();
    }

    @Override
    public void remove(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    @Override
    public void write(IPropertyVector3f dest) {
        dest.set(this);
    }

    @Override
    public Vector4f toVector4f() {
        return new Vector4f(x(), y(), z(), 1.0f);
    }

    @Override
    public Vector3f toVector3f() {
        return new Vector3f(x(), y(), z());
    }
}
