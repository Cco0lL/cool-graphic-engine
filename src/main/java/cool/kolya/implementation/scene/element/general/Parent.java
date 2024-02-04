package cool.kolya.implementation.scene.element.general;

import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;

import java.util.Collection;
import java.util.UUID;

public interface Parent<P extends DrawableProperties> {

    Collection<DrawableElement<? extends P>> getChildren();

    DrawableElement<? extends P> getChild(UUID id);


    default void updateAndRenderChildren(ElementMatrix parentMatrix) {
        for (DrawableElement<? extends P> child : getChildren()) {
            child.updateAndRender(parentMatrix);
        }
    }

    void addChild(DrawableElement<? extends P> child);

    default void addChildren(DrawableElement<? extends P>... children) {
        for (DrawableElement<? extends P> child : children) {
            addChild(child);
        }
    }

    void removeChild(UUID id);
}
