package cool.kolya.implementation.animation;

import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.property.TransformProperties;
import cool.kolya.implementation.scene.element.property.TransformPropertiesImpl;
import cool.kolya.implementation.scheduler.TaskScheduler;

import java.util.function.Consumer;

public interface Animation {

    static int animate(Element element, Easing easing, int duration,
                        ChangeType type, Consumer<TransformProperties> animationChanges) {
        TransformProperties properties = new TransformPropertiesImpl();
        animationChanges.accept(properties);
        Animation animation = new AnimationImpl(element, easing, duration, type, properties);
        return TaskScheduler.getContext().addTask(new AnimationTask(animation));
    }

    void update(int currentTick);

    boolean isDone();

    enum ChangeType {
        SET,
        ADD
    }
}