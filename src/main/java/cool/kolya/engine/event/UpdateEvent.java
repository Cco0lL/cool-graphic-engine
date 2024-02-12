package cool.kolya.engine.event;

/**
 * Represents an update event that calls before scene updates
 */
public record UpdateEvent(int currentTick) implements Event { }
