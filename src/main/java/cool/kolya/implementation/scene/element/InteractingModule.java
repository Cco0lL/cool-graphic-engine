package cool.kolya.implementation.scene.element;

import cool.kolya.implementation.scene.element.callback.Callback;

public interface InteractingModule {

    void updateInteractions();

    void addCallback(Callback.InteractType type, Runnable callbackRunnable);

    class Empty implements InteractingModule {

        @Override
        public void updateInteractions() {}

        @Override
        public void addCallback(Callback.InteractType type, Runnable callbackRunnable) {}
    }
}
