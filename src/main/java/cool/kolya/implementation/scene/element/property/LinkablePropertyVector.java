package cool.kolya.implementation.scene.element.property;

import java.util.ArrayList;
import java.util.List;

public class LinkablePropertyVector extends ElementPropertyVector {

    private final List<MergePropertyVector> linkedVectors = new ArrayList<>();

    public LinkablePropertyVector(ElementPropertyVector other, Property property) {
        this(other.x(), other.y(), other.z(), other.properties, property);
    }

    public LinkablePropertyVector(float d, TransformPropertiesImpl properties, Property property) {
        this(d, d, d, properties, property);
    }

    public LinkablePropertyVector(float x, float y, float z, TransformPropertiesImpl properties,
                                  Property property) {
        super(x, y, z, properties, property);
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
}
