package cool.kolya.implementation.scene.element.property;

import java.util.Arrays;
import java.util.UUID;

public class ElementTransformProperties implements TransformProperties {

    private final UUID elementId;
    private final PropertyVector[] propertyVectors = new PropertyVector[Properties.LENGTH];
    private final boolean[] dirtyMarks = new boolean[Properties.LENGTH];
    private final PropertyVector size = new ElementPropertyVector(0f, this, Properties.SIZE);
    private final PropertyVector offset = new ElementPropertyVector(0f, this, Properties.OFFSET);
    private final PropertyVector scale = new ElementPropertyVector(1f, this, Properties.SCALE);
    private final PropertyVector xyzRotation = new ElementPropertyVector(0f, this, Properties.ROTATION);
    private final PropertyVector align = new ElementPropertyVector(0f, this, Properties.ALIGN);
    private final PropertyVector origin = new ElementPropertyVector(0f, this, Properties.ORIGIN);

    public ElementTransformProperties(UUID elementId) {
        this.elementId = elementId;
        size.getChangeCallback().add(() -> setPropertyDirty(Properties.ORIGIN, true));
    }

    @Override
    public UUID getElementId() {
        return elementId;
    }

    @Override
    public PropertyVector getSize() {
        return size;
    }

    @Override
    public PropertyVector getOffset() {
        return offset;
    }

    @Override
    public PropertyVector getScale() {
        return scale;
    }

    @Override
    public PropertyVector getXYZRotation() {
        return xyzRotation;
    }

    @Override
    public PropertyVector getAlign() {
        return align;
    }

    @Override
    public PropertyVector getOrigin() {
        return origin;
    }

    @Override
    public PropertyVector getProperty(int propertyOffset) {
        return propertyVectors[propertyOffset];
    }

    @Override
    public boolean isPropertyDirty(int propertyOffset) {
        return dirtyMarks[propertyOffset];
    }

    @Override
    public void setPropertyDirty(int propertyOffset, boolean dirty) {
        dirtyMarks[propertyOffset] = dirty;
    }

    @Override
    public void setDirtyAll(boolean dirty) {
        Arrays.fill(dirtyMarks, dirty);
    }

    protected void setProperty(int propertyOffset, PropertyVector property) {
        propertyVectors[propertyOffset] = property;
    }
}
