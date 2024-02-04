package cool.kolya.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceUtil {

    public static String readResource(String resourceFile) throws IOException, NullPointerException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream is = cl.getResourceAsStream(resourceFile)) {
            if (is == null) {
                throw new NullPointerException("resource file not exists");
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
