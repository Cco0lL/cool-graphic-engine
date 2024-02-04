package cool.kolya.implementation.scene.element.general;

import cool.kolya.implementation.scene.element.general.matrix.AbstractElementMatrix;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;

import java.util.*;

public class ContextElement<P extends DrawableProperties> extends AbstractElement implements Parent<P> {

    protected final Map<UUID, DrawableElement<? extends P>> childrenMap = new HashMap<>();
    protected final Collection<DrawableElement<? extends P>> unmodifiableChildren =
            Collections.unmodifiableCollection(childrenMap.values());
    protected AbstractElementMatrix.Context matrix;

    public void updateAndRender() {
        updateAndRenderChildren(matrix);
        matrix.setDirty(false);
    }

    @Override
    public Collection<DrawableElement<? extends P>> getChildren() {
        return unmodifiableChildren;
    }

    @Override
    public DrawableElement<? extends P> getChild(UUID id) {
        return childrenMap.get(id);
    }

    @Override
    public void addChild(DrawableElement<? extends P> child) {
        childrenMap.put(child.getId(), child);
    }

    @Override
    public void removeChild(UUID id) {
        childrenMap.remove(id);
    }

    public ElementMatrix.Context getMatrix() {
        return matrix;
    }
}
