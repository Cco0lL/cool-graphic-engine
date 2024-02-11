package cool.kolya.implementation.scene;

import cool.kolya.engine.event.*;
import cool.kolya.implementation.scene.element.ContextElement;

public class SceneListener implements EventListener {

    private final Scene scene;

    public SceneListener(Scene scene) {
        this.scene = scene;
    }

    @EventHandler
    void handleWindowResize(WindowResizeEvent event) {
        for (ContextElement context : Scene.getContext().getContexts()) {
            context.updateState();
        }
    }

    @EventHandler
    void handleClickEvent(ClickEvent event) {
        scene.getLastClickEventData().setLastEvent(event);
    }

    @EventHandler
    void handleScrollEvent(ScrollEvent event) {
        scene.getLastScrollEventData().setLastEvent(event);
    }

    @EventHandler
    void handleRenderEvent(RenderEvent event) {
        scene.updateAndRenderContexts();
        scene.getLastScrollEventData().setOutdated(true);
        scene.getLastClickEventData().setOutdated(true);
    }
}
