package cool.kolya.implementation.scheduler;

public class ScheduledTask {

    private int id = -1;
    protected final Runnable runnable;
    protected int delayLeft;
    protected boolean needStop;

    public ScheduledTask(Runnable runnable, int delay) {
        this.runnable = runnable;
        this.delayLeft = delay;
    }

    public void start() {
        TaskScheduler scheduler = TaskScheduler.getContext();
        if (scheduler.isRunning(id)) {
            scheduler.removeTask(this);
        }
        scheduler.addTask(this);
    }

    protected void tickDelay() {
        delayLeft--;
    }

    protected boolean isDelayZero() {
        return delayLeft == 0;
    }

    public void run() {
        runnable.run();
        applyAfter();
    }

    public void stop() {
        TaskScheduler.getContext().removeTask(this);
    }

    public int getId() {
        return id;
    }

    protected void applyAfter() {
        stop();
    }

    protected boolean isNeedStop() {
        return needStop;
    }

    void setId(int id) {
        this.id = id;
    }
}
