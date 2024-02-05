package cool.kolya.engine.opengl.uniform;

public abstract class AbstractUniform<V> implements Uniform<V> {

    protected V value;
    protected final String name;
    protected final int location;

    protected AbstractUniform(String name, int location) {
        this.name = name;
        this.location = location;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int location() {
        return location;
    }

    @Override
    public abstract void set(V value);

    @Override
    public V get() {
        return value;
    }
}
