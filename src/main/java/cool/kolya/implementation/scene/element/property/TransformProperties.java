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

    PropertyVector getProperty(Property property);

    boolean isPropertyDirty(Property property);

    void setPropertyDirty(Property property, boolean dirty);

    void setDirtyAll(boolean dirty);
}
