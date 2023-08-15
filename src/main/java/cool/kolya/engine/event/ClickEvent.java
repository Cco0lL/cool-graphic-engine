package cool.kolya.engine.event;

import org.lwjgl.glfw.GLFW;

/**
 * <p>Represents a mouse click event
 *
 * <p> Note that mods means a bitfield of states of modification keys such a shift, ctrl and e.t.c
 * you can see all offsets of keys starts from {@link GLFW#GLFW_MOD_SHIFT}.
 *
 * <p>Example how to check is shift pressed:
 * <pre>{@code
 * @EventHandler
 * public void handle(ClickEvent event) {
 *     int mods = event.mods;
 *     if ((mods & GLFW.GLFW_MOD_SHIFT) == 1) {
 *         //shift is pressed
 *     } else {
 *         //shit isn't pressed
 *     }
 * }
 * }</pre>
 *
 * @param button a mouse key code, you can see all codes starts from {@link GLFW#GLFW_MOUSE_BUTTON_1 }
 * @param action an action code one of: {@link GLFW#GLFW_RELEASE} or {@link GLFW#GLFW_PRESS}
 * @param mods   mods
 */
public record ClickEvent(int button, int action, int mods) implements Event {}
