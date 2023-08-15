package cool.kolya.engine.opengl.uniform;

public interface Uniform<V> {

    int location();

    String name();

    void set(V value);
}
