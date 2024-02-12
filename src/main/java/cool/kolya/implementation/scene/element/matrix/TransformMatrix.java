package cool.kolya.implementation.scene.element.matrix;

import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.property.Property;
import org.joml.Matrix4f;

public interface TransformMatrix {

    Element getElement();

    Matrix4f get();

    void update();

    boolean isDirty();

    void setDirty(boolean dirty);

    void addTransformProperty(Property property);

    class ParentMatrixTransmitter implements TransformMatrix {

        private boolean dirty = false;
        private Matrix4f matrix;
        private final Element element;

        public ParentMatrixTransmitter(Element element) {
            this.element = element;
        }

        @Override
        public Element getElement() {
            return element;
        }

        @Override
        public Matrix4f get() {
            return matrix;
        }

        @Override
        public void update() {
            TransformMatrix parentMatrix = element.getParent().getTransformMatrix();
            if (parentMatrix.isDirty()) {
                matrix = parentMatrix.get();
                setDirty(true);
            }
        }

        @Override
        public boolean isDirty() {
            return dirty;
        }

        @Override
        public void setDirty(boolean dirty) {
            this.dirty = dirty;
        }

        @Override
        public void addTransformProperty(Property property) {}
    }
}
