package cool.kolya.implementation.scene.element.general;

import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;

public interface DrawableElement<P extends DrawableProperties> extends Element {

    void update();

    void render();

    default void updateAndRender(ElementMatrix parentMatrixTree) {
        update();
        getElementMatrix().update(parentMatrixTree);
        getProperties().unmarkDirty();
        render();
    }

    P getProperties();

    ElementMatrix.Drawable getElementMatrix();

    void setTexture(String texture);

    String getTexture();
}
