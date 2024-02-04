package cool.kolya.implementation.scene.element.general;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface IPropertyVector {

    Vector4f toVector4f();

    Vector3f toVector3f();

    Vector2f toVector2f();
}
