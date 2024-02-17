package cool.kolya.engine.process;

import cool.kolya.engine.Engine;
import cool.kolya.engine.event.*;
import cool.kolya.engine.callback.Callbacks;
import cool.kolya.engine.callback.listener.WindowCallbackListener;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.SpscUnboundedArrayQueue;
import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class ContextInputEventsManager {

    private static final Map<Long, SpscUnboundedArrayQueue<Event>> EVENT_QUEUES_MAP = new ConcurrentHashMap<>();

    static void createProcessQueue(long ptr) {
        EVENT_QUEUES_MAP.put(ptr, new SpscUnboundedArrayQueue<>(16));
    }

    static void routeEventToContextQueue(long ptr, Event event) {
        EVENT_QUEUES_MAP.get(ptr).offer(event);
    }

    static void removeProcessQueue(long ptr) {
        EVENT_QUEUES_MAP.remove(ptr);
    }

    /**
     * In public access for possibility to implement another process loop.
     *
     * @return queue with polled input events of current context process for
     * further computation in its own scope
     */
    @ApiStatus.Internal
    public static Queue<Event> pollRoutedContextEvents() {
        Queue<Event> pollEventsQueue = new LinkedList<>();
        MessagePassingQueue<Event> eventsQueue = EVENT_QUEUES_MAP.get(Engine.getContextProcess().getWindowPtr());
        eventsQueue.drain(pollEventsQueue::offer);
        return pollEventsQueue;
    }

    static void linkCallbacksWithProcessQueue(WindowCallbackListener windowCallbackListener) {
        windowCallbackListener
                .setCallbackHandler(Callbacks.MouseButton, (ptr, button, action, mods) ->
                        routeEventToContextQueue(ptr, new ClickEvent(button, action, mods)))
                .setCallbackHandler(Callbacks.Key, (ptr, key, scancode, action, mods) ->
                        routeEventToContextQueue(ptr, new KeyTypeEvent(key, action, mods)))
                .setCallbackHandler(Callbacks.CursorEnter, (ptr, entered) ->
                        routeEventToContextQueue(ptr, new CursorEnterEvent(entered)))
                .setCallbackHandler(Callbacks.Key, (ptr, key, scancode, action, mods) ->
                        routeEventToContextQueue(ptr, new KeyTypeEvent(key, action, mods)))
                .setCallbackHandler(Callbacks.FramebufferSize, ((ptr, width, height) ->
                        routeEventToContextQueue(ptr, new FrameBufferResizeEvent(width, height))))
                .setCallbackHandler(Callbacks.WindowSize, ((ptr, width, height) ->
                        routeEventToContextQueue(ptr, new WindowResizeEvent(width, height))))
                .setCallbackHandler(Callbacks.CursorPos, ((ptr, xpos, ypos) ->
                        routeEventToContextQueue(ptr, new CursorMoveEvent(xpos, ypos)))).
                setCallbackHandler(Callbacks.Scroll, ((ptr, xoffset, yoffset) ->
                        routeEventToContextQueue(ptr, (new ScrollEvent(xoffset, yoffset)))));
    }
}
