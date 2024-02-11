package cool.kolya.implementation.scene;

import cool.kolya.engine.event.ClickEvent;
import cool.kolya.engine.event.Event;
import cool.kolya.engine.event.ScrollEvent;
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

    LastEventData<ClickEvent> getLastClickEventData();

    LastEventData<ScrollEvent> getLastScrollEventData();

    class LastEventData<T extends Event> {

        private T lastEvent;
        private boolean outdated = true;

        public boolean isOutdated() {
            return outdated;
        }

        public T getLastEvent() {
            return lastEvent;
        }

        void setOutdated(boolean outdated) {
            this.outdated = outdated;
        }

        void setLastEvent(T lastEvent) {
            this.lastEvent = lastEvent;
            setOutdated(false);
        }
    }
}
