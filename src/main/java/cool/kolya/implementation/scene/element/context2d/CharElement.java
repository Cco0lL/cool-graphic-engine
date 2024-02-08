package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.engine.opengl.text.Character;
import cool.kolya.engine.opengl.text.Font;
import cool.kolya.implementation.FontManager;

public class CharElement extends RectangleElement {

    private final Character character;

    public CharElement(char c) {
        Font font = FontManager.getActiveFont();
        texture = font.bitmapTex().getName();
        character = font.getCharacter(c);
        texVertices = character.getTexVertices();
        updateTex();
        getProperties().getSize().set(character.getWidth(), character.getHeight());
    }

    public Character getCharacter() {
        return character;
    }
}
