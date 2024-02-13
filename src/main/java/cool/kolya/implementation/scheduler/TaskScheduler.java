package cool.kolya.implementation.scheduler;

import cool.kolya.api.util.SearchBitSet;
import cool.kolya.engine.Engine;
import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;
import cool.kolya.engine.event.UpdateEvent;

import java.util.ArrayList;
import java.util.List;

public class TaskScheduler {

    public static final ThreadLocal<TaskScheduler> SCHEDULER_THREAD_LOCAL = new ThreadLocal<>();
    private final List<ScheduledTask> tasks = new ArrayList<>(1 << 8);
    public final SearchBitSet acquiredIdBitSet = new SearchBitSet(1 << 8);

    public TaskScheduler() {
        Engine.getContextProcess().getEventBus()
                .registerListener(new EventListener() {
                    @EventHandler
                    void update(UpdateEvent e) {
                        updateTasks();
                    }
                });
    }

    public static void createContext() {
        if (SCHEDULER_THREAD_LOCAL.get() == null) {
            SCHEDULER_THREAD_LOCAL.set(new TaskScheduler());
        }
    }

    public static TaskScheduler getContext() {
        return SCHEDULER_THREAD_LOCAL.get();
    }

    public void updateTasks() {
        tasks.removeIf(task -> {
            boolean removal = task.isNeedStop();
            if (!removal) {
                if (task.isDelayZero()) {
                    task.run();
                } else {
                    task.tickDelay();
                }
            } else {
                acquiredIdBitSet.clearIndex(task.getId());
            }
            return removal;
        });
    }

    public int addTaskWithDelay(int delay, Runnable taskRunnable) {
        ScheduledTask task = new ScheduledTask(taskRunnable, delay);
        return addTask(task);
    }

    public int addTaskOnNextTick(Runnable runnable) {
        return addTaskWithDelay(0, runnable);
    }

    public int addRepeatableTask(int delay, int cooldown, Runnable runnable) {
        RepeatableScheduledTask task = new RepeatableScheduledTask(runnable, delay, cooldown);
        return addTask(task);
    }

    public boolean isRunning(int taskId) {
        if (!isIdValid(taskId)) {
            return false;
        }
        return tasks.get(taskId) != null;
    }

    public void stopTask(int taskId) {
        assertTaskValid(taskId);
        tasks.get(taskId).stop();
    }

    public int addTask(ScheduledTask task) {
        int taskId = acquiredIdBitSet.firstClear();
        task.setId(taskId);
        tasks.add(task);
        acquiredIdBitSet.set(taskId);
        return taskId;
    }

    protected synchronized void removeTask(ScheduledTask task) {
        int taskId = task.getId();
        tasks.remove(taskId);
        acquiredIdBitSet.clearIndex(taskId);
    }

    private boolean isIdValid(int id) {
        return id >= 0 && id < tasks.size();
    }

    private void assertTaskValid(int id) {
        if (!isIdValid(id)) {
            throw new IllegalStateException("id " + id + " is not valid");
        }
    }
}
