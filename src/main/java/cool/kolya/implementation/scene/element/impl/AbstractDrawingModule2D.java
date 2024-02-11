package cool.kolya.implementation.scene.element.impl;

import cool.kolya.engine.Engine;
import cool.kolya.implementation.graphic.element2d.ElementGraphic2D;
import cool.kolya.implementation.scene.element.AbstractDrawingModule;
import org.lwjgl.opengl.GL33;

public abstract class AbstractDrawingModule2D extends AbstractDrawingModule {

    protected final int vaoId = GL33.glGenVertexArrays(),
            verticesVBOId, texVBOId;

    public AbstractDrawingModule2D() {
        int[] vboIds = new int[2];
        GL33.glGenBuffers(vboIds);

        int vaoId = this.vaoId;
        this.verticesVBOId = vboIds[0];
        this.texVBOId = vboIds[1];

        Engine.CLEANER.register(this, () -> {
            GL33.glDeleteVertexArrays(vaoId);
            GL33.glDeleteBuffers(vboIds);
        });
    }

    protected abstract void drawSelf();

    @Override
    public void draw() {
        ElementGraphic2D.color(boundedElement.getColor());
        ElementGraphic2D.elementMatrix(boundedElement.getTransformMatrix().get());
        String texture = boundedElement.getTexture();
        if (texture != null) {
            ElementGraphic2D.textureBind(texture);
        }
        drawSelf();
        if (texture != null) {
            ElementGraphic2D.textureUnbind();
        }
    }
}
