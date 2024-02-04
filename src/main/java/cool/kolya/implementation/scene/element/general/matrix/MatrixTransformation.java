package cool.kolya.implementation.scene.element.general.matrix;

import cool.kolya.implementation.scene.element.general.IPropertyVector3f;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface MatrixTransformation extends BiConsumer<IPropertyVector3f, Matrix4f> {}
