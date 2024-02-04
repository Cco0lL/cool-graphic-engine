package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.DrawableElement;
import cool.kolya.implementation.scene.element.general.Parent;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;

import java.util.*;

public abstract class AbstractParent2D extends AbstractElement2D
        implements Parent<Drawable2DProperties> {

    protected final Map<UUID, DrawableElement<? extends Drawable2DProperties>> childrenMap =
            new HashMap<>();
    protected final Collection<DrawableElement<? extends Drawable2DProperties>> unmodifiableChildren =
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
    public Collection<DrawableElement<? extends Drawable2DProperties>> getChildren() {
        return unmodifiableChildren;
    }

    @Override
    public DrawableElement<? extends Drawable2DProperties> getChild(UUID id) {
        return childrenMap.get(id);
    }

    @Override
    public void addChild(DrawableElement<? extends Drawable2DProperties> child) {
        childrenMap.put(child.getId(), child);
    }

    @Override
    public void removeChild(UUID id) {
        childrenMap.remove(id);
    }
}
