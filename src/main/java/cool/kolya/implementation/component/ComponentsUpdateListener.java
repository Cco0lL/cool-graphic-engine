package cool.kolya.implementation.component;

import cool.kolya.engine.data.CursorPosition;
import cool.kolya.engine.data.FrameBufferSize;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.event.*;
import org.lwjgl.opengl.GL33;

public class ComponentsUpdateListener implements EventListener {

    private final Display.DisplayState displayState;
    private final Mouse.MouseState mouseState;

    public ComponentsUpdateListener(Display.DisplayState displayState, Mouse.MouseState mouseState) {
        this.displayState = displayState;
        this.mouseState = mouseState;
    }

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
    }

    @EventHandler
    void updateWindowSize(WindowResizeEvent event) {
        displayState.setWindowSize(new WindowSize(event.width(), event.height()));
    }
}
