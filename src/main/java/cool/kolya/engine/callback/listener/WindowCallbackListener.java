package cool.kolya.engine.callback.listener;

import org.jetbrains.annotations.Contract;
import org.lwjgl.system.CallbackI;

import java.util.function.Consumer;

public interface WindowCallbackListener {

    @Contract("_, _ -> this")
    <T extends CallbackI> WindowCallbackListener setCallbackHandler(Class<T> clazz, T handler);

    <T extends CallbackI> void listenCallback(Class<T> clazz, Consumer<T> invokeFunc);
}
