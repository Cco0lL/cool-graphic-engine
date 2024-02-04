package cool.kolya;

import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.element.context2d.Context2D;
import cool.kolya.engine.Engine;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.engine.process.ProcessSettings;
import cool.kolya.implementation.ProcessRunner;
import cool.kolya.implementation.scene.element.context2d.Drawable2DProperties;
import cool.kolya.implementation.scene.element.context2d.RectangleElement;
import cool.kolya.implementation.scene.element.general.IPropertyVector2f;
import cool.kolya.implementation.scene.element.general.IPropertyVector3f;

public class Main {

    public static void main(String[] args) {
        Engine.initialize();
        //Engine.getMainThreadTaskScheduler().offerInTaskQueue(() -> newProcess(1));
        //newProcess(1);
        EngineProcess processor = Engine.newProcess();

        ProcessSettings windowSettings = processor.getSettings();
        windowSettings.setTitle("Test");
        windowSettings.setWindowSize(new WindowSize(800, 600));

        new ProcessRunner(processor, () -> {
            Context2D context2D = new Context2D();
            Scene scene = Scene.getContext();
            scene.addContext(context2D);

            RectangleElement rectangle = new RectangleElement();
            Drawable2DProperties properties = rectangle.getProperties();

            properties.getOffset().set(400f, 300f);
            //properties.getScale().set(2f, 2f);
            properties.getColor().set(0.5f, 0.5f, 0.5f, 1.0f);
            properties.getSize().set(100f, 100f);
            properties.markDirty();

            rectangle.addHoverCallback(() -> {
                IPropertyVector2f size = properties.getSize();
                if (!rectangle.isHovered()) {
                    size.set(size.x() + 100, size.y() + 100);
                } else {
                    size.set(size.x() - 100, size.y() - 100);
                }
                properties.markDirty();
            });

            context2D.addChild(rectangle);
        }).run();
    }

    public static void newProcess(int i) {
        EngineProcess processor = Engine.newProcess();

        ProcessSettings windowSettings = processor.getSettings();
        windowSettings.setTitle("Test" + i);
        windowSettings.setWindowSize(new WindowSize(800, 600));

        new ProcessRunner(processor, () -> {
            Context2D context2D = new Context2D();
            Scene scene = Scene.getContext();
            scene.addContext(context2D);

            RectangleElement rectangle = new RectangleElement();
            Drawable2DProperties properties = rectangle.getProperties();

            properties.getOffset().set(400f, 300f);
            //properties.getScale().set(2f, 2f);
            properties.getColor().set(0.5f, 0.5f, 0.5f, 1.0f);
            properties.getSize().set(100f, 100f);
            properties.markDirty();

            rectangle.addHoverCallback(() -> {
                IPropertyVector2f size = properties.getSize();
                if (!rectangle.isHovered()) {
                    size.set(size.x() + 100, size.y() + 100);
                } else {
                    size.set(size.x() - 100, size.y() - 100);
                }
                properties.markDirty();
            });

            context2D.addChild(rectangle);
        }).run();
    }
}
