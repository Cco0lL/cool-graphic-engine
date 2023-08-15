package cool.kolya.engine.event;

/**
 * Calls on window resize, handles only size of frame buffer
 * which means render context
 * @param width - frame buffer width
 * @param height - frame buffer height
 */
public record FrameBufferResizeEvent(int width, int height) implements Event {}
