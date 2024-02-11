package cool.kolya.implementation.scene.element.matrix;

import cool.kolya.implementation.scene.element.property.Properties;
import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.property.TransformProperties;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ElementTransformMatrix implements TransformMatrix {

    protected final Element element;
    private final Matrix4f elementMatrix = new Matrix4f();
    protected final Matrix4f transformationMatrix = new Matrix4f();
    private final Matrix4f[] transformationMatrices = new Matrix4f[Properties.MATRICES_LENGTH];
    protected boolean dirty = true;

    public ElementTransformMatrix(Element element) {
        this.element = element;
        for (int i = 0; i < transformationMatrices.length; i++) {
            transformationMatrices[i] = new Matrix4f();
        }
    }

    @Override
    public void update() {
        updateMatrices();
        if (isDirty()) {
            transformationMatrix.identity();
            for (Matrix4f matrix : transformationMatrices) {
                this.transformationMatrix.mul(matrix);
            }
        }
        TransformMatrix parentMatrix = element.getParent().getTransformMatrix();
        if (isDirty() || parentMatrix.isDirty()) {
            elementMatrix.set(parentMatrix.get());
            elementMatrix.mul(transformationMatrix);
        }
    }

    protected void updateMatrices() {
        TransformProperties parentProperties = element.getParent().getProperties();
        TransformProperties properties = element.getProperties();
        for (int i = 0; i < transformationMatrices.length; i++) {
            if (!properties.isPropertyDirty(i)) {
                continue;
            }
            Matrix4f mat = transformationMatrices[i];
            switch (i) {
                case Properties.ALIGN -> {
                    Vector3f alignVec = properties.getAlign().toVector3f();
                    Vector3f parentSizeVec = parentProperties.getSize().toVector3f();
                    Transformations.TRANSLATE.accept(alignVec.mul(parentSizeVec), mat);
                }
                case Properties.OFFSET -> Transformations.TRANSLATE
                        .accept(properties.getOffset().toVector3f(), mat);
                case (Properties.ROTATION) -> Transformations.ROTATE
                        .accept(properties.getXYZRotation().toVector3f(), mat);
                case (Properties.SCALE) -> Transformations.SCALE
                        .accept(properties.getScale().toVector3f(), mat);
                case (Properties.ORIGIN) -> {
                    Vector3f originVec = properties.getOrigin().toVector3f().negate();
                    Vector3f size = properties.getSize().toVector3f();
                    Transformations.TRANSLATE.accept(originVec.mul(size), mat);
                }
            }
            properties.setPropertyDirty(i, false);
            setDirty(true);
        }
    }

    @Override
    public Element getElement() {
        return element;
    }

    @Override
    public Matrix4f get() {
        return elementMatrix;
    }

    @Override
    public Matrix4f getTransform() {
        return transformationMatrix;
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