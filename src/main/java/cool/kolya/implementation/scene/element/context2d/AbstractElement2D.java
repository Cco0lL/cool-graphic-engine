package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.engine.data.CursorPosition;
import cool.kolya.implementation.component.Mouse;
import cool.kolya.implementation.graphic.element2d.ElementGraphic2D;
import cool.kolya.implementation.scene.element.general.AbstractDrawableElement;
import cool.kolya.implementation.scene.element.general.IPropertyVector2f;
import cool.kolya.implementation.scene.element.general.matrix.AbstractElementMatrix;
import cool.kolya.implementation.scene.element.general.matrix.Properties;
import cool.kolya.implementation.scene.element.general.matrix.Transformations;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class AbstractElement2D extends AbstractDrawableElement<Drawable2DProperties>
        implements Element2D  {

    protected Drawable2DProperties properties = new Drawable2DPropertiesImpl();
    protected ElementMouseState mouseState = new ElementMouseState();
    protected ElementCallbacks callbacks = new ElementCallbacks();
    protected boolean hovered;

    public AbstractElement2D() {
        elementMatrix = new Element2DMatrix(properties);
    }

    public abstract void drawSelf();

    @Override
    public void addClickCallback(Runnable runnable) {
        callbacks.getCallback(ElementCallback.Type.HOVER).addCallback(runnable);
    }

    @Override
    public void addHoverCallback(Runnable runnable) {
        callbacks.getCallback(ElementCallback.Type.HOVER).addCallback(runnable);
    }

    @Override
    public void update() {
        updateMouseState();
        updateHover();
    }

    @Override
    public void render() {
        ElementGraphic2D.enable();
        ElementGraphic2D.color(getProperties().getColor());
        ElementGraphic2D.elementMatrix(elementMatrix.getMatrix());
        drawSelf();
        ElementGraphic2D.disable();
    }

    @Override
    public Drawable2DProperties getProperties() {
        return properties;
    }

    @Override
    public boolean isHovered() {
        return hovered;
    }

    @Override
    public ElementMouseState getElementMouseState() {
        return mouseState;
    }

    protected void updateMouseState() {
        CursorPosition cursorPosition = Mouse.getCursorPosition();
        Vector4f vec = new Vector4f();
        vec.set(cursorPosition.xPos(), cursorPosition.yPos(), 0f, 1f);
        vec.mul(elementMatrix.getTransformationMatrix().invert(new Matrix4f()));
        mouseState.getPosition().set(vec.x(), vec.y());
    }

    protected void updateHover() {
        IPropertyVector2f size = properties.getSize();
        Vector2f mousePosition = mouseState.getPosition();

        boolean hovered = mousePosition.x() >= 0 && mousePosition.x() <= size.x()
                && mousePosition.y() >= 0 && mousePosition.y() <= size.y();

        if (this.hovered != hovered) {
            try {
                callbacks.getCallback(ElementCallback.Type.HOVER).call();
            } finally {
                this.hovered = hovered;
            }
        }
    }

    private static final class Element2DMatrix extends AbstractElementMatrix.Drawable<Drawable2DProperties> {

        private final Matrix4f[] transformationMatrices = new Matrix4f[Properties.LENGTH];

        public Element2DMatrix(Drawable2DProperties properties) {
            super(properties);
            for (int i = 0; i < transformationMatrices.length; i++) {
                transformationMatrices[i] = new Matrix4f();
            }
        }

        @Override
        protected void updateTransformationMatrix() {
            for (Matrix4f matrix : transformationMatrices) {
                this.transformationMatrix.mul(matrix);
            }
        }

        @Override
        protected void updateMatrices() {
            for (int i = 0; i < transformationMatrices.length; i++) {
                if (!properties.isPropertyDirty(i)) {
                    continue;
                }
                Matrix4f mat = transformationMatrices[i];
                switch (i) {
                    case (Properties.OFFSET) -> Transformations.TRANSLATE
                            .accept(properties.getOffset().toVector4f(), mat);
                    case (Properties.ROTATION) -> Transformations.ROTATE
                            .accept(properties.getRotation().toVector4f(), mat);
                    case (Properties.SCALE) -> Transformations.SCALE
                            .accept(properties.getScale().toVector4f(), mat);
                    case (Properties.SIZE) -> {
                        Vector4f sizeOffset = properties.getSize()
                                .toVector4f()
                                .mul(-0.5f, -0.5f, 1f ,1f);
                        Transformations.TRANSLATE.accept(sizeOffset, mat);
                    }
                }
                properties.setPropertyDirty(i, false);
            }
        }
    }
}