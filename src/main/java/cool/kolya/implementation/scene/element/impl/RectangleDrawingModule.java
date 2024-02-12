package cool.kolya.implementation.scene.element.impl;

import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.element.WindowSettingsInterpreter;
import cool.kolya.implementation.scene.element.property.Property;
import cool.kolya.implementation.scene.element.property.PropertyVector;
import org.lwjgl.opengl.GL33;

public class RectangleDrawingModule extends AbstractDrawingModule2D {

    public RectangleDrawingModule() {
        this(new float[]{
                0f, 0f,
                1f, 0f,
                1f, 1f,
                1f, 1f,
                0f, 1f,
                0f, 0f
        });
    }

    public RectangleDrawingModule(float[] texVertices) {
        GL33.glBindVertexArray(vaoId);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, texVBOId);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, texVertices, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 0, 0);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
        GL33.glEnableVertexAttribArray(1);
        GL33.glBindVertexArray(0);
    }

    @Override
    protected boolean isNeedUpdate() {
        return boundedElement.getProperties().isPropertyDirty(Property.SIZE);
    }

    @Override
    public void updateExact() {
        final float[] vertices = vertices();
        GL33.glBindVertexArray(vaoId);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, verticesVBOId);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_DYNAMIC_DRAW);

        GL33.glVertexAttribPointer(0, 2, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        GL33.glBindVertexArray(0);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
        boundedElement.getProperties().setPropertyDirty(Property.SIZE, false);

    }

    @Override
    public void drawSelf() {
        GL33.glBindVertexArray(vaoId);
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 6);
        GL33.glBindVertexArray(0);
    }

    private float[] vertices() {
        PropertyVector size = boundedElement.getProperties().getSize();
        WindowSettingsInterpreter interpreter = Scene.getContext().getCurrentContextElement()
                .getWindowSettingsInterpreter();

        float width = interpreter.interpretWidth(size.x());
        float height = interpreter.interpretHeight(size.y());

        return new float[]{
                0f, 0f,
                width, 0f,
                width, height,
                width, height,
                0f, height,
                0f, 0f,
        };
    }
}
