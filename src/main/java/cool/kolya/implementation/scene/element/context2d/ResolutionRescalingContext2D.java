package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.engine.data.WindowSize;
import cool.kolya.implementation.component.Display;
import cool.kolya.implementation.scene.element.general.DrawableElement;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrix;
import cool.kolya.implementation.scene.element.general.matrix.ElementMatrixImpl;
import cool.kolya.implementation.scene.element.general.matrix.Properties;
import org.joml.Matrix4f;

public class ResolutionRescalingContext2D extends Context2D {

    private float sidesProportion;
    private int resWidth;
    private int resHeight;

    public ResolutionRescalingContext2D(int resWidth, int resHeight) {
        this.resWidth = resWidth;
        this.resHeight = resHeight;
        sidesProportion = (float) resWidth / resHeight;
        matrix = new ElementMatrixImpl.Context() {
            @Override
            public void update() {
                WindowSize size = Display.getWindowSize();
                sidesProportion = (float) size.width() / size.height();

                elementMatrix.identity();
                float xDivider = (1f / resWidth);
                float yDivider = -1f / resHeight * 2f;
                elementMatrix.translate(-1f, 1f, 0f)
                        .mul(new Matrix4f().scale(xDivider * 1.92f, yDivider, 0f));
                setDirty(true);
            }
        };
    }

    @Override
    public void updateAndRenderChildren(ElementMatrix parentMatrix) {
        WindowSize windowSize = Display.getWindowSize();
        for (DrawableElement<? extends DrawableProperties2D> child : getChildren()) {
            if (getMatrix().isDirty()) {
                child.getProperties().setPropertyDirty(Properties.SIZE, true);
            }
            child.updateAndRender(parentMatrix);
        }
    }

    public void setResHeight(int resHeight) {
        this.resHeight = resHeight;
        matrix.update();
    }

    public void setResWidth(int resWidth) {
        this.resWidth = resWidth;
        matrix.update();
    }

    public void setRes(int resWidth, int resHeight) {
        this.resWidth = resWidth;
        this.resHeight = resHeight;
        matrix.update();
    }

    public final float getSizesProportion() {
        return ((float) resWidth) / resHeight;
    }
}
