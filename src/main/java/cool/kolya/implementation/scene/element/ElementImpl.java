package cool.kolya.implementation.scene.element;

import cool.kolya.implementation.scene.element.matrix.TransformMatrix;
import cool.kolya.implementation.scene.element.property.ElementTransformProperties;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector4f;

import java.util.Objects;
import java.util.UUID;

public class ElementImpl implements Element {

    protected final UUID id = UUID.randomUUID();
    protected final ElementTransformProperties properties = new ElementTransformProperties(id);
    protected Parent parent;
    protected TransformMatrix transformMatrix = new TransformMatrix.ParentMatrixTransmitter(this);
    protected DrawingModule drawingModule = new DrawingModule.Empty(this);
    protected InteractingModule interactingModule = new InteractingModule.Empty();
    protected String texture;
    protected final Vector4f color = new Vector4f(1f);

    @Override
    public void updateAndRender() {
        update();
        render();
        getTransformMatrix().setDirty(false);
    }

    public void update() {
        getInteractingModule().updateMouseState();
        getTransformMatrix().update();
        getDrawingModule().update();
    }

    public void render() {
        getDrawingModule().draw();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public ElementTransformProperties getProperties() {
        return properties;
    }

    @Override
    public Parent getParent() {
        return parent;
    }

    @Override
    @ApiStatus.Internal
    public void setParent(Parent parent) {
        Objects.requireNonNull(parent, "parent is null");
         this.parent = parent;
    }

    @Override
    public TransformMatrix getTransformMatrix() {
        return transformMatrix;
    }

    @Override
    public void setTransformMatrix(TransformMatrix transformMatrix) {
        Objects.requireNonNull(transformMatrix, "transformMatrix is null");
        this.transformMatrix = transformMatrix;
        properties.setDirtyAll(true);
    }

    @Override
    public DrawingModule getDrawingModule() {
        return drawingModule;
    }

    @Override
    public void setDrawingModule(DrawingModule drawingModule) {
        Objects.requireNonNull(drawingModule, "drawingModule is null");
        this.drawingModule = drawingModule;
    }

    @Override
    public InteractingModule getInteractingModule() {
        return interactingModule;
    }

    @Override
    public void setInteractingModule(InteractingModule interactingModule) {
        Objects.requireNonNull(interactingModule, "interactingModule is null");
        this.interactingModule = interactingModule;
    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public void setTexture(String texture) {
        this.texture = texture;
    }

    @Override
    public Vector4f getColor() {
        return color;
    }

    @Override
    public void setColor(Vector4f color) {
        this.color.set(color);
    }
}