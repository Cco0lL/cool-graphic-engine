package cool.kolya.implementation.scene.element.callback;

public class Callback {

    private Runnable callbackRunnable = () -> {};

    public void add(Runnable callbackRunnable) {
        Runnable prev = this.callbackRunnable;
        this.callbackRunnable = () -> {
            prev.run();
            callbackRunnable.run();
        };
    }

    public void run() {
        callbackRunnable.run();
    }

    public enum Type {
        HOVER;
    }
}
