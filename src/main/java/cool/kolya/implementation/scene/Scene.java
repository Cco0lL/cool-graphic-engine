package cool.kolya.implementation.scene;

import cool.kolya.implementation.scene.element.general.ContextElement;
import cool.kolya.implementation.scene.element.general.DrawableProperties;

import java.util.Collection;
import java.util.UUID;

public interface Scene {

    static Scene getContext() {
        return ContextScene.getContext();
    }

    void updateAndRenderContexts();

    void addContext(ContextElement<?> contextElement);

    void removeContext(UUID id);

    <P extends DrawableProperties> ContextElement<P> getContext(UUID uuid, Class<P> cClass);

    Collection<ContextElement<? extends DrawableProperties>> getContexts();
}
