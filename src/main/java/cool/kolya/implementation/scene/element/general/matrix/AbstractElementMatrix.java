package cool.kolya.implementation.scene.element.general.matrix;

import cool.kolya.implementation.scene.element.general.DrawableProperties;
import org.joml.Matrix4f;

public abstract class AbstractElementMatrix implements ElementMatrix {

    protected final Matrix4f elementMatrix = new Matrix4f();

    @Override
    public Matrix4f getMatrix() {
        return elementMatrix;
    }

    public static abstract class Context extends AbstractElementMatrix
            implements ElementMatrix.Context {

        protected boolean dirty;

        @Override
        public boolean isDirty() {
            return dirty;
        }

        public void setDirty(boolean dirty) {
            this.dirty = dirty;
        }
    }

    public static class Drawable<P extends DrawableProperties> extends AbstractElementMatrix
            implements ElementMatrix.Drawable<P> {

        protected final P properties;
        protected final Matrix4f transformationMatrix = new Matrix4f();
        protected final Matrix4f[] transformationMatrices = new Matrix4f[Matrices.LENGTH];

        public Drawable(P properties) {
            this.properties = properties;
            for (int i = 0; i < transformationMatrices.length; i++) {
                transformationMatrices[i] = new Matrix4f();
            }
        }

        @Override
        public void update(ElementMatrix parentMatrix) {
            elementMatrix.set(parentMatrix.getMatrix());
            if (isDirty()) {
                cleanMatrices();
                transformationMatrix.identity();
                updateTransformationMatrix();
                properties.unmarkDirty();
            }
            elementMatrix.mul(transformationMatrix);
        }

        protected void updateTransformationMatrix() {
            for (Matrix4f matrix : transformationMatrices) {
                this.transformationMatrix.mul(matrix);
            }
        }

        protected void cleanMatrices() {
            for (int i = 0; i < transformationMatrices.length; i++) {
                Matrix4f mat = transformationMatrices[i];
                switch (i) {
                    case (Matrices.OFFSET_MATRIX) -> Transformations.TRANSLATE
                            .accept(properties.getOffset(), mat);
                    case (Matrices.ROTATION_MATRIX) -> Transformations.ROTATE
                            .accept(properties.getRotation(), mat);
                    case (Matrices.SCALE_MATRIX) -> Transformations.SCALE
                            .accept(properties.getScale(), mat);
                }
            }
        }

        @Override
        public Matrix4f getTransformationMatrix() {
            return transformationMatrix;
        }

        @Override
        public boolean isDirty() {
            return properties.isDirty();
        }
    }
}