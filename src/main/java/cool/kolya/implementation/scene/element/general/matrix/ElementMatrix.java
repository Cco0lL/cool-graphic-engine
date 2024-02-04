package cool.kolya.implementation.scene.element.general.matrix;

import cool.kolya.implementation.scene.element.general.DrawableProperties;
import org.joml.Matrix4f;

public interface ElementMatrix {

    boolean isDirty();

    Matrix4f getMatrix();

    interface Context extends ElementMatrix {

        void update();
    }

    interface Drawable<P extends DrawableProperties> extends ElementMatrix {

        void update(ElementMatrix parentMatrix);

        Matrix4f getTransformationMatrix();
    }
}
