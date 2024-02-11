package cool.kolya.implementation.scene.element.callback;

import java.util.EnumMap;
import java.util.Map;

public class Callbacks {

    public static final Callback NOOP = new Callback();

    protected final Map<Callback.Type, Callback> callbackMap = new EnumMap<>(Callback.Type.class);

    public void addCallback(Callback.Type type, Runnable callbackRunnable) {
        callbackMap.computeIfAbsent(type, k -> new Callback()).add(callbackRunnable);
    }

    public Callback getCallback(Callback.Type type) {
        return callbackMap.getOrDefault(type, NOOP);
    }
}
