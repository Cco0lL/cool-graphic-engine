package cool.kolya.implementation.component;

import cool.kolya.engine.Engine;
import cool.kolya.engine.data.CursorPosition;
import org.lwjgl.glfw.GLFW;

public class Mouse {

    private static final ThreadLocal<MouseState> STATE_THREAD_LOCAL = new ThreadLocal<>();

    public static MouseState createContextMouseState() {
        MouseState mouseState = new MouseState();

        CursorPosition cursorPosition = Engine.getContextProcess().getSettings().getCursorPosition();
        mouseState.setCursorPosition(cursorPosition);

        STATE_THREAD_LOCAL.set(mouseState);

        return mouseState;
    }

    public static boolean isRightPressed() {
        return isPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    }

    public static boolean isLeftPressed() {
        return isPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
    }

    public static boolean isPressed(int key) {
        return getState().getButtonState(key);
    }

    public boolean isReleased(int key) {
        return !getState().getButtonState(key);
    }

    public static CursorPosition getCursorPosition() {
        return getState().getCursorPosition();
    }

    private static MouseState getState() {
        return STATE_THREAD_LOCAL.get();
    }

    public static class MouseState {

        private final boolean[] buttonStates = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
        private CursorPosition cursorPosition;

        private MouseState() {}

        public boolean getButtonState(int key) {
            return buttonStates[key];
        }

        public void setButtonState(int key, boolean state) {
            buttonStates[key] = state;
        }

        public CursorPosition getCursorPosition() {
            return cursorPosition;
        }

        public void setCursorPosition(CursorPosition cursorPosition) {
            this.cursorPosition = cursorPosition;
        }
    }
}
