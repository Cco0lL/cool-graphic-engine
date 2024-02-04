package cool.kolya.implementation.graphic.context2d;

import cool.kolya.engine.Engine;

public class Graphic2D {

    private static final ThreadLocal<ContextGraphic2D> CONTEXT_GRAPHIC_2D_THREAD_LOCAL = new ThreadLocal<>();

    public static void createContext() {
        CONTEXT_GRAPHIC_2D_THREAD_LOCAL.set(new ContextGraphic2D(Engine.CLEANER));
    }

    public static void enable() {
        ContextGraphic2D graphic2D = getContext();
        graphic2D.enable();
    }

    public static void disable() {
        getContext().disable();
    }

    public static void translate(float x, float y) {
        getContext().translate(x, y);
    }

    public static void scale(float x, float y) {
        getContext().scale(x, y);
    }

    public static void color(float r, float g, float b, float alpha) {
        getContext().color(r, g , b, alpha);
    }

    public static void rotate(float angle) {
        getContext().rotate(angle);
    }

    private static ContextGraphic2D getContext() {
        return CONTEXT_GRAPHIC_2D_THREAD_LOCAL.get();
    }
}