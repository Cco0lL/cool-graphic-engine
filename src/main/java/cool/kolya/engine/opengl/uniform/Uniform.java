package cool.kolya.engine.opengl.uniform;

import org.jetbrains.annotations.Nullable;

public interface Uniform<V> {

    int location();

    String name();

    void set(V value);

    @Nullable V get();
}
