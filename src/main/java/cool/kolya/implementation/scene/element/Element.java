package cool.kolya.implementation.scene.element;

import cool.kolya.implementation.scene.element.callback.Callback;
import cool.kolya.implementation.scene.element.matrix.TransformMatrix;
import cool.kolya.implementation.scene.element.property.TransformProperties;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector4f;

import java.util.UUID;

public interface Element {

    UUID getId();

    TransformProperties getProperties();

    Parent getParent();

    @ApiStatus.Internal
    void setParent(Parent parent);

    TransformMatrix getTransformMatrix();

    void setTransformMatrix(TransformMatrix transformMatrix);

    DrawingModule getDrawingModule();

    void setDrawingModule(DrawingModule drawingModule);

    InteractingModule getInteractingModule();

    void setInteractingModule(InteractingModule interactingModule);

    void updateAndRender();

    void setTexture(String texture);

    String getTexture();

    Vector4f getColor();

    void setColor(Vector4f color);

    void addCallback(Callback.LifecycleType type, Runnable callbackRunnable);
}
