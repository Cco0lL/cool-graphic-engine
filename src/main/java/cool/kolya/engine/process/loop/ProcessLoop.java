package cool.kolya.engine.process.loop;

public interface ProcessLoop {

    void run();

    void pollInputEvents();

    void frame();

    boolean keepRunning();

    boolean isRunning();
}

