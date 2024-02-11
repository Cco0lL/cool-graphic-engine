package cool.kolya.implementation.scene.element.property;

import cool.kolya.implementation.scene.element.callback.Callback;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface PropertyVector {

    float x();

    void x(float x);

    float y();

    void y(float y);

    float z();

    void z(float z);

    void set(float x, float y);

    void set(float x, float y, float z);

    void set(Vector2f vec);

    void set(Vector3f vec);

    void set(PropertyVector other);

    void add(float x, float y);

    void add(float x, float y, float z);

    void add(Vector2f vec);

    void add(Vector3f vec);

    void remove(float x, float y);

    void remove(float x, float y, float z);

    void remove(Vector2f vec);

    void remove(Vector3f vec);

    void write(PropertyVector dest);

    void write(Vector2f dest);

    void write(Vector3f dest);

    Callback getChangeCallback();

    Vector2f toVector2f();

    Vector3f toVector3f();

    Vector4f toVector4f();
}
