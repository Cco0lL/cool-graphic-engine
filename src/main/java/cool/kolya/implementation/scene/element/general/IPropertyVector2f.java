package cool.kolya.implementation.scene.element.general;

public interface IPropertyVector2f extends IPropertyVector {

    float x();

    void x(float x);

    float y();

    void y(float y);

    void set(float x, float y);

    void set(IPropertyVector2f other);

    void add(float x, float y);

    void remove(float x, float y);

    void write(IPropertyVector2f dest);

}
