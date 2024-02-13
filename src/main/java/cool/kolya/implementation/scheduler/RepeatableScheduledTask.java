package cool.kolya.implementation.scheduler;

public class RepeatableScheduledTask extends ScheduledTask {

    protected final int cooldown;

    public RepeatableScheduledTask(Runnable runnable, int delay, int cooldown) {
        super(runnable, delay);
        this.cooldown = cooldown;
    }

    @Override
    public void applyAfter() {
        delayLeft = cooldown;
    }
}
