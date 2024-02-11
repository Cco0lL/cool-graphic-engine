package cool.kolya.implementation.scene.element.matrix;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface MatrixTransformation extends BiConsumer<Vector3f, Matrix4f> {}
