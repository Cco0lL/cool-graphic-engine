package cool.kolya.implementation.scene.element.context2d;

public class ElementCallback {

    private Runnable callbackRunnable = () -> {};

    public void addCallback(Runnable callbackRunnable) {
        Runnable prev = this.callbackRunnable;
        this.callbackRunnable = () -> {
            prev.run();
            callbackRunnable.run();
        };
    }

    public void call() {
        callbackRunnable.run();
    }

    public enum Type {
        HOVER,
        CLICK
    }
}
