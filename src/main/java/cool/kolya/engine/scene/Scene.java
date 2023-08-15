package cool.kolya.engine.scene;

public interface Scene {

    Iterable<AbstractElement> getElements();

    void addElement(AbstractElement element);

    void update();

    void render();
}
