package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.DrawableElement;
import cool.kolya.implementation.scene.element.general.Parent;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;

import java.util.*;

public abstract class AbstractParent2D extends AbstractElement2D
        implements Parent<DrawableProperties2D> {

    protected final Map<UUID, DrawableElement<? extends DrawableProperties2D>> childrenMap =
            new HashMap<>();
    protected final Collection<DrawableElement<? extends DrawableProperties2D>> unmodifiableChildren =
            Collections.unmodifiableCollection(childrenMap.values());

    @Override
    public void updateAndRender(ElementMatrix parentMatrixTree) {
        update();
        getElementMatrix().update(parentMatrixTree);
        render();
        updateAndRenderChildren(getElementMatrix());
        getProperties().unmarkDirty();
    }

    @Override
    public Collection<DrawableElement<? extends DrawableProperties2D>> getChildren() {
        return unmodifiableChildren;
    }

    @Override
    public DrawableElement<? extends DrawableProperties2D> getChild(UUID id) {
        return childrenMap.get(id);
    }

    @Override
    public void addChild(DrawableElement<? extends DrawableProperties2D> child) {
        childrenMap.put(child.getId(), child);
    }

    @Override
    public void removeChild(UUID id) {
        childrenMap.remove(id);
    }
}
