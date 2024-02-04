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

    public static abstract class Drawable<P extends DrawableProperties>
            extends AbstractElementMatrix
            implements ElementMatrix.Drawable<P> {

        protected final P properties;
        protected final Matrix4f transformationMatrix = new Matrix4f();

        public Drawable(P properties) {
            this.properties = properties;
        }

        @Override
        public void update(ElementMatrix parentMatrix) {
            if (isDirty()) {
                updateMatrices();
                transformationMatrix.identity();
                updateTransformationMatrix();
            }
            if (isDirty() || parentMatrix.isDirty()) {
                elementMatrix.set(parentMatrix.getMatrix());
                elementMatrix.mul(transformationMatrix);
            }
        }

        protected abstract void updateTransformationMatrix();

        protected abstract void updateMatrices();

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