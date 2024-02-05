package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.DrawableElement;

public interface Element2D extends DrawableElement<DrawableProperties2D> {

    void addClickCallback(Runnable runnable);

    void addHoverCallback(Runnable runnable);

    DrawableProperties2D getProperties();

    boolean isHovered();

    ElementMouseState getElementMouseState();
}
