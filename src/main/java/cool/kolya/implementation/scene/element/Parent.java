package cool.kolya.implementation.scene.element;

import java.util.Collection;
import java.util.UUID;

/**
 * An interface that can contain other elements. Drawing of child elements
 * will follow its basis.
 */
public interface Parent extends Element {

    default void updateAndRenderChildren() {
        getChildren().removeIf(child -> {
            boolean needRemove = child.getParent() != this;
            if (!needRemove) {
                child.updateAndRender();
            }
            return needRemove;
        });
    }

    void updateAndRenderChild(Element child);

    void addChild(Element child);

    void removeChild(UUID id);

    Collection<Element> getChildren();

    default void addChildren(Element... children) {
        for (Element child : children) {
            addChild(child);
        }
    }
}
