package cool.kolya.engine.event.bus;

import cool.kolya.engine.event.Event;
import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBusImpl implements EventBus {

    private final Map<Class<? extends Event>, List<Handler<? extends Event>>> eventHandlerMap = new ConcurrentHashMap<>();

    @Override
    public <T extends Event> void dispatch(T event) {
        List<Handler<? extends Event>> handlerQueue = eventHandlerMap.get(event.getClass());
        if (handlerQueue == null) {
            return;
        }
        for (Handler<? extends Event> handler : handlerQueue) {
            //noinspection RedundantCast, unchecked
            ((Handler<T>) handler).consumer.accept(event);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerListener(EventListener listener) {
        Class<?> lClass = listener.getClass();
        Method[] methods = lClass.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(EventHandler.class) || method.getParameterCount() != 1
                    || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                continue;
            }

            Class<? extends Event> eClass = (Class<? extends Event>) method.getParameterTypes()[0];
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            Handler<? extends Event> handler = createHandler(eClass, listener, annotation.priority(),
                    method);

            if (handler == null) {
                //an error occurred on current method handle
                return;
            }

            List<Handler<? extends Event>> handlerList = eventHandlerMap.computeIfAbsent(eClass, (k) -> new LinkedList<>());
            ListIterator<Handler<? extends Event>> li = handlerList.listIterator();
            while (true) {
                if (!li.hasNext()) {
                    li.add(handler);
                    break;
                }
                Handler<?> next = li.next();
                if (handler.compareTo(next) < 0) {
                    li.previous();
                    li.add(handler);
                    break;
                }
            }
        }
    }

    @Override
    public void unregisterListener(Class<? extends EventListener> listenerClass) {
        for (List<Handler<? extends Event>> handlerList : eventHandlerMap.values())
            handlerList.removeIf(eventHandler -> eventHandler.listenerClass().equals(listenerClass));
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> Handler<E> createHandler(Class<E> eClass, EventListener listener,
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
            return new Handler<>(lClass, priority, consumer);
        } catch (Throwable e) {
            throw new IllegalStateException("An error occurred on event handle creation");
        }
    }

    private record Handler<T extends Event>(Class<? extends EventListener> listenerClass,
                                            int priority, Consumer<T> consumer)
            implements Comparable<Handler<?>> {

        @Override
        public int compareTo(Handler<?> o) {
            return Integer.compare(priority, o.priority);
        }
    }
}
