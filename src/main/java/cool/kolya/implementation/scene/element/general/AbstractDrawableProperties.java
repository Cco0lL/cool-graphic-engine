package cool.kolya.implementation.scene.element.general;

import org.joml.Vector4f;

public abstract class AbstractDrawableProperties implements DrawableProperties {

    private final PropertyVector3f offset = new PropertyVector3f(0f, this);
    private final PropertyVector3f scale = new PropertyVector3f(1f, this);
    private final PropertyVector3f rotation = new PropertyVector3f(0f, this);
    private final Vector4f color = new Vector4f(1f);

    private boolean dirty;

    @Override
    public IPropertyVector3f getOffset() {
        return offset;
    }

    @Override
    public IPropertyVector3f getScale() {
        return scale;
    }

    @Override
    public IPropertyVector3f getRotation() {
        return rotation;
    }

    @Override
    public Vector4f getColor() {
        return color;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void unmarkDirty() {
        dirty = false;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
}
