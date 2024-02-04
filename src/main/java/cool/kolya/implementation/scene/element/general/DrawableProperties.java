package cool.kolya.implementation.scene.element.general;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector4f;

public interface DrawableProperties extends Cloneable {

    IPropertyVector3f getOffset();

    IPropertyVector3f getRotation();

    IPropertyVector3f getScale();

    Vector4f getColor();

    @ApiStatus.Internal
    void markDirty();

    @ApiStatus.Internal
    void unmarkDirty();

    @ApiStatus.Internal
    boolean isDirty();
}
