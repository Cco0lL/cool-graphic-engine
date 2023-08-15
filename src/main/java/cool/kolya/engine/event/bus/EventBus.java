package cool.kolya.engine.event.bus;

import cool.kolya.engine.event.Event;
import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;

/**
 * Dispatches events to registered handlers. Each type of event has a priority queue of handlers. Handlers
 * represents as a class with registration class of event listener, priority and consumer. When you
 * register an event listeners, methods, that annotated as {@link EventHandler} and provides event type as
 * parameter, converts to a handles and dispatches to a specified queue.
 */
public interface EventBus {

    static EventBus getInstance() {
        if (EventBusImpl.instance == null) {
            EventBusImpl.instance = new EventBusImpl();
        }
        return EventBusImpl.instance;
    }

    /**
     * Registers event listener
     * @param listener - instance of event listener to be registered
     * @param <T> - listener type
     */
    <T extends EventListener> void registerListener(T listener);

    /**
     * Unregisters event listener
     * @param lClass - class of type of event listener.
     */
    void unregisterListener(Class<? extends EventListener> lClass);

    /**
     * dispatches event to registered handlers
     * @param event - event that should be handled
     * @param <T> - event type
     */
    <T extends Event> void dispatch(T event);
}
