package cool.kolya.implementation.scene.element.impl;

import cool.kolya.engine.data.CursorPosition;
import cool.kolya.engine.event.ClickEvent;
import cool.kolya.engine.event.ScrollEvent;
import cool.kolya.implementation.component.Mouse;
import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.element.callback.Callback;
import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.InteractingModule;
import cool.kolya.implementation.scene.element.callback.Callbacks;
import cool.kolya.implementation.scene.element.property.PropertyVector;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class InteractingModule2D implements InteractingModule {

    protected final Element element;
    protected boolean hovered;
    protected Vector2f mousePosition = new Vector2f();
    protected boolean[] mouseButtonStates = new boolean[3];
    protected final Callbacks<Callback.InteractType> callbacks = new Callbacks<>(Callback.InteractType.class);
    protected float scrollXOffset, scrollYOffset;

    public InteractingModule2D(Element element) {
        this.element = element;
    }

    @Override
    public void updateInteractions() {
        Scene scene = Scene.getContext();
        updateMousePosition(scene);
        updateHoverState();
        if (hovered) {
            Scene.LastEventData<ClickEvent> lastClickEventData = scene.getLastClickEventData();
            if (!lastClickEventData.isOutdated()) {
                updateMouseButtonStates(lastClickEventData.getLastEvent());
            }
            Scene.LastEventData<ScrollEvent> lastScrollEventData = scene.getLastScrollEventData();
            if (!lastScrollEventData.isOutdated()) {
                updateScrollState(lastScrollEventData.getLastEvent());
            }
        }
    }

    @Override
    public void addCallback(Callback.InteractType type, Runnable callbackRunnable) {
        callbacks.addCallback(type, callbackRunnable);
    }

    public boolean isHovered() {
        return hovered;
    }

    public float getScrollXOffset() {
        return scrollXOffset;
    }

    public float getScrollYOffset() {
        return scrollYOffset;
    }

    public Vector2f getMousePosition() {
        return mousePosition;
    }

    protected void updateMousePosition(Scene scene) {
        CursorPosition cursorPosition = Mouse.getCursorPosition();
        Vector2f interpretedPos = scene.getCurrentContextElement().getWindowSettingsInterpreter()
                .interpretWindowPos(new Vector2f((float) cursorPosition.xPos(),
                        (float) cursorPosition.yPos()));

        Vector4f vec = new Vector4f(interpretedPos, 0 ,1);
        vec.mul(element.getTransformMatrix().getTransform().invert(new Matrix4f()));

        mousePosition.set(vec.x(), vec.y());
    }

    protected void updateHoverState() {
        PropertyVector size = element.getProperties().getSize();
        boolean hovered = mousePosition.x() >= 0 && mousePosition.x() <= size.x()
                && mousePosition.y() >= 0 && mousePosition.y() <= size.y();

        if (this.hovered != hovered) {
            this.hovered = hovered;
            callbacks.runCallback(Callback.InteractType.HOVER);
        }
    }

    protected void updateMouseButtonStates(ClickEvent event) {
        int button = event.button();
        if (button > 2) {
            return;
        }
        boolean pressed = event.action() != 0;
        if (pressed != mouseButtonStates[button]) {
            mouseButtonStates[button] = pressed;
            int offset = pressed ? Callback.InteractType.PRESS_OFFSET :
                    Callback.InteractType.RELEASE_OFFSET;
            Callback.InteractType type = Callback.InteractType.VALUES.get(offset + button);
            callbacks.runCallback(type);
        }
    }

    protected void updateScrollState(ScrollEvent event) {
        scrollXOffset = (float) event.xOffset();
        scrollYOffset = (float) event.yOffset();
        callbacks.runCallback(Callback.InteractType.SCROLL);
    }
}
