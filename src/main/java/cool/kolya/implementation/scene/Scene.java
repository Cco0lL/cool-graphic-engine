package cool.kolya.implementation.scene;

import cool.kolya.implementation.scene.element.ContextElement;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.UUID;

public interface Scene {

    static Scene getContext() {
        return ContextScene.getContext();
    }

    @ApiStatus.Internal
    ContextElement getCurrentContextElement();

    @ApiStatus.Internal
    void setCurrentContextElement(ContextElement contextElement);

    void updateAndRenderContexts();

    void addContext(ContextElement contextElement);

    void removeContext(UUID id);

    ContextElement getContext(UUID uuid);

    Collection<ContextElement> getContexts();
}
