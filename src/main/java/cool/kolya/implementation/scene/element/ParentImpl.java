package cool.kolya.implementation.scene.element;

import cool.kolya.implementation.scene.element.property.Properties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParentImpl extends ElementImpl implements Parent {

    private final Map<UUID, Element> childrenMap = new HashMap<>();
    private final Collection<Element> children = childrenMap.values();

    public ParentImpl() {
        properties.getSize().getChangeCallback().add(() -> {
            for (Element child : getChildren()) {
                child.getProperties().setPropertyDirty(Properties.ALIGN, true);
            }
        });
    }

    @Override
    public void render() {
        super.render();
        updateAndRenderChildren();
    }

    @Override
    public void updateAndRenderChild(Element child) {
        child.updateAndRender();
    }

    @Override
    public void addChild(Element child) {
        childrenMap.put(child.getId(), child);
        child.setParent(this);
    }

    @Override
    public void removeChild(UUID id) {
        Element child = childrenMap.remove(id);
        if (child != null) {
            child.setParent(null);
        }
    }

    @Override
    public Collection<Element> getChildren() {
        return children;
    }
}
