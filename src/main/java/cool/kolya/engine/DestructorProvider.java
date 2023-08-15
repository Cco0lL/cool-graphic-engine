package cool.kolya.engine;

import java.lang.ref.Cleaner;

public class DestructorProvider {

    private static final Cleaner CLEANER = Cleaner.create();
    public static Cleaner.Cleanable createDestructor(Object object, Runnable destructRunnable) {
        return CLEANER.register(object, destructRunnable);
    }
}
