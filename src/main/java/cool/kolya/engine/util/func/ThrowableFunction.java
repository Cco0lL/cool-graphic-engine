package cool.kolya.engine.util.func;

@FunctionalInterface
public interface ThrowableFunction<I, O> {

    O apply(I i) throws Throwable;
}
