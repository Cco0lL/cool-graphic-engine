package cool.kolya.engine.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class EventBusImpl implements EventBus {

    private static final Logger log = LoggerFactory.getLogger(EventBusImpl.class);

    protected static EventBus instance;

    private final Map<Class<? extends Event>, Queue<Handle<? extends Event>>> eventHandlerMap = new HashMap<>();

    @Override
    public <T extends Event> void dispatch(T event) {
        Queue<Handle<? extends Event>> handlerQueue = eventHandlerMap.get(event.getClass());
        if (handlerQueue == null) {
            return;
        }
        for (Handle<? extends Event> handler : handlerQueue) {
            //noinspection RedundantCast, unchecked
            ((Handle<T>) handler).consumer.accept(event);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerListener(EventListener listener) {
        synchronized (eventHandlerMap) {
            Class<?> lClass = listener.getClass();
            Method[] methods = lClass.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(EventHandler.class) || method.getParameterCount() != 1
                        || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    continue;
                }

                Class<? extends Event> eClass = (Class<? extends Event>) method.getParameterTypes()[0];
                EventHandler annotation = method.getAnnotation(EventHandler.class);
                Handle<? extends Event> handle = createHandle(eClass, listener, annotation.priority(),
                        method);

                if (handle == null) {
                    //an error occurred on current method handle
                    return;
                }

                eventHandlerMap.computeIfAbsent(eClass, (k) -> new PriorityQueue<>()).offer(handle);
            }
        }
    }

    @Override
    public void unregisterListener(Class<? extends EventListener> listenerClass) {
        synchronized (eventHandlerMap) {
            for (Queue<Handle<? extends Event>> handlerQueue : eventHandlerMap.values())
                handlerQueue.removeIf(eventHandler -> eventHandler.listenerClass().equals(listenerClass));
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> Handle<E> createHandle(Class<E> eClass, EventListener listener,
                                                    int priority, Method method) {
        Class<? extends EventListener> lClass = listener.getClass();
        try {
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(lClass, MethodHandles.lookup());
            MethodHandle handle = lookup.unreflect(method);
            CallSite callSite = LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    MethodType.methodType(Consumer.class, lClass),
                    MethodType.methodType(void.class, Object.class),
                    handle,
                    MethodType.methodType(void.class, eClass)
            );
            Consumer<E> consumer = (Consumer<E>) callSite.getTarget().invoke(listener);
            return new Handle<>(lClass, priority, consumer);
        } catch (Throwable e) {
            //TODO log
            e.printStackTrace();
            log.error("an error occurred on event handle creation", e);
        }
        return null;
    }

    private record Handle<T extends Event>(Class<? extends EventListener> listenerClass,
                                           int priority, Consumer<T> consumer)
            implements Comparable<Handle<T>> {

        @Override
        public int compareTo(Handle<T> o) {
            return Integer.compare(priority, o.priority);
        }

        public void handle(T event) {
            consumer.accept(event);
        }
    }
}
