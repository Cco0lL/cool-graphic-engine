package cool.kolya.implementation.scene.element.impl;

import cool.kolya.engine.data.WindowSize;
import cool.kolya.implementation.component.Display;
import cool.kolya.implementation.graphic.element2d.ElementGraphic2D;
import cool.kolya.implementation.scene.element.ContextElement;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;

public class Context2D extends ContextElement {

    private final Matrix4f windowMatrix = new Matrix4f();

    @Override
    public void render() {
        updateWindowMatrix();
        ElementGraphic2D.enable();
        ElementGraphic2D.windowMatrix(windowMatrix);
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

    protected void updateWindowMatrix() {
        windowMatrix.identity();
        WindowSize window = Display.getWindowSize();
        float xDivider = 1f / window.width() * 2f;
        float yDivider = -1f / window.height() * 2f;
        windowMatrix.translate(-1f, 1f, 0f)
                .mul(new Matrix4f().scale(xDivider, yDivider, 1f));
    }
}
