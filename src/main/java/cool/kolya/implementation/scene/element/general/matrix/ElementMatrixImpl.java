package cool.kolya.implementation.scene.element.general.matrix;

import cool.kolya.implementation.scene.element.general.DrawableProperties;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public abstract class ElementMatrixImpl implements ElementMatrix {

    protected final Matrix4f elementMatrix = new Matrix4f();

    @Override
    public Matrix4f getMatrix() {
        return elementMatrix;
    }

    public static abstract class Context extends ElementMatrixImpl
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

    public static class Drawable extends ElementMatrixImpl implements ElementMatrix.Drawable {

        protected final DrawableProperties properties;
        protected final Matrix4f transformationMatrix = new Matrix4f();
        private final Matrix4f[] transformationMatrices = new Matrix4f[Properties.LENGTH];

        public Drawable(DrawableProperties properties) {
            this.properties = properties;
            for (int i = 0; i < transformationMatrices.length; i++) {
                transformationMatrices[i] = new Matrix4f();
            }
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

        protected void updateTransformationMatrix() {
            for (Matrix4f matrix : transformationMatrices) {
                this.transformationMatrix.mul(matrix);
            }
        }

        protected void updateMatrices() {
            for (int i = 0; i < transformationMatrices.length; i++) {
                if (!properties.isPropertyDirty(i)) {
                    continue;
                }
                Matrix4f mat = transformationMatrices[i];
                switch (i) {
                    case (Properties.OFFSET) -> Transformations.TRANSLATE
                            .accept(properties.getOffset().toVector4f(), mat);
                    case (Properties.ROTATION) -> Transformations.ROTATE
                            .accept(properties.getRotation().toVector4f(), mat);
                    case (Properties.SCALE) -> Transformations.SCALE
                            .accept(properties.getScale().toVector4f(), mat);
                    case (Properties.SIZE) -> {
                        Vector4f sizeOffset = properties.getSize()
                                .toVector4f()
                                .mul(-0.5f, -0.5f, -0.5f ,1f);
                        Transformations.TRANSLATE.accept(sizeOffset, mat);
                    }
                }
                properties.setPropertyDirty(i, false);
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