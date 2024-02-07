package cool.kolya.engine.util;

import cool.kolya.engine.util.func.ThrowableConsumer;
import cool.kolya.engine.util.func.ThrowableFunction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceUtil {

    public static String readFile(String filePath) {
        try {
            return Files.readString(Path.of("",  filePath));
        } catch (IOException e) {
            throw new IllegalStateException("An error occurred on resource read", e);
        }
    }

    public static String readResource(String resourcePath) {
        return loadResourceAndApply(Path.of("", resourcePath), (is) -> new String(is.readAllBytes()));
    }

    public static void loadResourceAndCompute(Path resourcePath, ThrowableConsumer<InputStream> consumer) {
        loadResourceAndApply(resourcePath, consumer);
    }

    public static <O> O loadResourceAndApply(Path resourcePath, ThrowableFunction<InputStream, O> thFunc) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream is = cl.getResourceAsStream(resourcePath.toString())) {
            if (is == null) {
                throw new NullPointerException("resource with path " + resourcePath + "not exists");
            }
            return thFunc.apply(is);
        } catch (Throwable e) {
            throw new IllegalStateException("resource loading error", e);
        }
    }
}
