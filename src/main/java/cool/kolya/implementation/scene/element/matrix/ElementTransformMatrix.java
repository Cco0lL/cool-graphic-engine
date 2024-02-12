package cool.kolya.implementation.scene.element.matrix;

import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.property.Property;
import org.joml.Matrix4f;

public class ElementTransformMatrix extends TransformMatrixImpl {

    private final Matrix4f elementMatrix = new Matrix4f();

    public ElementTransformMatrix(Element element) {
        super(element);
        int matricesLength = Property.MATRICES_LENGTH;
        for (int i = 0; i < matricesLength; i++) {
            addTransformProperty(Property.get(i));
        }
    }

    @Override
    public void update() {
        super.update();
        TransformMatrix parentMatrix = element.getParent().getTransformMatrix();
        if (parentMatrix.isDirty()) {
            setDirty(true);
        }
        if (isDirty()) {
            elementMatrix.set(parentMatrix.get());
            elementMatrix.mul(transformMatrix);
        }
    }

    @Override
    public Matrix4f get() {
        return elementMatrix;
    }
}