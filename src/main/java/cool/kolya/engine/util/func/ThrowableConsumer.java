package cool.kolya.engine.util.func;

@FunctionalInterface
public interface ThrowableConsumer<T> extends ThrowableFunction<T, Object>  {

    @Override
    default Object apply(T t) throws Throwable {
        accept(t);
        return null;
    }

    void accept(T t) throws Throwable;
}
