package cool.kolya.implementation.scene.element.property;

import java.util.Arrays;

public class TransformPropertiesImpl implements TransformProperties {

    private final PropertyVector[] propertyVectors = new PropertyVector[Property.LENGTH];
    private final boolean[] dirtyMarks = new boolean[Property.LENGTH];
    private final PropertyVector size = new ElementPropertyVector(0f, this, Property.SIZE);
    private final PropertyVector offset = new ElementPropertyVector(0f, this, Property.OFFSET);
    private final PropertyVector scale = new ElementPropertyVector(1f, this, Property.SCALE);
    private final PropertyVector xyzRotation = new ElementPropertyVector(0f, this, Property.ROTATION);
    private final PropertyVector align = new ElementPropertyVector(0f, this, Property.ALIGN);
    private final PropertyVector origin = new ElementPropertyVector(0f, this, Property.ORIGIN);

    public TransformPropertiesImpl() {
        size.getChangeCallback().add(() -> setPropertyDirty(Property.ORIGIN, true));
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
    public PropertyVector getProperty(Property property) {
        return propertyVectors[property.ordinal()];
    }

    @Override
    public boolean isPropertyDirty(Property property) {
        return dirtyMarks[property.ordinal()];
    }

    @Override
    public void setPropertyDirty(Property property, boolean dirty) {
        dirtyMarks[property.ordinal()] = dirty;
    }

    @Override
    public void setDirtyAll(boolean dirty) {
        Arrays.fill(dirtyMarks, dirty);
    }

    protected void setProperty(Property property, PropertyVector propertyVec) {
        propertyVectors[property.ordinal()] = propertyVec;
    }
}
