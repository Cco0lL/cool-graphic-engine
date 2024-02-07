package cool.kolya.api.util;

public class ThreadScopeUtil {

    private static long MAIN_THREAD_ID = -1;

    public static synchronized void defineMainThread() {
        if (MAIN_THREAD_ID != -1) {
            throw new IllegalStateException("Main thread is already defined");
        }
        MAIN_THREAD_ID = Thread.currentThread().threadId();
    }

    public static boolean isCurrentThreadMain() {
        return Thread.currentThread().threadId() == MAIN_THREAD_ID;
    }

    public static void assertMainThread() {
        if (!isCurrentThreadMain()) {
            throw new IllegalStateException("function must be called from main thread");
        }
    }
}
