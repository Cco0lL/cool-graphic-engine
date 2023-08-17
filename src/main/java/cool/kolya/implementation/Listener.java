package cool.kolya.implementation;

import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;
import cool.kolya.engine.event.KeyTypeEvent;
import cool.kolya.engine.event.UpdateEvent;
import it.unimi.dsi.fastutil.ints.Int2BooleanMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Listener implements EventListener {

    private final Camera camera;

    public Listener(Camera camera) {
        this.camera = camera;

    }

    private final Int2BooleanMap stateMap = new Int2BooleanOpenHashMap() {
        {
            put(GLFW.GLFW_KEY_W, false);
            put(GLFW.GLFW_KEY_S, false);
            put(GLFW.GLFW_KEY_A, false);
            put(GLFW.GLFW_KEY_D, false);
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
            moveVec.y += 0.05f;
        }
        if (stateMap.get(GLFW.GLFW_KEY_S)) {
            moveVec.z += 0.05f;
            moveVec.y += 0.05f;
        }
        if (stateMap.get(GLFW.GLFW_KEY_A)) {
            moveVec.x += -0.05f;
            moveVec.y += 0.05f;
        }
        if (stateMap.get(GLFW.GLFW_KEY_D)) {
            moveVec.x += 0.05f;
            moveVec.y += 0.05f;
        }

        //not moved
        if (moveVec.y == 0f) {
            return;
        }

        camera.movePosition(moveVec);
    }
}
