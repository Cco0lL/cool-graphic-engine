package cool.kolya.implementation.scene.element.general;

public interface IPropertyVector3f extends IPropertyVector2f {

    float z();

    void z(float z);

    void set(float x, float y, float z);

    void set(IPropertyVector3f other);

    void add(float x, float y, float z);

    void remove(float x, float y, float z);

    void write(IPropertyVector3f dest);
}
