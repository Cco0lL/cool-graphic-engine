package cool.kolya.implementation.scene.element.general;


import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;

public abstract class AbstractDrawableElement<P extends DrawableProperties>
        extends AbstractElement
        implements DrawableElement<P> {

    protected P properties;
    protected ElementMatrix.Drawable<P> elementMatrix;

    @Override
    public ElementMatrix.Drawable<P> getElementMatrix() {
        return elementMatrix;
    }

    @Override
    public P getProperties() {
        return properties;
    }
}
