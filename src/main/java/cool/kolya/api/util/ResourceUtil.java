package cool.kolya.api.util;

import cool.kolya.api.func.ThrowableConsumer;
import cool.kolya.api.func.ThrowableFunction;
import cool.kolya.api.resource.DataSource;

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

    public static String readResource(DataSource dataSource) {
        return loadResourceAndApply(dataSource, (is) -> new String(is.readAllBytes()));
    }

    public static void loadResourceAndCompute(DataSource dataSource, ThrowableConsumer<InputStream> consumer) {
        loadResourceAndApply(dataSource, consumer);
    }

    public static <O> O loadResourceAndApply(DataSource dataSource, ThrowableFunction<InputStream, O> thFunc) {
        try (InputStream is = dataSource.getInputStream()) {
            return thFunc.apply(is);
        } catch (Throwable th) {
            throw new IllegalStateException("resource loading error", th);
        }
    }
}
