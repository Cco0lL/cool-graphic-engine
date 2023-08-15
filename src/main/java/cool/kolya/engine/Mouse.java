package cool.kolya.engine;

import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class Mouse {

    private final int[] states = new int[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

    Mouse() {
        Arrays.fill(states, 0);
    }

    /**
     * @param key   - key code
     * @param state - pressed if 1, released if 0
     */
    void changeState(int key, int state) {
        states[key] = state;
    }

    public boolean isPressed(int key) {
        return states[key] == 1;
    }

    public boolean isReleased(int key) {
        return states[key] == 0;
    }
}
