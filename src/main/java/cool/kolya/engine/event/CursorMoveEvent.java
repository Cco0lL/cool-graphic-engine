package cool.kolya.engine.event;

/**
 * Calls on cursor move, handles new cursor coordinates
 * @param xPos - new cursor x coordinate
 * @param yPos - new cursor y coordinate
 */
public record CursorMoveEvent(double xPos, double yPos) implements Event {}
