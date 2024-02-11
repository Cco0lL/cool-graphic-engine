package cool.kolya.implementation.scene.element.callback;

import java.util.EnumMap;
import java.util.Map;

public class Callbacks<T extends Enum<T>> {

    public static final Callback NOOP = new Callback();

    protected final Map<T, Callback> callbackMap;

    public Callbacks(Class<T> callbackTypeEnum) {
        callbackMap = new EnumMap<>(callbackTypeEnum);
    }

    public void runCallback(T type) {
        getCallback(type).run();
    }

    public void addCallback(T type, Runnable callbackRunnable) {
        callbackMap.computeIfAbsent(type, k -> new Callback()).add(callbackRunnable);
    }

    public Callback getCallback(T type) {
        return callbackMap.getOrDefault(type, NOOP);
    }
}
