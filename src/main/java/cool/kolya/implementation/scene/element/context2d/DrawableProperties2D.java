package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.AbstractDrawableProperties;
import cool.kolya.implementation.scene.element.general.PropertyVector2f;
import cool.kolya.implementation.scene.element.general.matrix.Properties;

public class DrawableProperties2D extends AbstractDrawableProperties {

    private final PropertyVector2f offset = new PropertyVector2f(0, this, Properties.OFFSET);
    private final PropertyVector2f rotation = new PropertyVector2f(0, this, Properties.ROTATION);
    private final PropertyVector2f scale = new PropertyVector2f(0, this, Properties.SCALE);
    private final PropertyVector2f size = new PropertyVector2f(0, this, Properties.SIZE);
    private final  PropertyVector2f parentSize = new PropertyVector2f(0, this, Properties.ALIGN);
    private final  PropertyVector2f align = new PropertyVector2f(0, this, Properties.ALIGN);
    private final PropertyVector2f origin = new PropertyVector2f(0.5f, this, Properties.ORIGIN);

    @Override
    public PropertyVector2f getOffset() {
        return offset;
    }

    @Override
    public PropertyVector2f getRotation() {
        return rotation;
    }

    @Override
    public PropertyVector2f getScale() {
        return scale;
    }

    @Override
    public PropertyVector2f getSize() {
        return size;
    }

    @Override
    public PropertyVector2f getParentSize() {
        return parentSize;
    }

    @Override
    public PropertyVector2f getAlign() {
        return align;
    }

    @Override
    public PropertyVector2f getOrigin() {
        return origin;
    }
}
