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
    protected boolean[] pressedKeys = new boolean[3];
    protected final Callbacks callbacks = new Callbacks();

    public InteractingModule2D(Element element) {
        this.element = element;
    }

    @Override
    public void handleClickEvent(ClickEvent event) {}

    @Override
    public void handleScrollEvent(ScrollEvent event) {}

    @Override
    public void updateMouseState() {
        CursorPosition cursorPosition = Mouse.getCursorPosition();
        Vector2f interpretedPos = Scene.getContext().getCurrentContextElement()
                .getWindowSettingsInterpreter()
                .interpretWindowPos(new Vector2f((float) cursorPosition.xPos(),
                        (float) cursorPosition.yPos()));

        Vector4f vec = new Vector4f(interpretedPos, 0 ,1);
        vec.mul(element.getTransformMatrix().getTransform().invert(new Matrix4f()));

        mousePosition.set(vec.x(), vec.y());

        PropertyVector size = element.getProperties().getSize();
        boolean hovered = mousePosition.x() >= 0 && mousePosition.x() <= size.x()
                && mousePosition.y() >= 0 && mousePosition.y() <= size.y();

        if (this.hovered != hovered) {
            this.hovered = hovered;
            callbacks.getCallback(Callback.Type.HOVER).run();
        }
    }

    @Override
    public void addCallback(Callback.Type type, Runnable callbackRunnable) {
        callbacks.addCallback(type, callbackRunnable);
    }

    public boolean isHovered() {
        return hovered;
    }
}
