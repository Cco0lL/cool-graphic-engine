package cool.kolya.implementation.scene.element;

import cool.kolya.engine.event.ClickEvent;
import cool.kolya.engine.event.ScrollEvent;
import cool.kolya.implementation.scene.element.callback.Callback;

public interface InteractingModule {

    void handleClickEvent(ClickEvent event);

    void handleScrollEvent(ScrollEvent event);

    void updateMouseState();

    void addCallback(Callback.Type type, Runnable callbackRunnable);

    class Empty implements InteractingModule {

        @Override
        public void handleClickEvent(ClickEvent event) {}

        @Override
        public void handleScrollEvent(ScrollEvent event) {}

        @Override
        public void updateMouseState() {}

        @Override
        public void addCallback(Callback.Type type, Runnable callbackRunnable) {}
    }
}
