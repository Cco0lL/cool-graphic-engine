package cool.kolya.implementation.scene.element.matrix;

import cool.kolya.api.util.Assertion;
import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.property.Property;
import cool.kolya.implementation.scene.element.property.TransformProperties;
import org.joml.Matrix4f;

public class TransformMatrixImpl implements TransformMatrix {

    protected final Element element;
    protected final Matrix4f transformMatrix = new Matrix4f();
    protected final Matrix4f[] transformMatrices = new Matrix4f[Property.MATRICES_LENGTH];
    protected boolean dirty;

    public TransformMatrixImpl(Element element) {
        this.element = element;
    }

    @Override
    public void update() {
        updateMatrices();
        if (isDirty()) {
            transformMatrix.identity();
            for (Matrix4f matrix : transformMatrices) {
                if (matrix == null) {
                    continue;
                }
                this.transformMatrix.mul(matrix);
            }
        }
    }

    @Override
    public void addTransformProperty(Property property) {
        Assertion.assertNotEqual(property, Property.SIZE, "Size is not a transform property");
        transformMatrices[property.ordinal()] = new Matrix4f();
    }

    @Override
    public Element getElement() {
        return element;
    }

    @Override
    public Matrix4f get() {
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

    protected void updateMatrices() {
        TransformProperties properties = element.getProperties();
        for (int i = 0; i < transformMatrices.length; i++) {
            Matrix4f mat = transformMatrices[i];
            if (mat == null) {
                continue;
            }
            Property property = Property.get(i);
            if (!properties.isPropertyDirty(property)) {
                continue;
            }
            property.getUpdateFunction().update(element, mat);
            properties.setPropertyDirty(property, false);
            setDirty(true);
        }
    }
}
