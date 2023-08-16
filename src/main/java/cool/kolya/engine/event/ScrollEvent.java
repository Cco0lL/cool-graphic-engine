package cool.kolya.engine.event;

public record ScrollEvent(double xOffset, double yOffset) implements Event {}
