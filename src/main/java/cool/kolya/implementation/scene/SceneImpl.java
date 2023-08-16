package cool.kolya.implementation.scene;

import org.lwjgl.opengl.GL33;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SceneImpl implements Scene {

    private final List<AbstractElement> elements = new ArrayList<>();

    @Override
    public Iterable<AbstractElement> getElements() {
        return Collections.unmodifiableCollection(elements);
    }

    @Override
    public void addElement(AbstractElement element) {
        elements.add(element);
    }

    @Override
    public void update() {
        Iterator<AbstractElement> iterator = elements.iterator();
        while (iterator.hasNext()) {
            AbstractElement e = iterator.next();
            e.update();
            if (!e.isEnabled())
                iterator.remove();
        }
    }

    @Override
    public void render() {
        for (AbstractElement element : elements) {
            element.render();
        }
    }
}
