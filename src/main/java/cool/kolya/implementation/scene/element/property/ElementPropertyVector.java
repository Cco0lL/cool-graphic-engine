package cool.kolya.implementation.scene.element.property;

import cool.kolya.implementation.scene.element.callback.Callback;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ElementPropertyVector implements PropertyVector {

    protected float x, y, z;
    protected final ElementTransformProperties properties;
    protected final Callback changeCallback = new Callback();
    protected final int propertyOffset;

    public ElementPropertyVector(ElementPropertyVector other, int propertyOffset) {
        this(other.x(), other.y(), other.z(), other.properties, propertyOffset);
    }

    public ElementPropertyVector(float d, ElementTransformProperties properties, int propertyOffset) {
        this(d, d, d, properties, propertyOffset);
    }

    public ElementPropertyVector(float x, float y, float z, ElementTransformProperties properties, int propertyOffset) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.properties = properties;
        this.propertyOffset = propertyOffset;
        properties.setProperty(propertyOffset, this);
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
    public float z() {
        return z;
    }

    @Override
    public void z(float z) {
        this.z = z;
        markDirty();
    }

    @Override
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
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
    public void set(Vector2f vec) {
        set(vec.x(), vec.y());
    }

    @Override
    public void set(Vector3f vec) {
        set(vec.x(), vec.y(), vec.z());
    }

    @Override
    public void set(PropertyVector other) {
        set(other.x(), other.y(), other.z());
    }

    @Override
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
        markDirty();
    }

    @Override
    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        markDirty();
    }

    @Override
    public void add(Vector2f vec) {
        add(vec.x(), vec.y());
    }

    @Override
    public void add(Vector3f vec) {
        add(vec.x(), vec.y(), vec.z());
    }

    @Override
    public void remove(float x, float y) {
        this.x -= x;
        this.y -= y;
        markDirty();
    }

    @Override
    public void remove(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        markDirty();
    }

    @Override
    public void remove(Vector2f vec) {
        remove(vec.x(), vec.y());
    }

    @Override
    public void remove(Vector3f vec) {
        remove(vec.x(), vec.y(), vec.z());
    }

    @Override
    public void write(PropertyVector dest) {
        dest.set(this);
    }

    @Override
    public void write(Vector2f dest) {
        dest.set(x(), y());
    }

    @Override
    public void write(Vector3f dest) {
        dest.set(x(), y(), z());
    }

    @Override
    public Callback getChangeCallback() {
        return changeCallback;
    }

    @Override
    public Vector4f toVector4f() {
        return new Vector4f(x(), y(), z(), 1f);
    }

    @Override
    public Vector3f toVector3f() {
        return new Vector3f(x(), y(), z());
    }

    @Override
    public Vector2f toVector2f() {
        return new Vector2f(x(), y());
    }

    protected void markDirty() {
        properties.setPropertyDirty(propertyOffset, true);
        changeCallback.run();
    }
}
