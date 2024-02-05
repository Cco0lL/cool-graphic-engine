package cool.kolya.implementation.scene.element.general;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector4f;

public interface DrawableProperties extends Cloneable {

    IPropertyVector getOffset();

    IPropertyVector getRotation();

    IPropertyVector getScale();

    IPropertyVector getSize();

    Vector4f getColor();

    @ApiStatus.Internal
    void markDirty();

    @ApiStatus.Internal
    void unmarkDirty();

    @ApiStatus.Internal
    boolean isDirty();

    @ApiStatus.Internal
    boolean isPropertyDirty(int propertyOrdinal);

    @ApiStatus.Internal
    void setPropertyDirty(int propertyOrdinal, boolean dirty);
}
