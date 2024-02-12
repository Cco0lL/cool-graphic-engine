package cool.kolya.implementation.graphic.element2d;

import cool.kolya.engine.Engine;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ElementGraphic2D {

    private static final ThreadLocal<ElementContextGraphic2D> CONTEXT_GRAPHIC_THREAD_LOCAL = new ThreadLocal<>();

    public static void createContext() {
        CONTEXT_GRAPHIC_THREAD_LOCAL.set(new ElementContextGraphic2D(Engine.CLEANER));
    }

    public static void enable() {
        getContext().enable();
    }

    public static void disable() {
        getContext().disable();
    }

    public static void color(Vector4f color) {
        getContext().color(color);
    }

    public static void windowMatrix(Matrix4f windowMatrix) {
        getContext().windowMatrix(windowMatrix);
    }

    public static void elementMatrix(Matrix4f elementMatrix) {
        getContext().elementMatrix(elementMatrix);
    }

    public static void textureBind(String texture) {
        getContext().textureBind(texture);
    }

    public static void textureUnbind() {
        getContext().textureUnbind();
    }

    public static ElementContextGraphic2D getContext() {
        return CONTEXT_GRAPHIC_THREAD_LOCAL.get();
    }
}
