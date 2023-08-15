package cool.kolya.engine.event;

import org.lwjgl.glfw.GLFW;

/**
 * <p>Represents a keyboard key type event
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
 * @param key key code, you can see all codes, starts from {@link GLFW#GLFW_KEY_SPACE}
 * @param action key type action: {@link GLFW#GLFW_RELEASE},  {@link GLFW#GLFW_PRESS}, {@link GLFW#GLFW_REPEAT}
 * @param mods mods
 */
public record KeyTypeEvent(int key, int action, int mods) implements Event {}
