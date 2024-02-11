package cool.kolya.implementation.scene;

import cool.kolya.engine.event.ClickEvent;
import cool.kolya.engine.event.ScrollEvent;
import cool.kolya.implementation.scene.element.ContextElement;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ContextScene {

    private static final ThreadLocal<Scene> SCENE_THREAD_LOCAL = new ThreadLocal<>();

    public static Scene getContext() {
        return SCENE_THREAD_LOCAL.get();
    }

    public static void create() {
        SCENE_THREAD_LOCAL.set(new SceneImpl());
    }

    public static final class SceneImpl implements Scene {

        ContextElement currentContextElement;
        final Map<UUID, ContextElement> contexts = new ConcurrentHashMap<>();
        final Collection<ContextElement> unmodifiableContexts =
                Collections.unmodifiableCollection(contexts.values());
        final LastEventData<ClickEvent> clickEventLastEventData = new LastEventData<>();
        final LastEventData<ScrollEvent> scrollEventLastEventData = new LastEventData<>();

        @Override
        public ContextElement getCurrentContextElement() {
            return currentContextElement;
        }

        @Override
        public void setCurrentContextElement(ContextElement contextElement) {
            currentContextElement = contextElement;
        }

        @Override
        public void updateAndRenderContexts() {
            for (ContextElement context : contexts.values()) {
                setCurrentContextElement(context);
                context.updateAndRender();
                context.getWindowSettingsInterpreter().setDirty(false);
            }
        }

        @Override
        public void addContext(ContextElement context) {
            contexts.put(context.getId(), context);
            context.updateState();
        }

        @Override
        public void removeContext(UUID uuid) {
            contexts.remove(uuid);
        }

        @Override
        public ContextElement getContext(UUID uuid) {
            return contexts.get(uuid);
        }

        @Override
        public Collection<ContextElement> getContexts() {
            return unmodifiableContexts;
        }

        @Override
        public LastEventData<ClickEvent> getLastClickEventData() {
            return clickEventLastEventData;
        }

        @Override
        public LastEventData<ScrollEvent> getLastScrollEventData() {
            return scrollEventLastEventData;
        }
    }
}