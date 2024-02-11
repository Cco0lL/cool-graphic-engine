package cool.kolya.implementation.scene.element.property;


import java.util.UUID;

public interface TransformProperties {

    UUID getElementId();

    PropertyVector getSize();

    PropertyVector getOffset();

    PropertyVector getScale();

    PropertyVector getXYZRotation();

    PropertyVector getAlign();

    PropertyVector getOrigin();

    PropertyVector getProperty(int propertyOffset);

    boolean isPropertyDirty(int propertyOffset);

    void setPropertyDirty(int propertyOffset, boolean dirty);

    void setDirtyAll(boolean dirty);
}
