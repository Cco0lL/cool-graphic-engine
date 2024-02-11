package cool.kolya.implementation.scene.element.matrix;

public class Transformations {

    public static final MatrixTransformation TRANSLATE = (vec, mat) ->
            mat.identity().translate(vec.x(), vec.y(), vec.z());

    public static final MatrixTransformation ROTATE = (vec, mat) ->
            mat.identity().rotateAffineXYZ(
                    (float) Math.toRadians(vec.x()),
                    (float) Math.toRadians(vec.y()),
                    (float) Math.toRadians(vec.z()));

    public static final MatrixTransformation SCALE = (vec, mat) ->
            mat.identity().scale(vec.x(), vec.y(), vec.z());
}
