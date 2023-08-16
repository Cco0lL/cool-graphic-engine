package cool.kolya.engine;

import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class WindowCallbackListenerImpl implements WindowCallbackListener {

    WindowCallbackListenerImpl() {}

    private final Map<Class<? extends CallbackI>, CallbackI> callbackHandlersMap = new HashMap<>();

    @Override
    public <T extends CallbackI> WindowCallbackListener setCallbackHandler(Class<T> clazz, T handler) {
        callbackHandlersMap.put(clazz, handler);
        return this;
    }

    @Override
    public <T extends CallbackI> void listenCallback(Class<T> clazz, Consumer<T> invokeFunc) {
        CallbackI callbackHandler = callbackHandlersMap.get(clazz);
        if (callbackHandler == null)
            return;
        //noinspection unchecked
        invokeFunc.accept((T) callbackHandler);
    }
}
