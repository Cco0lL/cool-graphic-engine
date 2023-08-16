package cool.kolya.engine;

import java.lang.ref.Cleaner;

public class DestructorProvider {

    private final Cleaner cleaner = Cleaner.create();

    DestructorProvider() {}

    public Cleaner.Cleanable createDestructor(Object object, Runnable destructRunnable) {
        return cleaner.register(object, destructRunnable);
    }
}
