package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.engine.event.ClickEvent;
import org.joml.Vector2f;

public class ElementMouseState {

    private Vector2f position = new Vector2f();
    private boolean isRightPressed;
    private boolean isLeftPressed;
    private ClickEvent lastClickEvent;

    public boolean isRightPressed() {
        return isRightPressed;
    }

    public boolean isLeftPressed() {
        return isLeftPressed;
    }

    public ClickEvent getLastClickEvent() {
        return lastClickEvent;
    }

    public Vector2f getPosition() {
        return position;
    }

    void setRightPressed(boolean isRightPressed) {
        this.isRightPressed = isRightPressed;
    }

    void setLeftPressed(boolean leftPressed) {
        isLeftPressed = leftPressed;
    }

    void setPosition(Vector2f position) {
        this.position = position;
    }
}
