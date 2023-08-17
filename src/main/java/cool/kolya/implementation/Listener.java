package cool.kolya.implementation;

import cool.kolya.Engine;
import cool.kolya.engine.Window;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.event.*;
import it.unimi.dsi.fastutil.ints.Int2BooleanMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Listener implements EventListener {

    private final Camera camera;
    private double widthCenter;
    private double heightCenter;

    public Listener(Camera camera) {
        this.camera = camera;
        Window window = Engine.getProcessor().getWindow();
        WindowSize size = window.getSize();
        widthCenter = size.width() / 2.0;
        heightCenter = size.height() / 2.0;
    }

    private final Int2BooleanMap stateMap = new Int2BooleanOpenHashMap() {
        {
            put(GLFW.GLFW_KEY_W, false);
            put(GLFW.GLFW_KEY_S, false);
            put(GLFW.GLFW_KEY_A, false);
            put(GLFW.GLFW_KEY_D, false);
            put(GLFW.GLFW_KEY_SPACE, false);
            put(GLFW.GLFW_KEY_LEFT_SHIFT, false);
        }
    };

    @EventHandler
    void handle(KeyTypeEvent event) {
        if (event.action() == GLFW.GLFW_REPEAT || !stateMap.containsKey(event.key()))
            return;
        stateMap.put(event.key(), event.action() == GLFW.GLFW_PRESS);
    }

    @EventHandler
    void handle(UpdateEvent event) {
        Vector3f moveVec = new Vector3f();
        if (stateMap.get(GLFW.GLFW_KEY_W)) {
            moveVec.z += -0.05f;
        }
        if (stateMap.get(GLFW.GLFW_KEY_S)) {
            moveVec.z += 0.05f;
        }
        if (stateMap.get(GLFW.GLFW_KEY_A)) {
            moveVec.x += -0.05f;
        }
        if (stateMap.get(GLFW.GLFW_KEY_D)) {
            moveVec.x += 0.05f;
        }
        if (stateMap.get(GLFW.GLFW_KEY_SPACE)) {
            moveVec.y += 0.05f;
        }
        if (stateMap.get(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            moveVec.y += -0.05f;
        }
        camera.movePosition(moveVec);
    }

    @EventHandler
    void handle(WindowResizeEvent event) {
        widthCenter = event.width() / 2.0;
        heightCenter = event.height() / 2.0;
    }

    @EventHandler
    void handle(CursorMoveEvent event) {
        Window window = Engine.getProcessor().getWindow();
        window.setCursorPosition(widthCenter, heightCenter);
        Vector2f rot = new Vector2f(0.05f);
        rot.mul((float) (event.xPos() - widthCenter), (float) (event.yPos() - heightCenter));
        if (rot.x == 0f && rot.y == 0f) {
            return;
        }
        camera.moveRotation(rot.x, rot.y);
    }
}
