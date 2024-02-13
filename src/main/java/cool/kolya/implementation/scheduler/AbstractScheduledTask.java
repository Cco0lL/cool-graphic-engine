package cool.kolya.implementation.scheduler;

public abstract class AbstractScheduledTask {

    private int id = -1;
    protected Runnable runnable;
    protected int delayLeft;
    protected boolean needStop;

    public AbstractScheduledTask(int delay) {
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

    public abstract void run();

    public void stop() {
        TaskScheduler.getContext().removeTask(this);
    }

    public int getId() {
        return id;
    }

    protected boolean isNeedStop() {
        return needStop;
    }

    void setId(int id) {
        this.id = id;
    }
}
