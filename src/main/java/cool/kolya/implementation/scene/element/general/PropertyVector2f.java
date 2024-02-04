package cool.kolya.implementation.scene.element.general;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PropertyVector2f implements IPropertyVector2f {

    protected float x, y;
    protected final DrawableProperties properties;
    protected final int propertyOrdinal;

    public PropertyVector2f(PropertyVector2f other, int propertyOrdinal) {
        this(other.x(), other.y(), other.properties, propertyOrdinal);
    }

    public PropertyVector2f(float d, DrawableProperties properties, int propertyOrdinal) {
        this(d, d, properties, propertyOrdinal);
    }

    public PropertyVector2f(float x, float y, DrawableProperties properties, int propertyOrdinal) {
        this.x = x;
        this.y = y;
        this.properties = properties;
        this.propertyOrdinal = propertyOrdinal;
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public void x(float x) {
        this.x = x;
        markDirty();
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public void y(float y) {
        this.y = y;
        markDirty();
    }

    @Override
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
        markDirty();
    }

    @Override
    public void set(IPropertyVector2f other) {
        set(other.x(), other.y());
    }

    @Override
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
        markDirty();
    }

    @Override
    public void remove(float x, float y) {
        this.x -= x;
        this.y -= y;
        markDirty();
    }

    @Override
    public void write(IPropertyVector2f dest) {
        dest.set(this);
    }

    @Override
    public Vector4f toVector4f() {
        return new Vector4f(x(), y(), 0f, 1f);
    }

    @Override
    public Vector3f toVector3f() {
        return new Vector3f(x(), y(), 0f);
    }

    @Override
    public Vector2f toVector2f() {
        return new Vector2f(x(), y());
    }

    public void markDirty() {
        properties.setPropertyDirty(propertyOrdinal, true);
    }
}
