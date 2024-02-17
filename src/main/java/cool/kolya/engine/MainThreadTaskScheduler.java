package cool.kolya.engine;

import cool.kolya.api.util.ThreadScopeUtil;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpscUnboundedArrayQueue;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class MainThreadTaskScheduler {

    private final MessagePassingQueue<Runnable> taskQueue = new MpscUnboundedArrayQueue<>(32);

    public void addTask(Runnable task) {
        if (ThreadScopeUtil.isCurrentThreadMain()) {
            task.run();
        } else {
            offerInTaskQueue(task);
        }
    }

    public void offerInTaskQueue(Runnable task) {
        taskQueue.offer(task);
    }

    public <T> T blockingReturningRequest(Supplier<T> supplier) {
        if (ThreadScopeUtil.isCurrentThreadMain()) {
            return supplier.get();
        }
        return returningRequest(supplier).join();
    }

    public <T> CompletableFuture<T> returningRequest(Supplier<T> supplier) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        offerInTaskQueue(() -> cf.complete(supplier.get()));
        return cf;
    }

    void computeTasks() {
        taskQueue.drain(Runnable::run);
    }
}
