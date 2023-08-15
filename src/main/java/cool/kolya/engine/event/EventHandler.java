package cool.kolya.engine.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Represents an annotation which marks a method as handler of specified event
 * <p>Note that this annotation works only in class which inherited from {@link EventListener}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    /**
     * Priority of this event handler, the smaller value means the strongest
     * (will handle before others) priority
     */
    int priority() default 10;
}
