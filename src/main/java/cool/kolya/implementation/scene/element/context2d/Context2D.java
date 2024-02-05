package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.engine.data.WindowSize;
import cool.kolya.implementation.component.Display;
import cool.kolya.implementation.scene.element.general.ContextElement;
import cool.kolya.implementation.scene.element.general.DrawableElement;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrixImpl;
import org.joml.Matrix4f;

public class Context2D extends ContextElement<DrawableProperties2D> {

    public Context2D() {
        matrix = new ContextMatrix2D();
    }

    @Override
    public void updateAndRenderChildren(ElementMatrix parentMatrix) {
        WindowSize windowSize = Display.getWindowSize();
        for (DrawableElement<? extends DrawableProperties2D> child : getChildren()) {
            if (getMatrix().isDirty()) {
                child.getProperties().getParentSize().set(windowSize.width(), windowSize.height());
            }
            child.updateAndRender(parentMatrix);
        }
    }

    private static final class ContextMatrix2D extends ElementMatrixImpl.Context {

        @Override
        public void update() {
            elementMatrix.identity();
            WindowSize window = Display.getWindowSize();
            float xDivider = 1f / window.width() * 2f;
            float yDivider = -1f / window.height() * 2f;
            elementMatrix.translate(-1f, 1f, 0f)
                    .mul(new Matrix4f().scale(xDivider, yDivider, 0f));
            setDirty(true);

        }
    }
}
