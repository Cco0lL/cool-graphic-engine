package cool.kolya.engine.event;

/**
 * <p> Represents an event listener marker. Inheritors of this interface could have event handlers.
 *
 * <p> Event handler means a method that annotated as {@link EventHandler}. You should provide
 * event type as parameter of this method.
 *
 * <pre>{@code
 * @EventHandler
 * public void handle(Event eventInheritor) {
 *     //handler logic here
 * }
 * }
 * </pre>
 *
 * <p> All event handlers will be initialized during the registration of the current listener.
 *
 * @see EventBus for listeners registration/unregistration
 */
public interface EventListener {}
