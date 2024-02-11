package cool.kolya.implementation.scene.element.property;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LinkablePropertyVector extends ElementPropertyVector {

    private final List<MergePropertyVector> linkedVectors = new ArrayList<>();

    public LinkablePropertyVector(ElementPropertyVector other, int propertyOffset) {
        this(other.x(), other.y(), other.z(), other.properties, propertyOffset);
    }

    public LinkablePropertyVector(float d, ElementTransformProperties properties, int propertyOffset) {
        this(d, d, d, properties, propertyOffset);
    }

    public LinkablePropertyVector(float x, float y, float z, ElementTransformProperties properties,
                                  int propertyOffset) {
        super(x, y, z, properties, propertyOffset);
        changeCallback.add(() -> {
            for (MergePropertyVector linkedVec : linkedVectors) {
                linkedVec.mergeXYZ();
            }
        });
    }

    public void link(MergePropertyVector vec) {
        linkedVectors.add(vec);
        vec.setLinkVec(this);
    }

    public void unlink(MergePropertyVector vec) {
        linkedVectors.remove(vec);
        vec.setLinkVec(null);
    }

    public void unlink(UUID id) {
        linkedVectors.removeIf(vec -> {
            boolean remove = vec.properties.getElementId().equals(id);
            if (remove) {
                vec.setLinkVec(null);
            }
            return remove;
        });
    }
}
