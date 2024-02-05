package cool.kolya.implementation.scene.element.general;


import cool.kolya.engine.Engine;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;
import org.lwjgl.opengl.GL33;

public abstract class AbstractDrawableElement<P extends DrawableProperties>
        extends AbstractElement
        implements DrawableElement<P> {

    protected final int vaoId = GL33.glGenVertexArrays(), verticesVBOId, texVBOId;
    protected P properties;
    protected String texture;
    protected ElementMatrix.Drawable elementMatrix;

    public AbstractDrawableElement() {
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

    @Override
    public ElementMatrix.Drawable getElementMatrix() {
        return elementMatrix;
    }

    @Override
    public P getProperties() {
        return properties;
    }

    @Override
    public void setTexture(String texture) {
        this.texture = texture;
    }

    @Override
    public String getTexture() {
        return texture;
    }
}
