package cool.kolya.api.func;

@FunctionalInterface
public interface ThrowableConsumer<T> extends ThrowableFunction<T, Object>  {

    @Override
    default Object apply(T t) throws Throwable {
        accept(t);
        return null;
    }

    void accept(T t) throws Throwable;
}
