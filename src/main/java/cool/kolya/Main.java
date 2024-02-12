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
import cool.kolya.implementation.animation.Animation;
import cool.kolya.implementation.animation.Easings;
import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.element.ContextElement;
import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.Elements;
import cool.kolya.implementation.scene.element.callback.Callback;
import cool.kolya.implementation.scene.element.impl.InteractingModule2D;
import cool.kolya.implementation.scene.element.impl.TextElement;
import cool.kolya.implementation.scene.element.property.PropertyVector;
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
            TransformProperties contextProperties = context.getProperties();
            contextProperties.getOrigin().set(0.5f, 0.5f);

            PropertyVector size = contextProperties.getSize();
            size.getChangeCallback().add(() -> {
                contextProperties.getOffset().set(size.x() / 2, size.y() / 2);
            });

            InteractingModule2D ctxInteractModule = new InteractingModule2D(context);
            ctxInteractModule.addCallback(Callback.InteractType.SCROLL, () -> {
                float scale = 0.05f * ctxInteractModule.getScrollYOffset();
                contextProperties.getScale().add(scale, scale);
            });
            context.setInteractingModule(ctxInteractModule);

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
            interactingModule.addCallback(Callback.InteractType.HOVER, () -> {
                if (interactingModule.isHovered()) {
                    textElement.setTextColor(new Vector4f());
                } else {
                    textElement.setTextColor(new Vector4f(0.2f, 1.0f, 0.7f, 1f));
                }
            });

            interactingModule.addCallback(Callback.InteractType.LEFT_PRESS, () ->
                    System.out.println("left pressed"));
            interactingModule.addCallback(Callback.InteractType.LEFT_RELEASE, () ->
                    System.out.println("left released"));
            interactingModule.addCallback(Callback.InteractType.SCROLL, () -> {
                float scroll = interactingModule.getScrollYOffset() * 10f;
                properties.getOffset().add(0, scroll);
            });
            textElement.setInteractingModule(interactingModule);
            processor.getEventBus().registerListener(new EventListener() {

                boolean animate = true;
                @EventHandler
                void handleUpdate(UpdateEvent updateEvent) {
                    if (animate) {
                        Animation.animate(textElement, Easings.QUAD_OUT, 60, Animation.ChangeType.ADD,
                                cp -> {
                                    cp.getOffset().set(-300, -150);
                                });
                        animate = false;
                    }
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
