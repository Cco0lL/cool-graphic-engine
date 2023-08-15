package cool.kolya.engine.scene;

import cool.kolya.Engine;
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
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
        for (AbstractElement element : elements) {
            element.render();
        }
        Engine.getWindow().refresh();
    }
}
