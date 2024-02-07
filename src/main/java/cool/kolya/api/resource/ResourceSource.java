package cool.kolya.api.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ResourceSource extends AbstractDataSource {

    public ResourceSource(String path, String name) {
        super(path, name);
    }

    public ResourceSource(File file) {
        super(file);
    }

    public ResourceSource(Path path) {
        super(path);
    }

    public ResourceSource(String stringPath) {
        super(stringPath);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(stringPath);
        if (is == null) {
            throw new NullPointerException("resource with path " + stringPath + "not exists");
        }
        return is;
    }
}
