package cool.kolya.implementation.scene.element;

import cool.kolya.implementation.scene.element.impl.CharElement;
import cool.kolya.implementation.scene.element.impl.Context2D;
import cool.kolya.implementation.scene.element.impl.RectangleDrawingModule;
import cool.kolya.implementation.scene.element.impl.TextElement;
import cool.kolya.implementation.scene.element.matrix.ElementTransformMatrix;

public class Elements {

    public static Element newRectangle() {
        Element rectangle = new ElementImpl();
        applyDefaultRectangleSettings(rectangle);
        return rectangle;
    }

    public static ContextElement newContext2D() {
        Context2D context = new Context2D();
        context.setTransformMatrix(new Context2D.Context2DTransformMatrix(context));
        context.setWindowSettingsInterpreter(new WindowSettingsInterpreter.Default());
        DrawingModule drawingModule = new RectangleDrawingModule();
        drawingModule.setBoundedElement(context);
        context.setDrawingModule(drawingModule);
        return context;
    }

    public static CharElement newCharElement(char c) {
        CharElement charElement = new CharElement(c);
        DrawingModule drawingModule = new RectangleDrawingModule(charElement.getCharacter().getTexVertices());
        drawingModule.setBoundedElement(charElement);
        charElement.setDrawingModule(drawingModule);
        charElement.setTransformMatrix(new ElementTransformMatrix(charElement));
        return charElement;
    }

    public static TextElement newTextElement() {
        TextElement textElement = new TextElement();
        applyDefaultRectangleSettings(textElement);
        return textElement;
    }


    public static void applyDefaultRectangleSettings(Element element) {
        DrawingModule drawingModule = new RectangleDrawingModule();
        drawingModule.setBoundedElement(element);
        element.setDrawingModule(drawingModule);
        element.setTransformMatrix(new ElementTransformMatrix(element));
    }
}
