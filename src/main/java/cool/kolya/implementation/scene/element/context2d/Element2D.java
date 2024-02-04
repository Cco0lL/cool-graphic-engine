package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.DrawableElement;

public interface Element2D extends DrawableElement<Drawable2DProperties> {

    void addClickCallback(Runnable runnable);

    void addHoverCallback(Runnable runnable);

    Drawable2DProperties getProperties();

    boolean isHovered();

    ElementMouseState getElementMouseState();
}
