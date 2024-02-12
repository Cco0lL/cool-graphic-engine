package cool.kolya.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Assertion {
    
    public static void assertNotEqual(@NotNull Object test,  Object compared, String message) {
        Objects.requireNonNull(test, "testing object is null");
        if (test.equals(compared)) {
            throw new IllegalStateException(message);
        }
    }
}
