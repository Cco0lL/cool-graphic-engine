package cool.kolya.implementation;

import cool.kolya.engine.Engine;
import cool.kolya.engine.data.CursorPosition;
import cool.kolya.engine.data.FrameBufferSize;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.event.*;
import cool.kolya.engine.event.bus.EventBus;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.implementation.component.ComponentsUpdateListener;
import cool.kolya.implementation.component.Display;
import cool.kolya.implementation.component.Mouse;
import cool.kolya.implementation.graphic.context2d.Graphic2D;
import cool.kolya.implementation.graphic.element2d.ElementGraphic2D;
import cool.kolya.implementation.scene.ContextScene;
import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.SceneListener;
import cool.kolya.implementation.scene.element.ContextElement;
import org.lwjgl.opengl.GL33;

public class ProcessRunner implements EventListener {

    private final EngineProcess process;
    private final Runnable runnable;

    public ProcessRunner(EngineProcess process, Runnable runnable) {
        this.process = process;
        this.runnable = runnable;
    }

    public void run() {
        new Thread(() -> {
            process.getEventBus().registerListener(this);
            process.start();
        }).start();
        if (!Engine.isListingInputs()) {
            Engine.listenInputEventsAndActions();
        }
    }

    @EventHandler
    public void handleStart(ProcessStartEvent event) {
        Display.DisplayState displayState = Display.createContextDisplayState();
        Mouse.MouseState mouseState = Mouse.createContextMouseState();
        Texture2DManager.createContext();
        FontManager.createContext();
        Graphic2D.createContext();
        ElementGraphic2D.createContext();
        ContextScene.create();

        EventBus bus = process.getEventBus();
        bus.registerListener(new ComponentsUpdateListener(displayState, mouseState));
        bus.registerListener(new SceneListener(Scene.getContext()));

        runnable.run();
    }
}
