package cool.kolya.implementation.scene.element.general;


import cool.kolya.engine.Engine;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;
import org.lwjgl.opengl.GL33;

public abstract class AbstractDrawableElement<P extends DrawableProperties>
        extends AbstractElement
        implements DrawableElement<P> {

    protected final int vaoId = GL33.glGenVertexArrays();
    protected final int vboId = GL33.glGenBuffers();
    protected P properties;
    protected ElementMatrix.Drawable<P> elementMatrix;

    public AbstractDrawableElement() {
        int vaoId = this.vaoId;
        int vboId = this.vboId;
        Engine.CLEANER.register(this, () -> {
            GL33.glDeleteVertexArrays(vaoId);
            GL33.glDeleteBuffers(vboId);
        });
    }

    @Override
    public ElementMatrix.Drawable<P> getElementMatrix() {
        return elementMatrix;
    }

    @Override
    public P getProperties() {
        return properties;
    }
}
