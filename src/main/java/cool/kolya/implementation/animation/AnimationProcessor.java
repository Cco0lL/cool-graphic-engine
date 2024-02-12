package cool.kolya.implementation.animation;

import cool.kolya.engine.Engine;
import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;
import cool.kolya.engine.event.UpdateEvent;

import java.util.ArrayList;
import java.util.List;

public class AnimationProcessor {

    private static final ThreadLocal<AnimationProcessor> ANIMATION_PROCESSOR_THREAD_LOCAL = new ThreadLocal<>();

    private final List<Animation> animations = new ArrayList<>();

    public AnimationProcessor() {
        Engine.getContextProcess().getEventBus().registerListener(new EventListener() {
            @EventHandler
            void update(UpdateEvent updateEvent) {
                animations.removeIf(animation -> {
                    animation.update(updateEvent.currentTick());
                    return animation.isDone();
                });
            }
        });
    }

    public static void createContext() {
        if (ANIMATION_PROCESSOR_THREAD_LOCAL.get() == null) {
            ANIMATION_PROCESSOR_THREAD_LOCAL.set(new AnimationProcessor());
        }
    }

    public static AnimationProcessor getContext() {
        return ANIMATION_PROCESSOR_THREAD_LOCAL.get();
    }

    public void processAnimation(Animation animation) {
        animations.add(animation);
    }
}
