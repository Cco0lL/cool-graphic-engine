package cool.kolya.implementation.scene.element;

/**
 * Represents a module that is drawing something. One module instance can be
 * used in several elements and can be bound to a single element only in the same time.
 * Uses bounded element properties for drawing
 */
public interface DrawingModule {

    Element boundedElement();

    void setBoundedElement(Element element);

    void update();

    void draw();

    record Empty(Element boundedElement) implements DrawingModule {

        @Override
        public void setBoundedElement(Element element) {}

        @Override
        public void update() {}

        @Override
        public void draw() {}
    }
}
