package cool.kolya.api.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface DataSource {

    String name();

    Path path();

    String stringPath();

    InputStream getInputStream() throws IOException;
}
