package cool.kolya.api.resource;

import java.io.File;
import java.nio.file.Path;

public abstract class AbstractDataSource implements DataSource {

    protected final String name;
    protected final Path path;
    protected final String stringPath;

    public AbstractDataSource(String path, String name) {
        this.name = name;
        this.path = Path.of(path, name);
        this.stringPath = this.path.toString();
    }

    public AbstractDataSource(File file) {
        this(file.toPath());
    }

    public AbstractDataSource(Path path) {
        this.path = path;
        name = path.getFileName().toString();
        stringPath = path.toString();
    }

    public AbstractDataSource(String stringPath) {
        this.stringPath = stringPath;
        path = Path.of("", stringPath);
        name = path.getFileName().toString();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Path path() {
        return path;
    }

    @Override
    public String stringPath() {
        return stringPath;
    }
}
