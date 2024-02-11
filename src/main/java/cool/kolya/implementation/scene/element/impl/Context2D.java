package cool.kolya.implementation.scene.element.impl;

import cool.kolya.engine.data.WindowSize;
import cool.kolya.implementation.component.Display;
import cool.kolya.implementation.graphic.element2d.ElementGraphic2D;
import cool.kolya.implementation.scene.element.ContextElement;
import cool.kolya.implementation.scene.element.matrix.AbstractContextTransformMatrix;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;

public class Context2D extends ContextElement {

    @Override
    public void render() {
        ElementGraphic2D.enable();
        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA,GL33.GL_ONE_MINUS_SRC_ALPHA);
        super.render();
        GL33.glDisable(GL33.GL_BLEND);
        ElementGraphic2D.disable();
    }

    @Override
    public void updateState() {
        WindowSize windowSize = Display.getWindowSize();
        properties.getSize().set(windowSize.width(), windowSize.height());
        super.updateState();
    }

    public static class Context2DTransformMatrix extends AbstractContextTransformMatrix {

        public Context2DTransformMatrix(Context2D contextElement) {
            super(contextElement);
        }

        @Override
        protected void updateWindowTransformMatrix() {
            windowTransformMatrix.identity();
            WindowSize window = Display.getWindowSize();
            float xDivider = 1f / window.width() * 2f;
            float yDivider = -1f / window.height() * 2f;
            windowTransformMatrix.translate(-1f, 1f, 0f)
                    .mul(new Matrix4f().scale(xDivider, yDivider, 1f));
        }
    }
}
