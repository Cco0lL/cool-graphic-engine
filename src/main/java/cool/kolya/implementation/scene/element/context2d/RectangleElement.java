package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.IPropertyVector2f;
import cool.kolya.implementation.scene.element.general.matrix.Properties;
import org.lwjgl.opengl.GL33;

public class RectangleElement extends AbstractParent2D {

    protected float[] texVertices = new float[] {
            0f, 0f,
            1f, 0f,
            1f, 1f,
            1f, 1f,
            0f, 1f,
            0f, 0f
    };

    public RectangleElement() {
        updateTex();
    }

    @Override
    public void update() {
        super.update();
        if (properties.isPropertyDirty(Properties.MATRICES_LENGTH)) {
            final float[] vertices = vertices();
            GL33.glBindVertexArray(vaoId);

            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, verticesVBOId);
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_DYNAMIC_DRAW);

            GL33.glVertexAttribPointer(0, 2, GL33.GL_FLOAT, false, 0, 0);
            GL33.glEnableVertexAttribArray(0);

            GL33.glBindVertexArray(0);
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
        }
    }

    @Override
    public void drawSelf() {
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 6);
    }

    private float[] vertices() {
        IPropertyVector2f size = properties.getSize();
        float width = size.x();
        float height = size.y();

        return new float[]{
                0f,     0f,
                width,  0f,
                width,  height,
                width,  height,
                0f,     height,
                0f,     0f,
        };
    }

    protected void updateTex() {
        GL33.glBindVertexArray(vaoId);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, texVBOId);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, texVertices, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 0, 0);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
        GL33.glEnableVertexAttribArray(1);
        GL33.glBindVertexArray(0);
    }
}
