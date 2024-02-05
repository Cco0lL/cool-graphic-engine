package cool.kolya;

import cool.kolya.engine.Engine;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;
import cool.kolya.engine.event.UpdateEvent;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.engine.process.ProcessSettings;
import cool.kolya.implementation.ProcessRunner;
import cool.kolya.implementation.Texture2DManager;
import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.element.context2d.Context2D;
import cool.kolya.implementation.scene.element.context2d.DrawableProperties2D;
import cool.kolya.implementation.scene.element.context2d.RectangleElement;
import cool.kolya.implementation.scene.element.general.IPropertyVector2f;

public class Main {

    public static void main(String[] args) {
        Engine.initialize();
        EngineProcess processor = Engine.newProcess();

        ProcessSettings windowSettings = processor.getSettings();
        windowSettings.setTitle("Test");
        windowSettings.setWindowSize(new WindowSize(800, 600));

        new ProcessRunner(processor, () -> {
            Texture2DManager.loadTexture("cool/kolya", "pickaxe.png");

            Context2D context2D = new Context2D();
            Scene scene = Scene.getContext();
            scene.addContext(context2D);

            RectangleElement rectangle = new RectangleElement();
            rectangle.setTexture("pickaxe");
            DrawableProperties2D properties = rectangle.getProperties();

            properties.getOffset().set(400f, 300f);
            //properties.getAlign().set(1f, 1f);
            //properties.getScale().set(2f, 2f);
            properties.getColor().set(0.5f, 0.5f, 1f, 1f);
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

            processor.getEventBus().registerListener(new EventListener() {

                private int tick;

                @EventHandler
                void handleUpdate(UpdateEvent event) {
                    if (++tick % 20 == 0) {
                      //  properties.getOffset().add(10f, 10f);
                    }
                }
            });

            context2D.addChild(rectangle);
        }).run();
    }
}
