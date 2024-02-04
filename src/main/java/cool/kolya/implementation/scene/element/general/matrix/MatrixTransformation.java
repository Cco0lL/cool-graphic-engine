package cool.kolya.implementation.scene.element.general.matrix;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface MatrixTransformation extends BiConsumer<Vector4f, Matrix4f> {}
