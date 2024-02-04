package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.AbstractDrawableProperties;
import cool.kolya.implementation.scene.element.general.IPropertyVector2f;
import cool.kolya.implementation.scene.element.general.PropertyVector2f;
import cool.kolya.implementation.scene.element.general.matrix.Properties;

public class Drawable2DPropertiesImpl extends AbstractDrawableProperties
        implements Drawable2DProperties {

    private final PropertyVector2f size = new PropertyVector2f(0, this, Properties.SIZE);

    @Override
    public IPropertyVector2f getSize() {
        return size;
    }
}