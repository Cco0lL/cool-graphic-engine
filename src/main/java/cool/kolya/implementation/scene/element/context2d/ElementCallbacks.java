package cool.kolya.implementation.scene.element.context2d;

import java.util.EnumMap;
import java.util.Map;

public class ElementCallbacks {

    private final Map<ElementCallback.Type, ElementCallback> callbackMap =
            new EnumMap<>(ElementCallback.Type.class);

    public ElementCallbacks() {
        for (ElementCallback.Type type : ElementCallback.Type.values()) {
            callbackMap.put(type, new ElementCallback());
        }
    }

    public ElementCallback getCallback(ElementCallback.Type type) {
        return callbackMap.get(type);
    }
}
