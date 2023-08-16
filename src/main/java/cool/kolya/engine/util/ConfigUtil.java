package cool.kolya.engine.util;

import com.typesafe.config.ConfigException;

import java.util.function.Consumer;

public class ConfigUtil {

    public static void readSafe(Runnable runnable, Consumer<ConfigException> onFailure) {
        try {
          runnable.run();
        } catch (ConfigException exception) {
            onFailure.accept(exception);
        }
    }
}
