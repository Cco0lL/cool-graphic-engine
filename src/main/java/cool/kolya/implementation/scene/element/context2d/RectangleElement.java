package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.IPropertyVector2f;
import cool.kolya.implementation.scene.element.general.matrix.Properties;
import org.lwjgl.opengl.GL33;

public class RectangleElement extends AbstractParent2D {

    @Override
    public void update() {
        super.update();
        if (properties.isPropertyDirty(Properties.SIZE)) {
            final float[] vertices = vertices();
            GL33.glBindVertexArray(vaoId);

            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId);
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_DYNAMIC_DRAW);

            GL33.glVertexAttribPointer(0, 2, GL33.GL_FLOAT, false, 0, 0);
            GL33.glEnableVertexAttribArray(0);

            GL33.glBindVertexArray(0);
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
        }
    }

    @Override
    public void drawSelf() {
        GL33.glBindVertexArray(vaoId);
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 6);
        GL33.glBindVertexArray(0);
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
}
