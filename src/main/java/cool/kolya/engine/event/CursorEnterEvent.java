package cool.kolya.engine.event;

/**
 * <p> Represents a cursor enter event
 * @param isEntered - true if cursor entered, otherwise false
 */
public record CursorEnterEvent(boolean isEntered) implements Event {}
