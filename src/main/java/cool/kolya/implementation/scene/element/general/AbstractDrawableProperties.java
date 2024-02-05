package cool.kolya.implementation.scene.element.general;

import cool.kolya.implementation.scene.element.general.matrix.Properties;
import org.joml.Vector4f;

public abstract class AbstractDrawableProperties implements DrawableProperties {

    private final Vector4f color = new Vector4f(1f);
    private final boolean[] dirtyMarks = new boolean[Properties.LENGTH];

    private boolean dirty;


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

    @Override
    public boolean isPropertyDirty(int propertyOrdinal) {
        return dirtyMarks[propertyOrdinal];
    }

    @Override
    public void setPropertyDirty(int propertyOrdinal, boolean dirty) {
        if (dirty) {
            markDirty();
        }
        dirtyMarks[propertyOrdinal] = dirty;
    }
}
