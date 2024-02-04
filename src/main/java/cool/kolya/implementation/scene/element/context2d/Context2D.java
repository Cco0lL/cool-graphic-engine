package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.engine.data.WindowSize;
import cool.kolya.implementation.component.Display;
import cool.kolya.implementation.scene.element.general.ContextElement;
import cool.kolya.implementation.scene.element.general.matrix.AbstractElementMatrix;
import org.joml.Matrix4f;

public class Context2D extends ContextElement<Drawable2DProperties> {

    public Context2D() {
        matrix = new Context2DMatrix();
    }

    private static final class Context2DMatrix extends AbstractElementMatrix.Context {

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
