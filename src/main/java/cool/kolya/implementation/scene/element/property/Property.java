package cool.kolya.implementation.scene.element.property;

import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.matrix.Transformations;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public enum Property {

    ALIGN((e, m) -> {
        TransformProperties ep = e.getProperties();
        TransformProperties pp = e.getParent().getProperties();
        Vector3f alignVec = ep.getAlign().toVector3f();
        Vector3f parentSizeVec = pp.getSize().toVector3f();
        Transformations.TRANSLATE.accept(alignVec.mul(parentSizeVec), m);

    }),
    OFFSET((e, m) -> {
        TransformProperties ep = e.getProperties();
        Transformations.TRANSLATE.accept(ep.getOffset().toVector3f(), m);
    }),
    ROTATION((e, m) -> {
        TransformProperties ep = e.getProperties();
        Transformations.ROTATE.accept(ep.getXYZRotation().toVector3f(), m);
    }),
    SCALE((e, m) -> {
        TransformProperties ep = e.getProperties();
        Transformations.SCALE.accept(ep.getScale().toVector3f(), m);
    }),
    ORIGIN((e, m) -> {
        TransformProperties ep = e.getProperties();
        Vector3f originVec = ep.getOrigin().toVector3f().negate();
        Vector3f size = ep.getSize().toVector3f();
        Transformations.TRANSLATE.accept(originVec.mul(size), m);
    }),
    SIZE((e, m) -> {});

    public static final List<Property> VALUES = List.of(values());
    public static final int MATRICES_LENGTH = 5;
    public static final int LENGTH = 6;

    private final UpdateFunction updateFunction;

    Property(UpdateFunction updateFunction) {
        this.updateFunction = updateFunction;
    }

    public static Property get(int ordinal) {
        return VALUES.get(ordinal);
    }

    public UpdateFunction getUpdateFunction() {
        return updateFunction;
    }

    @FunctionalInterface
    public interface UpdateFunction {

        void update(Element element, Matrix4f matrix);
    }
}
