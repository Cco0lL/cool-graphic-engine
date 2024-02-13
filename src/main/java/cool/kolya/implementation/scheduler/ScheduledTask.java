package cool.kolya.implementation.scheduler;

public class ScheduledTask extends AbstractScheduledTask {

    protected Runnable runnable;

    public ScheduledTask(Runnable runnable, int delay) {
        super(delay);
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();
        applyAfter();
    }

    protected void applyAfter() {
        stop();
    }
}
