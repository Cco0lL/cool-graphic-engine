package cool.kolya.implementation;

import cool.kolya.engine.Engine;
import cool.kolya.engine.data.CursorPosition;
import cool.kolya.engine.data.FrameBufferSize;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.event.*;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.implementation.component.Display;
import cool.kolya.implementation.component.Mouse;
import cool.kolya.implementation.graphic.context2d.Graphic2D;
import cool.kolya.implementation.graphic.element2d.ElementGraphic2D;
import cool.kolya.implementation.scene.ContextScene;
import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.element.general.ContextElement;
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

        process.getEventBus().registerListener(new EventListener() {
            @EventHandler
            void updateCursorPos(CursorMoveEvent event) {
                mouseState.setCursorPosition(new CursorPosition(event.xPos(), event.yPos()));
            }

            @EventHandler
            void updateMouseButtonState(ClickEvent event) {
                mouseState.setButtonState(event.button(), event.action() == 1);
            }

            @EventHandler
            void updateFrameBufferSize(FrameBufferResizeEvent event) {
                int newWidth = event.width();
                int newHeight = event.height();
                GL33.glViewport(0, 0, newWidth, newHeight);
                displayState.setFrameBufferSize(new FrameBufferSize(newWidth, newHeight));

                for (ContextElement<?> context : Scene.getContext().getContexts()) {
                    context.getMatrix().update();
                }
            }

            @EventHandler
            void updateWindowSize(WindowResizeEvent event) {
                displayState.setWindowSize(new WindowSize(event.width(), event.height()));
                for (ContextElement<?> context : Scene.getContext().getContexts()) {
                    context.getMatrix().update();
                }
            }

            @EventHandler
            void handleRender(RenderEvent e) {
                Scene.getContext().updateAndRenderContexts();
            }
        });

        runnable.run();
    }
}
