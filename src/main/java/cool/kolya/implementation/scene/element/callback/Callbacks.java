package cool.kolya.implementation.scene.element.callback;

import java.util.EnumMap;
import java.util.Map;

public class Callbacks {

    public static final Callback NOOP = new Callback();

    protected final Map<Callback.InteractType, Callback> callbackMap = new EnumMap<>(Callback.InteractType.class);

    public void addCallback(Callback.InteractType type, Runnable callbackRunnable) {
        callbackMap.computeIfAbsent(type, k -> new Callback()).add(callbackRunnable);
    }

    public Callback getCallback(Callback.InteractType type) {
        return callbackMap.getOrDefault(type, NOOP);
    }
}
