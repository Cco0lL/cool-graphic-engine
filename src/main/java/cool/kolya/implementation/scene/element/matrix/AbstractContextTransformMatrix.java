package cool.kolya.implementation.scene.element.matrix;

import cool.kolya.implementation.scene.element.property.Properties;
import cool.kolya.implementation.scene.element.ContextElement;
import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.property.TransformProperties;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class AbstractContextTransformMatrix implements TransformMatrix {

    protected final ContextElement context;
    protected final Matrix4f windowTransformMatrix = new Matrix4f();
    protected final Matrix4f scaleMatrix = new Matrix4f();
    protected final Matrix4f elementMatrix = new Matrix4f();
    protected final Matrix4f transformMatrix = new Matrix4f();
    protected boolean dirty;

    public AbstractContextTransformMatrix(ContextElement context) {
        this.context = context;
    }

    protected abstract void updateWindowTransformMatrix();

    @Override
    public Element getElement() {
        return context;
    }

    @Override
    public Matrix4f get() {
        return elementMatrix;
    }

    @Override
    public void update() {
        TransformProperties properties = context.getProperties();
        if (properties.isPropertyDirty(Properties.SCALE)) {
            setDirty(true);
            Vector3f scale = properties.getScale().toVector3f();
            Transformations.SCALE.accept(scale, scaleMatrix);
            properties.setPropertyDirty(Properties.SCALE, false);
        }
        if (isDirty()) {
            updateWindowTransformMatrix();
            transformMatrix.identity()
                    .mul(windowTransformMatrix)
                    .mul(scaleMatrix);
            elementMatrix.identity()
                    .mul(transformMatrix);
        }
    }

    @Override
    public Matrix4f getTransform() {
        return transformMatrix;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
