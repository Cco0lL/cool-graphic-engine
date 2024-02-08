package cool.kolya.engine.opengl.text;

import cool.kolya.engine.opengl.texture.Texture2D;

public record Font(String name, Texture2D bitmapTex, Character[] characters) {

    public Font {
    }

    public Character getCharacter(char c) {
        return characters[c - 32];
    }
}
