package cool.kolya.implementation.animation;

import cool.kolya.engine.Engine;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.implementation.scheduler.AbstractScheduledTask;

public class AnimationTask extends AbstractScheduledTask {

    private final EngineProcess process = Engine.getContextProcess();
    private final Animation animation;

    public AnimationTask(Animation animation) {
        super(0);
        this.animation = animation;
    }

    @Override
    protected void tickDelay() {}

    @Override
    public void run() {
        animation.update(process.currentTick());
    }

    @Override
    protected boolean isNeedStop() {
        return animation.isDone();
    }
}
