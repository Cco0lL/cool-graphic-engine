package cool.kolya;

import cool.kolya.api.resource.ResourceSource;
import cool.kolya.engine.Engine;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;
import cool.kolya.engine.event.UpdateEvent;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.engine.process.ProcessSettings;
import cool.kolya.implementation.FontManager;
import cool.kolya.implementation.ProcessRunner;
import cool.kolya.implementation.Texture2DManager;
import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.element.ContextElement;
import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.Elements;
import cool.kolya.implementation.scene.element.callback.Callback;
import cool.kolya.implementation.scene.element.impl.InteractingModule2D;
import cool.kolya.implementation.scene.element.impl.TextElement;
import cool.kolya.implementation.scene.element.property.TransformProperties;
import org.joml.Vector4f;

import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        Engine.initialize();

        EngineProcess processor = Engine.newProcess();

        ProcessSettings windowSettings = processor.getSettings();
        windowSettings.setTitle("Test");
        windowSettings.setWindowSize(new WindowSize(800, 600));

        new ProcessRunner(processor, () -> {
            FontManager.loadFont(new ResourceSource("cool/kolya", "SmallestPixel7.ttf"));
            FontManager.loadFont(new ResourceSource("cool/kolya", "Roboto-Black.ttf"));
            FontManager.setActiveFont("Roboto-Black");

            Texture2DManager.loadTexture(new ResourceSource("cool/kolya", "pickaxe.png"));

            ContextElement context = Elements.newContext2D();
            context.getColor().set(0f, 0f, 0f, 1f);
            Scene scene = Scene.getContext();
            scene.addContext(context);

            TextElement textElement = Elements.newTextElement();
            textElement.setText("With a great power comes a great responsibility");
            textElement.setTextColor(new Vector4f(0.2f, 1.0f, 0.7f, 1f));
            textElement.getColor().set(1f);
            TransformProperties properties = textElement.getProperties();
            properties.getAlign().set(0.5f, 0.5f);
            properties.getOrigin().set(0.5f, 0.5f);

            InteractingModule2D interactingModule = new InteractingModule2D(textElement);
            interactingModule.addCallback(Callback.Type.HOVER, () -> {
                if (interactingModule.isHovered()) {
                    textElement.setTextColor(new Vector4f());
                } else {
                    textElement.setTextColor(new Vector4f(0.2f, 1.0f, 0.7f, 1f));
                }
            });
            textElement.setInteractingModule(interactingModule);
            processor.getEventBus().registerListener(new EventListener() {
                @EventHandler
                void handleUpdate(UpdateEvent updateEvent) {
                    if (interactingModule.isHovered()) {
                        return;
                    }
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    for (Element child : textElement.getChildren()) {
                        child.getColor().set(random.nextFloat(), random.nextFloat(), random.nextFloat());
                    }
                }
            });

            context.addChild(textElement);
        }).run();
    }
}
