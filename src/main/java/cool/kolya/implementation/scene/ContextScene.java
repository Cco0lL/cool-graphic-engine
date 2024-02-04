package cool.kolya.implementation.scene;

import cool.kolya.implementation.scene.element.general.ContextElement;
import cool.kolya.implementation.scene.element.general.DrawableProperties;

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

        final Map<UUID, ContextElement<?>> contexts = new ConcurrentHashMap<>();
        final Collection<ContextElement<?>> unmodifiableContexts =
                Collections.unmodifiableCollection(contexts.values());

        @Override
        public void updateAndRenderContexts() {
            for (ContextElement<?> context : contexts.values()) {
                context.updateAndRender();
            }
        }

        @Override
        public void addContext(ContextElement<?> context) {
            context.getMatrix().update();
            contexts.put(context.getId(), context);
        }

        @Override
        public void removeContext(UUID uuid) {
            contexts.remove(uuid);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <P extends DrawableProperties> ContextElement<P> getContext(UUID uuid, Class<P> cClass) {
            return (ContextElement<P>) contexts.get(uuid);
        }

        @Override
        public Collection<ContextElement<?>> getContexts() {
            return unmodifiableContexts;
        }
    }
}