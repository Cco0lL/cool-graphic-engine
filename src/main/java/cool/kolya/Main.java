package cool.kolya;

import cool.kolya.api.resource.ResourceSource;
import cool.kolya.engine.Engine;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;
import cool.kolya.engine.event.UpdateEvent;
import cool.kolya.engine.opengl.text.Font;
import cool.kolya.engine.process.EngineProcess;
import cool.kolya.engine.process.ProcessSettings;
import cool.kolya.implementation.FontManager;
import cool.kolya.implementation.ProcessRunner;
import cool.kolya.implementation.Texture2DManager;
import cool.kolya.implementation.scene.Scene;
import cool.kolya.implementation.scene.element.context2d.*;
import cool.kolya.implementation.scene.element.general.IPropertyVector2f;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.io.IOException;

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

            Texture2DManager.loadTexture(new ResourceSource("cool/kolya", "font.png"));
            Texture2DManager.loadTexture(new ResourceSource("cool/kolya", "pickaxe.png"));

            Context2D context2D = new Context2D();
            Scene scene = Scene.getContext();
            scene.addContext(context2D);

            TextElement textElement = new TextElement();
            textElement.setText("With a great power comes a great responsibility");
            DrawableProperties2D properties = textElement.getProperties();

            properties.getOffset().set(600f, 300f);

            RectangleElement rect = new RectangleElement();
            rect.setTexture("Roboto-Black");
            rect.getProperties().getSize().set(512, 512);
            rect.getProperties().getOffset().set(400f, 300f);
            textElement.getProperties().getColor().set(0.7f, 0.5f, 1f, 1f);
            context2D.addChild(rect);

            context2D.addChild(textElement);
        }).run();
    }
}
