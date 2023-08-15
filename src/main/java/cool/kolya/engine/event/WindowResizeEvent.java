package cool.kolya.engine.event;

/**
 * Calls on window resize, it handles full window size (with frame)
 * @param width - window size
 * @param height - window height
 */
public record WindowResizeEvent(int width, int height) implements Event {}
