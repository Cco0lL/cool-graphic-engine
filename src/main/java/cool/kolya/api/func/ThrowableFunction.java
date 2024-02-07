package cool.kolya.api.func;

@FunctionalInterface
public interface ThrowableFunction<I, O> {

    O apply(I i) throws Throwable;
}
