package cool.kolya.implementation.scene.element.impl;

import cool.kolya.engine.opengl.text.Character;
import cool.kolya.engine.opengl.text.Font;
import cool.kolya.implementation.FontManager;
import cool.kolya.implementation.scene.element.ElementImpl;

public class CharElement extends ElementImpl {
    private final Character character;

    public CharElement(char c) {
        Font font = FontManager.getActiveFont();
        texture = font.bitmapTex().getName();
        character = font.getCharacter(c);
        getProperties().getSize().set(character.getWidth(), character.getHeight());
    }

    public Character getCharacter() {
        return character;
    }
}
